package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.floor


// --- Day 1: The Tyranny of the Rocket Equation ---
// https://adventofcode.com/2019/day/1


fun main() {
    val data = readFileDirectlyAsText("/year2019/day01/data.txt")
    val day = Day01(2019, 1, "The Tyranny of the Rocket Equation", data)
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo() }
}

class Day01(year: Int, day: Int, title: String, data: String) : AdventOfCodeDay(year, day, title) {

    private val masses = data.lines().map { string -> string.toInt() }

     fun partOne(): Int {
        return masses.sumOf { floor(it.div(3.0)) - 2 }.toInt()
     }

     fun partTwo(): Int {
        return masses.sumOf { recurse(it) }
    }

    private fun recurse(input: Int): Int {
        val fuel = (floor(input.div(3.0)) - 2).toInt()
        if (fuel <= 0) {
            return 0
        }
        return fuel + recurse((fuel))
    }
}
