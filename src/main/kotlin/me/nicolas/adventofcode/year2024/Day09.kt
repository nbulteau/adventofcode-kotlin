package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 9: ---
// https://adventofcode.com/2024/day/9
fun main() {
    val data = readFileDirectlyAsText("/year2024/day09/data.txt")
    val day = Day09(2024, 9, "")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day09(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        return 0
    }

    fun partTwo(data: String): Int {
        return 0
    }
}