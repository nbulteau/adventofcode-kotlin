package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 09: Encoding Error ---
// https://adventofcode.com/2020/day/9
fun main() {
    val data = readFileDirectlyAsText("/year2020/day09/data.txt")
    val day = Day09(2020, 9, "Encoding Error")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day09(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String, preamble: Int = 25): Long {
        val numbers = data.split("\n").filter { line -> line.isNotEmpty() }.map { line -> line.trim().toLong() }
        return findFirstInvalidNumber(numbers, preamble)
    }

    fun partTwo(data: String, preamble: Int = 25): Long {
        val numbers = data.split("\n").filter { line -> line.isNotEmpty() }.map { line -> line.trim().toLong() }
        val invalidNumber = findFirstInvalidNumber(numbers, preamble)
        return findEncryptionWeakness(numbers, invalidNumber)
    }

    private fun findFirstInvalidNumber(numbers: List<Long>, preamble: Int): Long {
        for (i in preamble until numbers.size) {
            val currentNumber = numbers[i]
            val previousNumbers = numbers.subList(i - preamble, i)
            if (!isValid(currentNumber, previousNumbers)) {
                return currentNumber
            }
        }
        throw IllegalStateException("No invalid number found")
    }

    private fun isValid(number: Long, previous: List<Long>): Boolean {
        for (i in previous.indices) {
            for (j in i + 1 until previous.size) {
                if (previous[i] + previous[j] == number) {
                    return true
                }
            }
        }
        return false
    }

    private fun findEncryptionWeakness(numbers: List<Long>, target: Long): Long {
        var start = 0
        var end = 0
        var sum = 0L
        while (end < numbers.size) {
            sum += numbers[end]
            while (sum > target) {
                sum -= numbers[start]
                start++
            }
            if (sum == target && end > start) {
                val range = numbers.subList(start, end + 1)
                return range.minOrNull()!! + range.maxOrNull()!!
            }
            end++
        }
        throw IllegalStateException("No encryption weakness found")
    }
}
