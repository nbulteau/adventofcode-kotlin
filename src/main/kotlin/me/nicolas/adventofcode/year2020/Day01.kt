package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 01: Report Repair ---
// https://adventofcode.com/2020/day/1
fun main() {
    val data = readFileDirectlyAsText("/year2020/day01/data.txt")
    val day = Day01()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day01(year: Int = 2020, day: Int = 1, title: String = "Report Repair") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val numbers = data.split("\n").filter { it.isNotEmpty() }.map { it.toInt() }
        for (i in numbers.indices) {
            for (j in i + 1 until numbers.size) {
                if (numbers[i] + numbers[j] == 2020) {
                    return numbers[i] * numbers[j]
                }
            }
        }
        return 0
    }

    fun partTwo(data: String): Int {
        val numbers = data.split("\n").filter { it.isNotEmpty() }.map { it.toInt() }
        for (i in numbers.indices) {
            for (j in i + 1 until numbers.size) {
                for (k in j + 1 until numbers.size) {
                    if (numbers[i] + numbers[j] + numbers[k] == 2020) {
                        return numbers[i] * numbers[j] * numbers[k]
                    }
                }
            }
        }
        return 0
    }
}
