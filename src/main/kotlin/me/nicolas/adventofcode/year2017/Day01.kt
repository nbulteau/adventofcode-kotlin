package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 1: Inverse Captcha ---
// https://adventofcode.com/2017/day/1
fun main() {
    val data = readFileDirectlyAsText("/year2017/day01/data.txt")
    val day = Day01()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

/**
 * Day01 solver for Advent of Code 2017 Day 1 - Inverse Captcha.
 *
 * The two parts operate on a circular sequence of digits. The implementations
 * below keep the code simple and imperative for clarity and performance.
 */
class Day01(year: Int = 2017, day: Int = 1, title: String = "Inverse Captcha") : AdventOfCodeDay(year, day, title) {

    /**
     * Part one: sum all digits that match the next digit in the list (circular).
     *
     * Steps:
     * 1. Filter and convert the input string to a list of digits (0-9).
     * 2. If the list is empty, return 0 (no digits to compare).
     * 3. Iterate once over the list and compare each digit with the next one.
     *    Use modulo arithmetic for the circular comparison: nextIndex = (i + 1) % n.
     * 4. Accumulate matched digits into sum and return it.
     */
    fun partOne(data: String): Int {
        // Convert to digits and ignore any non-digit characters (robust input parsing).
        val digits = data.trim().filter { char -> char.isDigit() }.map { char -> char - '0' }

        var sum = 0
        val n = digits.size
        // Loop once: compare current digit to the next (wrap-around using modulo).
        for (i in 0 until n) {
            val next = (i + 1) % n
            if (digits[i] == digits[next]){
                sum += digits[i]
            }
        }

        return sum
    }

    /**
     * Part two: sum all digits that match the digit halfway around the circular list.
     *
     * Steps:
     * 1. Convert the input into a list of digits like in part one.
     * 2. The comparison index is (i + n/2) % n. This assumes n is even for "halfway"
     *    to be well-defined; if n is odd the modulo still works but the original
     *    puzzle input uses even lengths for this part.
     * 3. Iterate once and accumulate matches.
     */
    fun partTwo(data: String): Int {
        // Reuse the same parsing logic to get digits array.
        val digits = data.trim().filter { char -> char.isDigit() }.map { char -> char - '0' }

        val n = digits.size
        val step = n / 2 // halfway around the circular list
        var sum = 0
        // Compare each digit with the digit 'step' positions ahead (wrap-around).
        for (i in 0 until n) {
            val j = (i + step) % n
            if (digits[i] == digits[j]) {
                sum += digits[i]
            }
        }

        return sum
    }
}