package me.nicolas.adventofcode.year2025

import com.microsoft.z3.*
import kotlinx.coroutines.*
import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 10: Factory ---
// https://adventofcode.com/2025/day/10
fun main() {
    val data = readFileDirectlyAsText("/year2025/day10/data.txt")
    val day = Day10(2025, 10)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwoWithZ3(data) }
    //prettyPrintPartTwo { day.partTwo(data) }
}

class Day10(year: Int, day: Int, title: String = "Factory") : AdventOfCodeDay(year, day, title) {

    data class Machine(
        val targetLights: List<Boolean>,
        val buttons: List<List<Int>>,
        val joltageRequirements: List<Int>
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
     * Uses a brute-force approach by checking all possible combinations of button presses.
     * This is feasible for a small number of buttons due to the exponential growth of combinations.
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

    /**
     * Finds the minimum number of button presses required to achieve the target joltage levels.
     * This is a system of linear equations over integers (not GF(2)).
     * We need to find non-negative integer values for each button press count that:
     * 1. Satisfies all joltage requirements exactly
     * 2. Minimizes the total number of button presses
     *
     * Algorithm: Optimized backtracking with advanced pruning
     * - For each button, try all possible press counts from 0 to a calculated upper bound
     * - Upper bound for a button = min remaining value among all counters it affects
     * - Multiple pruning strategies:
     *   1. Branch bound pruning: skip if current presses + lower bound >= best solution
     *   2. Infeasibility detection: skip if remaining buttons can't satisfy remaining targets
     *   3. Early termination: stop when optimal solution is found
     */
    fun findMinPressesForJoltage(machine: Machine): Int {
        val numCounters = machine.joltageRequirements.size
        val numButtons = machine.buttons.size
        val target = machine.joltageRequirements.toIntArray()

        // Track the minimum number of presses found so far
        var minPresses = Int.MAX_VALUE

        // Precompute which buttons affect which counters (inverse mapping)
        val counterToButtons = Array(numCounters) { mutableListOf<Int>() }
        for (buttonIdx in 0 until numButtons) {
            for (counterIdx in machine.buttons[buttonIdx]) {
                if (counterIdx < numCounters) {
                    counterToButtons[counterIdx].add(buttonIdx)
                }
            }
        }

        /**
         * Calculate a lower bound on remaining button presses needed
         * This helps prune branches earlier
         */
        fun calculateLowerBound(counters: IntArray, startButtonIdx: Int): Int {
            var lowerBound = 0
            for (counterIdx in 0 until numCounters) {
                val remaining = target[counterIdx] - counters[counterIdx]
                if (remaining > 0) {
                    // Find the button that affects this counter most efficiently among remaining buttons
                    val availableButtons = counterToButtons[counterIdx].filter { it >= startButtonIdx }
                    if (availableButtons.isEmpty()) {
                        return Int.MAX_VALUE // Impossible to satisfy
                    }
                    // Optimistic lower bound: assume we can satisfy this counter with minimal presses
                    lowerBound = maxOf(lowerBound, remaining)
                }
            }
            return lowerBound
        }

        /**
         * Recursive backtracking search with optimizations
         */
        fun search(buttonIdx: Int, currentTotal: Int, counters: IntArray) {
            // Base case: all buttons have been considered
            if (buttonIdx == numButtons) {
                // Check if we've reached the target exactly
                if (counters.contentEquals(target)) {
                    minPresses = minOf(minPresses, currentTotal)
                }
                return
            }

            // Pruning 1: Branch bound with lower bound estimation
            val lowerBound = calculateLowerBound(counters, buttonIdx)
            if (currentTotal + lowerBound >= minPresses) return

            // Calculate the upper bound for this button
            // Don't press more than needed to reach any counter's remaining target
            var maxForButton = target.maxOrNull() ?: 0
            for (counterIdx in machine.buttons[buttonIdx]) {
                if (counterIdx < numCounters) {
                    val remaining = target[counterIdx] - counters[counterIdx]
                    if (remaining < 0) {
                        maxForButton = -1 // This counter is already exceeded
                        break
                    }
                    maxForButton = minOf(maxForButton, remaining)
                }
            }

            // Try pressing this button from 0 to maxForButton times
            for (presses in 0..maxOf(0, maxForButton)) {
                // Early pruning: skip if this would exceed our current best
                if (currentTotal + presses >= minPresses) break

                val newCounters = counters.copyOf()
                var valid = true

                // Update counters affected by this button
                for (counterIdx in machine.buttons[buttonIdx]) {
                    if (counterIdx < numCounters) {
                        newCounters[counterIdx] += presses
                        // Prune: if any counter exceeds its target, this branch is invalid
                        if (newCounters[counterIdx] > target[counterIdx]) {
                            valid = false
                            break
                        }
                    }
                }

                // Recurse to the next button if this configuration is valid
                if (valid) {
                    search(buttonIdx + 1, currentTotal + presses, newCounters)
                }
            }
        }

        search(0, 0, IntArray(numCounters))
        return if (minPresses == Int.MAX_VALUE) 0 else minPresses
    }

    /**
     * Finds the minimum number of button presses using Z3 solver.
     * This is much faster than brute force for large problems.
     * Z3 solves the system of linear equations and minimizes the objective function.
     */
    fun findMinPressesForJoltageZ3(machine: Machine): Int {
        val numCounters = machine.joltageRequirements.size
        val numButtons = machine.buttons.size
        val target = machine.joltageRequirements

        // Use Z3 to solve the linear system
        Context().use { context ->
            with(context) {
                // Extension functions for Z3 API (similar to Day24 2023)
                operator fun IntExpr.times(other: IntExpr): IntExpr = mkMul(this, other) as IntExpr
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

    fun partOne(data: String): Int {
        return data.lines()
            .filter { line -> line.isNotBlank() }
            .sumOf { line ->
                val machine = parseMachine(line)
                findMinPresses(machine)
            }
    }

    fun partTwo(data: String): Int = runBlocking {
        // Process each machine for joltage configuration in parallel using coroutines

        val machines = buildAllMachines(data)
        val totalMachines = machines.size

        val availableProcessors = Runtime.getRuntime().availableProcessors()
        println("Processing $totalMachines machines in parallel using $availableProcessors cores...")

        // Process machines in parallel using coroutines with optimized backtracking
        val results = machines.map { machine ->
            async(Dispatchers.Default) {
                findMinPressesForJoltage(machine)
            }
        }.awaitAll()

        // Sum all results
        results.sum()
    }

    fun partTwoWithZ3(data: String): Int = runBlocking {
        // Process each machine for joltage configuration in parallel using coroutines
        // Use Z3 solver for efficient solving of the linear system

        val machines = buildAllMachines(data)
        val totalMachines = machines.size

        val availableProcessors = Runtime.getRuntime().availableProcessors()
        println("Processing $totalMachines machines in parallel using Z3 solver on $availableProcessors cores...")

        // Process machines in parallel using coroutines with Z3 solver
        val results = machines.map { machine ->
            async(Dispatchers.Default) {
                findMinPressesForJoltageZ3(machine)
            }
        }.awaitAll()

        // Sum all results
        results.sum()
    }
}
