package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// --- Day 2: I Was Told There Would Be No Math ---
// https://adventofcode.com/2015/day/2
fun main() {
    val data = readFileDirectlyAsText("/year2015/day02/data.txt")
    val day = Day02(2015, 2, "I Was Told There Would Be No Math")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day02(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(dimensions: String): Int {
        return dimensions.split("\n").sumOf { line ->
            val (l, w, h) = extractLengthWidthHeight(line)
            val lw = l * w
            val wh = w * h
            val hl = h * l
            2 * lw + 2 * wh + 2 * hl + minOf(lw, wh, hl)
        }
    }

    fun partTwo(dimensions: String): Int {
        return dimensions.split("\n").sumOf { line ->
            val (l, w, h) = extractLengthWidthHeight(line)
            val smallestPerimeter = listOf(2 * (l + w), 2 * (w + h), 2 * (h + l)).minOrNull()!!
            val volume = l * w * h
            smallestPerimeter + volume
        }
    }

    private fun extractLengthWidthHeight(line: String): Triple<Int, Int, Int> {
        val (l, w, h) = line.trim().split("x").map { it.toInt() }

        return Triple(l, w, h)
    }
}
