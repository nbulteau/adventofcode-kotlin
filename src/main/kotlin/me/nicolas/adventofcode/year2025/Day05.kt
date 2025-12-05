package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 5: Cafeteria ---
// https://adventofcode.com/2025/day/5
fun main() {
    val data = readFileDirectlyAsText("/year2025/day05/data.txt")
    val day = Day05(2025, 5, "Cafeteria")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day05(year: Int, day: Int, title: String = "Cafeteria") : AdventOfCodeDay(year, day, title) {

    /**
     * Count how many of our available ingredients are actually fresh.
     *
     * We check each ingredient ID we have against the fresh ranges.
     * If an ingredient falls within any range, it's fresh.
     */
    fun partOne(data: String): Int {
        val (ranges, ingredientIds) = parseInput(data)

        return ingredientIds.count { id -> ranges.any { range -> id in range } }
    }

    /**
     * Find out how many ingredient IDs total are covered by the fresh ranges.
     *
     * We don't care about our actual ingredients anymore - just figure out how many
     * unique IDs exist across all the ranges. Since ranges can overlap (like 10-14 and 12-18),
     * we merge them first to avoid counting the same IDs twice.
     */
    fun partTwo(data: String): Long {
        val (ranges, _) = parseInput(data)
        val mergedRanges = mergeRanges(ranges)

        return mergedRanges.sumOf { range -> (range.last - range.first + 1) }
    }

    private fun parseInput(data: String): Pair<List<LongRange>, List<Long>> {
        val sections = data.replace("\r\n", "\n").split("\n\n")

        val ranges = sections[0].lines()
            .filter { it.isNotBlank() }
            .map { line ->
                val (start, end) = line.trim().split("-").map { it.toLong() }
                start..end
            }
        val ingredients = sections[1].lines()
            .filter { line -> line.isNotBlank() }
            .map { line -> line.toLong() }

        return ranges to ingredients
    }

    private fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
        val sortedRanges = ranges.sortedBy { range -> range.first }
        val merged = mutableListOf<LongRange>()
        var current = sortedRanges.first()

        for (range in sortedRanges.drop(1)) { // Skip first element
            if (range.first <= current.last + 1) {
                // Overlapping or adjacent ranges, merge them
                current = current.first..maxOf(current.last, range.last)
            } else {
                // No overlap, add current and start new range
                merged.add(current)
                current = range
            }
        }
        merged.add(current)

        return merged
    }
}