package me.nicolas.adventofcode.year2025

import com.microsoft.z3.BoolExpr
import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import org.ojalgo.optimisation.ExpressionsBasedModel
import java.util.*
import kotlin.math.roundToInt
import kotlin.use

// --- Day 10: Factory ---
// https://adventofcode.com/2025/day/10
fun main() {
    val data = readFileDirectlyAsText("/year2025/day10/data.txt")
    val day = Day10(2025, 10)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwoWithLinearSolver(data) }
    prettyPrintPartTwo { day.partTwoWithOjalgo(data) }
    prettyPrintPartTwo { day.partTwoWithZ3(data) }
}

class Day10(year: Int, day: Int, title: String = "Factory") : AdventOfCodeDay(year, day, title) {

    data class Machine(
        val targetLights: List<Boolean>,
        val buttons: List<List<Int>>,
        val joltageRequirements: List<Int>,
    )

    fun buildAllMachines(data: String): List<Machine> {
        return data.lines()
            .filter { line -> line.isNotBlank() }
            .map { line -> parseMachine(line) }
    }

    fun parseMachine(line: String): Machine {
        val parts = line.split(Regex("[\\[\\]()]"))
            .filter { it.isNotBlank() && it.trim().isNotEmpty() }

        val targetLights = parts[0].map { it == '#' }

        val buttons = parts.drop(1)
            .takeWhile { !it.contains('{') }
            .map { buttonStr ->
                buttonStr.trim().split(',').map { it.trim().toInt() }
            }

        // Extract joltage requirements from the {curly braces} section
        val joltageStr = line.substringAfter('{').substringBefore('}')
        val joltageRequirements = joltageStr.split(',').map { it.trim().toInt() }

        return Machine(targetLights, buttons, joltageRequirements)
    }

    /**
     * Finds the minimum number of button presses required to achieve the target light configuration.
     * Each button can be pressed either 0 or 1 time (in GF(2)).
     * https://en.wikipedia.org/wiki/GF(2)
     *
     * Use a brute-force approach by checking all possible combinations of button presses.
     * This is possible for a small number of buttons due to the exponential growth of combinations.
     */
    fun findMinPresses(machine: Machine): Int {
        val numLights = machine.targetLights.size
        val numButtons = machine.buttons.size

        // This problem operates in GF(2) (Galois Field of order 2), also known as binary field or Z/2Z

        // In GF(2):
        // - All arithmetic is done modulo 2 (only values 0 and 1 exist)
        // - Addition is equivalent to XOR: 0+0=0, 0+1=1, 1+0=1, 1+1=0
        // - Pressing a button twice cancels out (1+1=0 mod 2), same as not pressing it
        // Therefore, each button only needs to be pressed 0 or 1 times in any optimal solution

        // Try all possible combinations of button presses (each button pressed 0 or 1 times in GF(2))
        var minPresses = Int.MAX_VALUE

        for (mask in 0 until (1 shl numButtons)) {
            val lights = BooleanArray(numLights)
            var presses = 0

            for (buttonIdx in 0 until numButtons) {
                if (mask and (1 shl buttonIdx) != 0) {
                    presses++
                    // Toggling is the physical manifestation of XOR/addition in GF(2)
                    for (lightIdx in machine.buttons[buttonIdx]) {
                        lights[lightIdx] = !lights[lightIdx]
                    }
                }
            }

            if (lights.contentEquals(machine.targetLights.toBooleanArray())) {
                minPresses = minOf(minPresses, presses)
            }
        }

        return if (minPresses == Int.MAX_VALUE) 0 else minPresses
    }

    fun findMinPressesForJoltage(machine: Machine): Int {
        val numCounters = machine.joltageRequirements.size
        val target = machine.joltageRequirements

        // Check for impossible cases: a counter has a non-zero target but no button affects it.
        val coveredCounters = machine.buttons.flatten().toSet()
        for (i in 0 until numCounters) {
            if (i !in coveredCounters && target[i] > 0) {
                return 0 // Impossible, but problem expects sum, so return 0 for this machine
            }
        }

        // Decompose the problem into independent subproblems (connected components)
        val components = findConnectedComponents(numCounters, machine.buttons)
        var totalPresses = 0L

        for (comp in components) {
            val compMap = comp.withIndex().associate { (idx, counter) -> counter to idx }
            val k = comp.size
            val subTarget = DoubleArray(k) { i -> target[comp[i]].toDouble() }

            // Filter buttons relevant to this component
            val relevantButtons = machine.buttons.withIndex()
                .filter { (_, buttons) -> buttons.any { it in comp } }

            val l = relevantButtons.size
            val aMat = Array(k) { DoubleArray(l) }

            relevantButtons.forEachIndexed { j, (_, btn) ->
                for (counter in btn) {
                    compMap[counter]?.let { compIdx ->
                        aMat[compIdx][j] = 1.0
                    }
                }
            }

            val subResult = LinearSolver().solve(k, l, aMat, subTarget)
            if (subResult == Long.MAX_VALUE) {
                return 0 // Inconsistent subproblem, fail the entire machine
            }
            totalPresses += subResult
        }

        return totalPresses.toInt()
    }

    private fun findConnectedComponents(numCounters: Int, buttons: List<List<Int>>): List<List<Int>> {
        val adj = Array(numCounters) { mutableListOf<Int>() }
        for (btn in buttons) {
            for (i in btn.indices) {
                for (j in i + 1 until btn.size) {
                    adj[btn[i]].add(btn[j])
                    adj[btn[j]].add(btn[i])
                }
            }
        }

        val seen = BooleanArray(numCounters)
        val components = mutableListOf<List<Int>>()
        for (u in 0 until numCounters) {
            if (!seen[u]) {
                val component = mutableListOf<Int>()
                val queue: Queue<Int> = LinkedList()
                queue.add(u)
                seen[u] = true
                while (queue.isNotEmpty()) {
                    val v = queue.poll()
                    component.add(v)
                    for (w in adj[v]) {
                        if (!seen[w]) {
                            seen[w] = true
                            queue.add(w)
                        }
                    }
                }
                components.add(component.sorted())
            }
        }
        return components
    }

    /**
     * Solves the joltage puzzle using the Ojalgo library, a pure Java library for mathematics,
     * linear algebra, and optimization. This method models the problem as an Integer Linear
     * Programming (ILP) problem.
     *
     * The ILP model is structured as follows:
     * - **Variables**: `x_i` representing the number of presses for each button `i`. These are
     *   defined as non-negative integer variables.
     * - **Constraints**: For each counter `j`, a linear equality constraint is created to ensure
     *   that the sum of presses of buttons affecting it equals the target requirement `t_j`.
     *   `sum(x_i for all i affecting j) = t_j`.
     * - **Objective Function**: The goal is to minimize the total number of button presses,
     *   which is formulated as minimizing the sum of all variables: `minimize(sum(x_i))`.
     *
     * Ojalgo provides a high-level API to build and solve such optimization models.
     * https://www.ojalgo.org/
     * https://github.com/optimatika/ojAlgo
     */
    fun findMinPressesForJoltageOjalgo(machine: Machine): Int {
        val numCounters = machine.joltageRequirements.size
        val numButtons = machine.buttons.size
        val targets = machine.joltageRequirements

        if (numButtons == 0) {
            return if (targets.all { it == 0 }) 0 else -1
        }

        val model = ExpressionsBasedModel()

        // Create non-negative integer variables
        val variables = (0 until numButtons).map { i ->
            model.newVariable("x$i").lower(0).integer(true)
        }

        // Add equality constraints
        for (c in 0 until numCounters) {
            val constraint = model.newExpression("c$c").level(targets[c].toBigDecimal())
            for (b in 0 until numButtons) {
                if (c in machine.buttons[b]) {
                    constraint.set(variables[b], 1)
                }
            }
        }

        // Minimize sum of all variables
        variables.forEach { variable -> variable.weight(1) }

        // Solve
        val result = model.minimise()

        return if (result.state.isOptimal || result.state.isFeasible) {
            result.value.roundToInt()
        } else {
            0 // Should indicate an error or impossible case, returning 0 for sum
        }
    }

    /**
     * Solves the joltage puzzle using the Z3 solver, an SMT (Satisfiability Modulo Theories) solver.
     * This method models the problem as a system of linear integer equations and uses Z3 to find the
     * optimal solution that minimizes the total number of button presses.
     *
     * The problem is defined as follows:
     * - Let `x_i` be the number of times button `i` is pressed.
     * - For each counter `j`, the sum of presses of buttons affecting it must equal the target `t_j`.
     *   This gives a set of linear equations: `sum(x_i for all i affecting j) = t_j`.
     * - The variables `x_i` must be non-negative integers.
     * - The objective is to minimize `sum(x_i)`.
     *
     * Z3 is particularly powerful for such constraint satisfaction and optimization problems.
     */
    fun findMinPressesForJoltageZ3(machine: Machine): Int {
        val numCounters = machine.joltageRequirements.size
        val numButtons = machine.buttons.size
        val target = machine.joltageRequirements

        // Use Z3 to solve the linear system
        Context().use { context ->
            with(context) {
                // Extension functions for Z3 API (similar to Day24 2023)
                operator fun IntExpr.plus(other: IntExpr): IntExpr = mkAdd(this, other) as IntExpr
                infix fun IntExpr.eq(other: IntExpr): BoolExpr = mkEq(this, other)
                fun Int.int() = mkInt(this)

                val solver = mkOptimize()

                // Create integer variables for button press counts
                val buttonPresses = (0 until numButtons).map { i ->
                    mkIntConst("button_$i")
                }

                // Constraint: all button presses must be non-negative
                buttonPresses.forEach { press ->
                    solver.Add(mkGe(press, 0.int()))
                }

                // Constraint: each counter must equal its target value
                for (counterIdx in 0 until numCounters) {
                    // Sum of all button presses that affect this counter
                    val affectingButtons = buttonPresses.filterIndexed { buttonIdx, _ ->
                        counterIdx in machine.buttons[buttonIdx]
                    }

                    if (affectingButtons.isNotEmpty()) {
                        val sum = affectingButtons.reduce { acc, press -> acc + press }
                        solver.Add(sum eq target[counterIdx].int())
                    } else {
                        // No button affects this counter, it must be 0
                        if (target[counterIdx] != 0) {
                            return 0 // Impossible to satisfy
                        }
                    }
                }

                // Minimize the total number of button presses
                val totalPresses = buttonPresses.reduce { acc, press -> acc + press }
                solver.MkMinimize(totalPresses)

                return when (solver.Check()) {
                    Status.UNSATISFIABLE -> 0 // No solution
                    null, Status.UNKNOWN -> 0 // Unknown
                    Status.SATISFIABLE -> {
                        val model = solver.model
                        buttonPresses.sumOf { press ->
                            (model.evaluate(press, false) as IntNum).int
                        }
                    }
                }
            }
        }
    }

    /**
     * Part One using brute-force approach.
     */
    fun partOne(data: String): Int {
        val machines = buildAllMachines(data)
        return machines.sumOf { machine -> findMinPresses(machine) }
    }

    /**
     * Part Two using the custom LinearSolver.
     */
    fun partTwoWithLinearSolver(data: String): Int {
        val machines = buildAllMachines(data)
        return machines.sumOf { machine -> findMinPressesForJoltage(machine) }
    }

    /**
     * Part Two using Ojalgo solver for better performance on large inputs.
     */
    fun partTwoWithOjalgo(data: String): Int {
        val machines = buildAllMachines(data)
        return machines.sumOf { machine -> findMinPressesForJoltageOjalgo(machine) }
    }

    /**
     * Part Two using Z3 solver for better performance on large inputs.
     */
    fun partTwoWithZ3(data: String): Int {
        val machines = buildAllMachines(data)
        return machines.sumOf { machine -> findMinPressesForJoltageZ3(machine) }
    }
}
