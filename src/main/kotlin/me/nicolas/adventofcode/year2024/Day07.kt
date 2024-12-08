package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 7: Bridge Repair ---
// https://adventofcode.com/2024/day/7
fun main() {
    val data = readFileDirectlyAsText("/year2024/day07/data.txt")
    val day = Day07(2024, 7, "Bridge Repair")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        val operators: List<(Long, Long) -> Long> = listOf(Long::plus, Long::times)

        return solve(data, operators)
    }

    fun partTwo(data: String): Long {
        val operators: List<(Long, Long) -> Long> =
            listOf(Long::plus, Long::times, { first, second -> "$first$second".toLong() })

        return solve(data, operators)
    }

    private fun parseLine(line: String): Pair<Long, List<Long>> {
        val parts = line.split(": ")
        val testValue = parts[0].toLong()
        val numbers = parts[1].split(" ").map { item -> item.trim().toLong() }

        return testValue to numbers
    }

    private fun solve(data: String, operators: List<(Long, Long) -> Long>): Long {
        return data.split('\n')
            .map { parseLine(it) }
            .filter { (testValue, numbers) -> isSolvable(testValue, numbers, operators) }
            .sumOf { (testValue, _) -> testValue }
    }

    private fun isSolvable(testValue: Long, numbers: List<Long>, operators: List<(Long, Long) -> Long>): Boolean {
        fun recurse(currentValue: Long, index: Int): Boolean {
            if (index == numbers.size) {
                return currentValue == testValue
            }
            // Optimization: exit early if current value exceeds test value
            if (currentValue > testValue) {
                return false
            }
            return operators.any { op ->
                recurse(op(currentValue, numbers[index]), index + 1)
            }
        }
        return recurse(numbers.first(), 1)
    }
}