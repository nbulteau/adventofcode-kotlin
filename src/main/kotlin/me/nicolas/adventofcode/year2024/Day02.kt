package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 2: ---
// https://adventofcode.com/2024/day/2
fun main() {
    val data = readFileDirectlyAsText("/year2024/day02/data.txt")
    val day = Day02(2024, 2, "")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day02(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {

        val lines = data.split("\n")

        val results = lines.map { line ->
            val report = line.split(" ").map { string -> string.trim().toInt() }
            report.isReportValid()
        }

        return results.count { bool -> bool }
    }

    fun partTwo(data: String): Int {

        val lines = data.split("\n")

        val results = lines.map { line ->
            val report = line.split(" ").map { string -> string.trim().toInt() }

            val allPermutations = report.buildAllPermutations()

            allPermutations.map { report ->
                report.isReportValid()
            }.any { bool -> bool }
        }

        return results.count { it }
    }

    private fun List<Int>.buildAllPermutations(): List<List<Int>> =
        this.indices.map { index ->
            this.toMutableList().also { list -> list.removeAt(index) }
        }

    private fun List<Int>.isReportValid(): Boolean = if (this[0] > this[1]) {
        // Decrease
        this.windowed(2, 1).all { ints ->
            val delta = ints.first() - ints.last()
            delta > 0 && delta <= 3
        }
    } else if (this[0] < this[1]) {
        // Increase
        this.windowed(2, 1).all { ints ->
            val delta = ints.last() - ints.first()
            delta > 0 && delta <= 3
        }
    } else {
        false
    }
}