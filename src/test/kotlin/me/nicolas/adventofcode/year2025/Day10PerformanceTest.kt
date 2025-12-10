package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class Day10PerformanceTest {
    private val day = Day10(2025, 10)

    val test = """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent()

    @Test
    fun compareBacktrackingVsZ3() {
        val machines = test.lines()
            .filter { it.isNotBlank() }
            .map { machine -> day.parseMachine(machine) }

        println("\n=== Performance Comparison (Optimized Backtracking vs Z3) ===\n")

        // Test Optimized Backtracking
        var backtrackingResult = 0
        val backtrackingTime = measureTimeMillis {
            backtrackingResult = machines.sumOf { machine -> day.findMinPressesForJoltage(machine) }
        }

        // Test Z3 Solver
        var z3Result = 0
        val z3Time = measureTimeMillis {
            z3Result = machines.sumOf { machine -> day.findMinPressesForJoltageZ3(machine) }
        }

        println("Optimized Backtracking: $backtrackingResult in ${backtrackingTime}ms")
        println("Z3 Solver:              $z3Result in ${z3Time}ms")

        if (z3Time > 0) {
            println("Z3 Speedup:             ${backtrackingTime.toDouble() / z3Time}x faster")
        }

        assert(backtrackingResult == z3Result) { "Results don't match! Backtracking=$backtrackingResult, Z3=$z3Result" }
    }
}

