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