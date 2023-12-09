package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day04/training.txt")
    val data = readFileDirectlyAsText("/year2022/day04/data.txt")

    val lines = data.split("\n")

    val day = Day04(2022, 4, "Camp Cleanup")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

private class Day04(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(lines: List<String>): Int {
        val sectionAssignmentPairs = buildSectionAssignmentPairs(lines)

        return sectionAssignmentPairs.count { pair ->
            pair.first.contains(pair.second) || pair.second.contains(pair.first)
        }
    }

    fun partTwo(lines: List<String>): Int {
        val sectionAssignmentPairs = buildSectionAssignmentPairs(lines)

        return sectionAssignmentPairs.count { pair ->
            pair.first.overlap(pair.second)
        }
    }

    private fun buildSectionAssignmentPairs(lines: List<String>): List<Pair<IntRange, IntRange>> {
        return lines.flatMap { line ->
            line.split(",")
                .map {
                    val range = it.split("-")
                    IntRange(range.first().toInt(), range.last().toInt())
                }.chunked(2) { it.first() to it.last() }
        }
    }

    private fun IntRange.contains(range: IntRange) = this.first <= range.first && this.last >= range.last

    private fun IntRange.overlap(range: IntRange) = range.first in this || this.first in range
}
