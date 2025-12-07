package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo


// --- Day 4: Secure Container ---
// https://adventofcode.com/2019/day/4
fun main() {

    val data = "264360-746325"
    val day = Day04(2019, 4)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day04(year: Int, day: Int, title: String = "Secure Container") : AdventOfCodeDay(year, day, title) {

    /**
     * Count valid passwords in the given range according to part one rules:
     * - digits never decrease from left to right
     * - at least one pair of adjacent digits are the same
     */
    fun partOne(data: String): Int {
        val (from, to) = data.split("-")
        val range = from.toInt()..to.toInt()

        return range.map { index -> index.toString() }
            .filter { isIncreaseOrStayTheSame(it) }
            .count { hasTwoAdjacentDigitsAreTheSame(it) }
    }

    /**
     * Count valid passwords in the given range according to part two rules:
     * - digits never decrease from left to right
     * - at least one group of exactly two equal adjacent digits (not part of a larger group)
     */
    fun partTwo(data: String): Int {
        val (from, to) = data.split("-")
        val range = from.toInt()..to.toInt()

        return range.map { index -> index.toString() }
            .filter { isIncreaseOrStayTheSame(it) }
            .count { hasAOnlyTwoAdjacentDigitsAreTheSame(it) }
    }

    fun isIncreaseOrStayTheSame(input: String): Boolean =
        input.zipWithNext().all { it.first <= it.second }

    fun hasTwoAdjacentDigitsAreTheSame(input: String): Boolean =
        input.zipWithNext().any { it.first == it.second }

    fun hasAOnlyTwoAdjacentDigitsAreTheSame(input: String): Boolean =
        input.groupBy { char -> char }.any { it.value.size == 2 }

}


