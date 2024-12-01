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
    val lines = data.split("\r\n")
    val day = Day01(2024, 1, "Historian Hysteria")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day01(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(lines: List<String>): Int {

        val (left, right) = buildLists(lines)

        val sortedLeft = left.sorted()
        val sortedRight = right.sorted()

        return sortedLeft.zip(sortedRight).sumOf { (first, second) ->
            abs(second - first)
        }
    }

    fun partTwo(lines: List<String>): Int {

        val (left, right) = buildLists(lines)

        return left.sumOf { leftNumber ->
            leftNumber * right.count { rightNumber ->
                rightNumber == leftNumber
            }
        }
    }

    private fun buildLists(lines: List<String>): Pair<List<Int>, List<Int>> {
        return lines.map { line ->
            line.split("   ").map { it.toInt() }.let { it.first() to it.last() }
        }.unzip()
    }
}

