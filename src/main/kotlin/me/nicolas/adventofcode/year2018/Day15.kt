package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime


// https://adventofcode.com/2018/day/15
fun main() {
    val data = readFileDirectlyAsText("/year2018/day15/data.txt")
    val day = Day15(2018, 15, "Beverage Bandits")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day15(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        return 42
    }

    fun partTwo(data: String): Int {
        return 42
    }
}