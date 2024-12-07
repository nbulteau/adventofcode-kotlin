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

        return data.split('\n')
            .map { parseLine(it) }
            .filter { (testValue, numbers) -> isSolvable(testValue, numbers, operators) }
            .sumOf { (testValue, _) -> testValue }
    }

    fun partTwo(data: String): Long {

        val operators: List<(Long, Long) -> Long> =
            listOf(Long::plus, Long::times, { first, second -> "$first$second".toLong() })

        return data.split('\n')
            .map { parseLine(it) }
            .filter { (testValue, numbers) -> isSolvable(testValue, numbers, operators) }
            .sumOf { (testValue, _) -> testValue }
    }

    private fun parseLine(line: String): Pair<Long, List<Long>> {
        val parts = line.split(": ")
        val testValue = parts[0].toLong()
        val numbers = parts[1].split(" ").map { item -> item.trim().toLong() }

        return testValue to numbers
    }

    private fun isSolvable(testValue: Long, numbers: List<Long>, operators: List<(Long, Long) -> Long>): Boolean {

        fun recurse(currentValue: Long, remainingNumbers: List<Long>): Boolean {
            if (remainingNumbers.isEmpty()) {
                return currentValue == testValue
            }

            // optimize: exit as soon as possible
            if (currentValue > testValue) {
                return false
            }

            return operators.any { op ->
                recurse(op(currentValue, remainingNumbers.first()), remainingNumbers.subList(1, remainingNumbers.size))
            }
        }

        return recurse(numbers.first(), numbers.subList(1, numbers.size))
    }
}