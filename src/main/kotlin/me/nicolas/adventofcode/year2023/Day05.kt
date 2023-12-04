package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day05/data.txt")
    val lines = data.split("\n")
    val day = Day05("--- Day 5:  ---", "https://adventofcode.com/2023/day/5")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day05(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {
    fun partOne(lines: List<String>): Int {
        return 42
    }

    fun partTwo(lines: List<String>): Int {
        return 42
    }
}

