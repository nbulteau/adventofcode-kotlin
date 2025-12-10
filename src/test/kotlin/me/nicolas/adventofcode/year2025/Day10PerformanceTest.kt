package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class Day10PerformanceTest {
    private val day = Day10(2025, 10)

    val test = """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
        [.#...#.#..] (0,2,4,5,7,8,9) (4,6) (0,6) (1,2,3,5,8,9) (0,3,4,5,6,8) (1,3,5,6,9) (0,1,2,4,5,6,8) (1,3,5,7,8,9) (1,3,6,7,8) (2,5,6,8,9) (0,1,2,4,5,6,8,9) {42,210,196,207,47,232,92,48,233,227}
        [.#.....#.#] (1,2,3,4,5,6,7,8) (0,1,3,4,6,7,8,9) (1,2,3,4,5,6,8) (0,1,2,3,5,6,7,8) (0,2,3,4,6,7,8,9) (0,3,4,7) (1,4,6,7) (0,4,5,8,9) (1,4,5,7,8,9) (1,3,4,5,6,7,8,9) (0,6,7) (0,1,2,3,4,5,7,9) (1,2,3,4,6,8,9) {242,110,78,295,305,83,116,282,116,76}
    """.trimIndent()

    @Test
    fun compareSolvers() {
        val machines = test.lines()
            .filter { it.isNotBlank() }
            .map { machine -> day.parseMachine(machine) }


        // Test Ojalgo Solver
        var ojalgoResult = 0
        val ojalgoTime = measureTimeMillis {
            ojalgoResult = machines.sumOf { machine -> day.findMinPressesForJoltageOjalgo(machine) }
        }
        println("Ojalgo Solver:          $ojalgoResult in ${ojalgoTime}ms")

        // Test Linear Solver
        var linearSolverResult = 0
        val linearSolverTime = measureTimeMillis {
            linearSolverResult = machines.sumOf { machine -> day.findMinPressesForJoltage(machine) }
        }
        println("Linear Solver:          $linearSolverResult in ${linearSolverTime}ms")

        // Test Z3 Solver
        var z3Result = 0
        val z3Time = measureTimeMillis {
            z3Result = machines.sumOf { machine -> day.findMinPressesForJoltageZ3(machine) }
        }
        println("Z3 Solver:              $z3Result in ${z3Time}ms")

        assert(ojalgoResult == linearSolverResult) { "Results don't match! Ojalgo=$ojalgoResult, LinearSolver=$linearSolverResult" }
        assert(ojalgoResult == z3Result) { "Results don't match! Ojalgo=$ojalgoResult, Z3=$z3Result" }
    }
}
