package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 1: Sonar Sweep ---
// https://adventofcode.com/2021/day/1
fun main() {

    val data = readFileDirectlyAsText("/year2021/day01/data.txt")

    val day = Day01(2021, 1)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day01(year: Int, day: Int, title: String = "Sonar Sweep") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val numbers = data.split("\n").map { string -> string.trim().toInt() }

        var increase = 0
        for (index in 0..numbers.size - 2) {
            if (numbers[index + 1] - numbers[index] > 0) {
                increase++
            }
        }

        return increase
    }

    fun partTwo(data: String): Int {
        val numbers = data.split("\n").map { string -> string.trim().toInt() }

        var increase = 0
        for (index in 0..numbers.size - 4) {
            val number1 = numbers[index] + numbers[index + 1] + numbers[index + 2]
            val number2 = numbers[index + 1] + numbers[index + 2] + numbers[index + 3]

            if (number2 > number1) {
                increase++
            }
        }

        return increase
    }
}
