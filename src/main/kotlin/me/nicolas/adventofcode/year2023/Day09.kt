package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day09/data.txt")
    val day = Day09(2023, 9, "Mirage Maintenance")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day09(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val lists = extractLists(data)
        return lists.sumOf { list -> nextValue(list) }
    }

    fun partTwo(data: String): Int {
        val lists = extractLists(data)
        // Reverse each list before calculating the next ("previous") value
        return lists.sumOf { list -> nextValue(list.asReversed()) }
    }

    // Iterate is fine, but recursion is divine :)
    private fun nextValue(values: Iterable<Int>): Int {
        return if (values.all { value -> value == 0 }) {
            0
        } else {
            // lambda function inside windowed is used to calculate the differences between consecutive elements
            val differences = values.windowed(2) { it.last() - it.first() }
            values.last() + nextValue(differences)
        }
    }

    private fun extractLists(data: String): List<List<Int>> {
        return data.lines().map { line ->
            line.split(' ').map { it.toInt() }
        }
    }
}