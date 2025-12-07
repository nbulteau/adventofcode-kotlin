package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// https://adventofcode.com/2021/day/2
fun main() {
    val data = readFileDirectlyAsText("/year2021/day02/data.txt")

    val day = Day02(2021, 2)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day02(year: Int, day: Int, title: String = "Dive!") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val lines = data.split("\n")
        var horizontal = 0
        var depth = 0

        for (line in lines) {
            val instruction = line.split(" ")
            when (instruction[0]) {
                "forward" -> horizontal += instruction[1].toInt()
                "down" -> depth -= instruction[1].toInt()
                "up" -> depth += instruction[1].toInt()
            }
        }
        return abs(horizontal) * abs(depth)
    }

    fun partTwo(data: String): Int {
        val lines = data.split("\n")
        var horizontal = 0
        var depth = 0
        var aim = 0

        for (line in lines) {
            val instruction = line.split(" ")
            when (instruction[0]) {
                "forward" -> {
                    horizontal += instruction[1].toInt()
                    depth += (aim * instruction[1].toInt())
                }

                "down" -> aim += instruction[1].toInt()
                "up" -> aim -= instruction[1].toInt()
            }
        }
        return abs(horizontal) * abs(depth)
    }
}


