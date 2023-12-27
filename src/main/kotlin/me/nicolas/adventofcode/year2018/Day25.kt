package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime


// https://adventofcode.com/2018/day/25
fun main() {
    val data = readFileDirectlyAsText("/year2018/day25/data.txt")
    val day = Day25(2018, 25, "Four-Dimensional Adventure")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day25(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        return 42
    }

    fun partTwo(data: String): Int {
        return 42
    }
}