package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 25: Let It Snow ---
// https://adventofcode.com/2015/day/25
fun main() {
    val data = readFileDirectlyAsText("/year2015/day25/data.txt")
    val day = Day25(2015, 25)
    prettyPrintPartOne { day.partOne(data) }
}

class Day25(year: Int, day: Int, title: String = "Let It Snow") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        val row = data.substringAfter("row ").substringBefore(',').toInt()
        val column = data.substringAfter("column ").substringBefore('.').toInt()

        val position = getPosition(row, column)

        return generateCode(position)
    }

    // Calculates the position in the sequence for the given row and column.
    private fun getPosition(row: Int, column: Int): Int {
        val diagonal = row + column - 1
        val start = (diagonal * (diagonal - 1)) / 2

        return start + column
    }

    // Generates the code at the given position using the provided algorithm.
    private fun generateCode(position: Int): Long {
        var code = 20151125L
        repeat(position - 1) {
            code = (code * 252533) % 33554393
        }

        return code
    }
}