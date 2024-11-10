package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2024/day01/data.txt")
    val lines = data.split("\n")
    val day = Day01(2024, 1,"")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day01(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(lines: List<String>): Int {
        return 0
    }

    fun partTwo(lines: List<String>): Int {
        return 0
    }
}

