package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// --- Day 1: Historian Hysteria ---
// https://adventofcode.com/2024/day/1
fun main() {
    val data = readFileDirectlyAsText("/year2024/day01/data.txt")
    val day = Day01(2024, 1, "Historian Hysteria")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day01(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {

        val (left, right) = buildLists(data)

        val sortedLeft = left.sorted()
        val sortedRight = right.sorted()

        return sortedLeft.zip(sortedRight).sumOf { (first, second) ->
            abs(second - first)
        }
    }

    fun partTwo(data: String): Int {

        val (left, right) = buildLists(data)

        return left.sumOf { leftNumber ->
            leftNumber * right.count { rightNumber ->
                rightNumber == leftNumber
            }
        }
    }

    private fun buildLists(data: String): Pair<List<Int>, List<Int>> {
        val lines = data.lines()

        return lines.map { line ->
            line.trim().split("   ").map { it.toInt() }.let { it.first() to it.last() }
        }.unzip()
    }
}

