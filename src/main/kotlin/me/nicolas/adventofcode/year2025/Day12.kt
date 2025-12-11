package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 12: ---
// https://adventofcode.com/2025/day/12
fun main() {
    val data = readFileDirectlyAsText("/year2025/day12/data.txt")
    val day = Day12(2025, 12)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day12(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        return 0
    }

    fun partTwo(data: String): Int {
        return 0
    }
}