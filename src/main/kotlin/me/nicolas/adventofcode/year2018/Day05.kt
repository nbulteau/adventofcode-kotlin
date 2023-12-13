package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2018/day/5
fun main() {
    val data = readFileDirectlyAsText("/year2018/day05/data.txt")
    val day = Day05(2018, 5, "Alchemical Reduction")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

private class Day05(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        return 42
    }

    fun partTwo(data: String): Int {
        return 42
    }
}