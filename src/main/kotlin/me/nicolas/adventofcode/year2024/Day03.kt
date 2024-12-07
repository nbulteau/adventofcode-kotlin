package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 3: Mull It Over ---
// https://adventofcode.com/2024/day/3
fun main() {
    val data = readFileDirectlyAsText("/year2024/day03/data.txt")
    val day = Day03(2024, 3, "Mull It Over")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day03(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private val regEx = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

    fun partOne(data: String): Int {

        return processMulExpressions(data)
    }

    fun partTwo(data: String): Int {

        val doList = data.split("do()")

        return doList.sumOf { part ->
            val dontList = part.split("don't()")
            processMulExpressions(dontList[0])
        }
    }

    private fun processMulExpressions(data: String): Int {
        val matches = regEx.findAll(data)

        return matches.sumOf{ result ->
            val (_, left, right) = result.groupValues
            left.toInt() * right.toInt()
        }
    }
}