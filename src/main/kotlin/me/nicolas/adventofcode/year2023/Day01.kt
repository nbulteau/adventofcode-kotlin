package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day01/data.txt")
    val lines = data.split("\n")
    val day = Day01("--- Day 1: Trebuchet?! ---", "https://adventofcode.com/2023/day/1")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day01(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {
    fun partOne(lines: List<String>): Int {
        return lines.sumOf { line ->
            val firstDigit = line.first { it.isDigit() }.digitToInt()
            val lastDigit = line.last { it.isDigit() }.digitToInt()

            10 * firstDigit + lastDigit
        }
    }

    private data class Digit(val value: Int, val label: String)

    private val digits = listOf(
        Digit(0, "0"),
        Digit(1, "1"),
        Digit(2, "2"),
        Digit(3, "3"),
        Digit(4, "4"),
        Digit(5, "5"),
        Digit(6, "6"),
        Digit(7, "7"),
        Digit(8, "8"),
        Digit(9, "9"),
        Digit(0, "zero"),
        Digit(1, "one"),
        Digit(2, "two"),
        Digit(3, "three"),
        Digit(4, "four"),
        Digit(5, "five"),
        Digit(6, "six"),
        Digit(7, "seven"),
        Digit(8, "eight"),
        Digit(9, "nine"),
    )

    fun partTwo(lines: List<String>): Int {
        return lines.sumOf { line ->
            // 1. filter the digits that are in the line
            val digitsInTheLine = digits.filter { digit -> digit.label in line }
            // 2. find the min index of the first digit in the line
            val firstDigit = digitsInTheLine.minBy { digit -> line.indexOf(digit.label) }.value
            // 3. find the max index of the last digit in the line
            val lastDigit = digitsInTheLine.maxBy { digit -> line.lastIndexOf(digit.label) }.value

            10 * firstDigit + lastDigit
        }
    }
}

