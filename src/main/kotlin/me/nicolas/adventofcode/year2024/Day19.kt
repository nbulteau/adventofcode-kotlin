package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 19:Linen Layout ---
// https://adventofcode.com/2024/day/19
fun main() {
    val data = readFileDirectlyAsText("/year2024/day19/data.txt")
    val day = Day19(2024, 19)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

/**
The code determine how many designs can be constructed using a given set of towel patterns and then count the number of ways each design can be constructed.

partOne Method:
- Parses the input data to get the list of patterns and designs.
- Counts how many designs can be constructed using the available patterns.
- Uses the canConstructDesign method to check if a design can be constructed.

partTwo Method:
- Parses the input data to get the list of patterns and designs.
- Sums up the number of ways each design can be constructed using the available patterns.
- Uses the countWaysToConstructDesign method to count the number of ways to construct a design.

Helper Methods:
- canConstructDesign: Recursively checks if a design can be constructed using the available patterns.
Uses a cache to store results for subproblems to avoid redundant calculations.
- countWaysToConstructDesign: Recursively counts the number of ways to construct a design using the available patterns.
Uses a cache to store results for subproblems to avoid redundant calculations.
 */

class Day19(year: Int, day: Int, title: String = "Linen Layout") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val (patterns, designs) = parseInput(data)

        return designs.count { design -> canConstructDesign(design, patterns) }
    }

    fun partTwo(data: String): Long {
        val (patterns, designs) = parseInput(data)

        return designs.sumOf { design -> countWaysToConstructDesign(design, patterns) }
    }

    // Return true if the design can be constructed using the patterns
    private fun canConstructDesign(design: String, patterns: List<String>, cache: MutableMap<String, Boolean> = mutableMapOf()): Boolean {
        if (design.isEmpty()) {
            return true
        }
        if (cache.containsKey(design)) {
            return cache[design]!!
        }

        for (pattern in patterns) {
            if (design.startsWith(pattern)) {
                if (canConstructDesign(design.removePrefix(pattern), patterns, cache)) {
                    cache[design] = true

                    return true
                }
            }
        }
        cache[design] = false

        return false
    }

    // Return the number of ways to construct the design using the patterns
    private fun countWaysToConstructDesign(design: String, patterns: List<String>, cache: MutableMap<String, Long> = mutableMapOf()): Long {
        if (design.isEmpty()) {
            return 1
        }
        if (cache.containsKey(design)) {
            return cache[design]!!
        }

        var count = 0L
        for (pattern in patterns) {
            if (design.startsWith(pattern)) {
                count += countWaysToConstructDesign(design.removePrefix(pattern), patterns, cache)
            }
        }
        cache[design] = count

        return count
    }

    private fun parseInput(data: String): Pair<List<String>, List<String>> {
        val lines = data.lines()
        val patterns = lines.first().split(", ").map { line -> line.trim() }
        val designs = lines.drop(2).map { line -> line.trim() }

        return Pair(patterns, designs)
    }
}