package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 2: Gift Shop ---
// https://adventofcode.com/2025/day/2
fun main() {
    val data = readFileDirectlyAsText("/year2025/day02/data.txt")
    val day = Day02(2025, 2)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day02(year: Int, day: Int, title: String = "Gift Shop") : AdventOfCodeDay(year, day, title) {

    /**
     * Find all invalid IDs that are formed by a sequence repeated exactly twice
     * For example: 1212 (12 repeated 2 times), 99 (9 repeated 2 times)
     * Sum all these invalid IDs across all ranges
     */
    fun partOne(data: String): Long {
        return parseRanges(data)
            .flatMap { (start, end) -> findInvalidIds(start, end) }
            .sum()
    }

    /**
     * Find all invalid IDs that are formed by a sequence repeated two or more times
     * For example: 1212 (12 repeated 2 times), 9999 (9 repeated 4 times), 123123123 (123 repeated 3 times)
     * Sum all these invalid IDs across all ranges
     */
    fun partTwo(data: String): Long {
        return parseRanges(data)
            .flatMap { (start, end) -> findInvalidIds(start, end, true) }
            .sum()
    }

    private fun parseRanges(input: String): List<Pair<Long, Long>> {
        return input.trim().split(",").map { range ->
            val (start, end) = range
                .trim()
                .split("-")
                .map { it.toLong() }
            start to end
        }
    }

    private fun findInvalidIds(start: Long, end: Long, partTwo: Boolean = false): List<Long> {
        val validator = if (partTwo) {
            ::isInvalidIdPartTwo
        } else {
            ::isInvalidIdPartOne
        }

        return (start..end).filter(validator)
    }

    private fun isInvalidIdPartOne(id: Long): Boolean {
        val idStr = id.toString()
        val length = idStr.length

        if (length % 2 != 0) return false

        val halfLength = length / 2
        val firstHalf = idStr.take(halfLength)
        val secondHalf = idStr.substring(halfLength)

        if (firstHalf.startsWith("0")) return false

        return firstHalf == secondHalf
    }

    private fun isInvalidIdPartTwo(id: Long): Boolean {
        val idStr = id.toString()
        val length = idStr.length

        for (patternLength in 1..length / 2) {
            if (length % patternLength != 0) {
                continue
            }

            val pattern = idStr.take(patternLength)
            if (pattern.startsWith("0")) {
                continue
            }

            val repetitions = length / patternLength
            if (repetitions >= 2 && pattern.repeat(repetitions) == idStr) {
                return true
            }
        }

        return false
    }
}