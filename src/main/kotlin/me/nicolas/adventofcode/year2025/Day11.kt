package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 11: Reactor ---
// https://adventofcode.com/2025/day/11
fun main() {
    val data = readFileDirectlyAsText("/year2025/day11/data.txt")
    val day = Day11(2025, 11)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
    prettyPrintPartTwo { day.partTwoOptimised(data) }
}

class Day11(year: Int, day: Int, title: String = "Reactor") : AdventOfCodeDay(year, day, title) {
    /**
     * Counts the number of distinct paths from "you" to "out" in the given graph.
     * The graph is represented as a map where keys are node names and values are lists of connected nodes.
     * It uses a depth-first search (DFS) with memoization to avoid re-computing path counts.
     */
    fun partOne(data: String): Long {
        return countPaths("you", "out", parseInput(data))
    }

    /**
     * Counts the number of paths from "svr" to "out" that pass through both "dac" and "fft".
     * It calculates the paths for two separate cases:
     * 1. svr -> ... -> dac -> ... -> fft -> ... -> out
     * 2. svr -> ... -> fft -> ... -> dac -> ... -> out
     * The total number of paths is the sum of the paths from these two disjoint cases.
     * The number of paths for each segment is calculated using the countPaths function.
     */
    fun partTwoOptimised(data: String): Long {
        val connections = parseInput(data)

        // Case 1: svr -> ... -> dac -> ... -> fft -> ... -> out
        val pathsSvrToDac = countPaths("svr", "dac", connections)
        val pathsDacToFft = countPaths("dac", "fft", connections)
        val pathsFftToOut = countPaths("fft", "out", connections)
        // Total paths for case 1 is the product of the paths in each segment
        val totalCase1 = pathsSvrToDac * pathsDacToFft * pathsFftToOut

        // Case 2: svr -> ... -> fft -> ... -> dac -> ... -> out
        val pathsSvrToFft = countPaths("svr", "fft", connections)
        val pathsFftToDac = countPaths("fft", "dac", connections)
        val pathsDacToOut = countPaths("dac", "out", connections)
        // Total paths for case 2 is the product of the paths in each segment
        val totalCase2 = pathsSvrToFft * pathsFftToDac * pathsDacToOut

        return totalCase1 + totalCase2
    }

    /**
     * Counts the number of paths from "svr" to "out" that pass through both "dac" and "fft".
     * This is a naive implementation that uses a different approach:
     * It explores all paths from "svr" to "out" and counts only those that have visited both "dac" and "fft".
     */
    fun partTwo(data: String): Long {
        return countPathsWithState("svr", "out", parseInput(data))
    }

    private fun parseInput(data: String): Map<String, List<String>> {
        return data.lines().associate { line ->
            val (source, destinations) = line.split(": ")
            source to destinations.split(" ")
        }
    }

    /**
     * Recursive function to count paths from start to end while tracking if "dac" and "fft" have been visited.
     * Uses DFS with memoization to avoid re-computing path counts for the same state.
     */
    private fun countPathsWithState(
        start: String,
        end: String,
        connections: Map<String, List<String>>,
        hasVisitedDac: Boolean = false,
        hasVisitedFft: Boolean = false,
        memo: MutableMap<Triple<String, Boolean, Boolean>, Long> = mutableMapOf(),
    ): Long {
        // Base case: reached the end node
        if (start == end) {
            // Count this path only if both dac and fft have been visited
            return if (hasVisitedDac && hasVisitedFft) 1L else 0L
        }

        val memoKey = Triple(start, hasVisitedDac, hasVisitedFft)
        memo[memoKey]?.let { return it }

        val destinations = connections[start] ?: return 0L

        val count = destinations.sumOf { destination ->
            val newHasVisitedDac = hasVisitedDac || destination == "dac"
            val newHasVisitedFft = hasVisitedFft || destination == "fft"
            countPathsWithState(destination, end, connections, newHasVisitedDac, newHasVisitedFft, memo)
        }

        memo[memoKey] = count

        return count
    }

    /**
     * Count paths from start to end using DFS (Depth-First Search) with memoization.
     */
    private fun countPaths(
        start: String,
        end: String,
        connections: Map<String, List<String>>,
        memo: MutableMap<String, Long> = mutableMapOf(),
    ): Long {
        if (start == end) {
            return 1L
        }
        // Check if result is already computed
        memo[start]?.let { return it }

        val destinations = connections[start] ?: return 0L

        val count = destinations.sumOf { destination ->
            countPaths(destination, end, connections, memo)
        }
        memo[start] = count

        return count
    }
}