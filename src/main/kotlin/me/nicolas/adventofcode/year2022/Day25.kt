package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2022/day25/data.txt")
    val day = Day25(2022, 25, "Full of Hot Air")
    prettyPrintPartOne { day.partOne(data) }
}

class Day25(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private val snafuValues = arrayOf(0, 1, 2, -2, -1)
    private val snafuDigits = "012=-".toCharArray()

    private fun Char.snafuToDigit(): Int = when (this) {
        '=' -> -2
        '-' -> -1
        else -> this.digitToInt()
    }

    private fun String.snafuToDecimal() = this.indices.fold(0L) { acc, i -> acc * 5 + this[i].snafuToDigit() }

    private fun Long.decimalToSNAFU(): String {
        var decimal = this
        var result = ""
        while (decimal > 0) {
            val index = (decimal % 5).toInt()
            result += snafuDigits[index]
            decimal = (decimal - snafuValues[index]) / 5
        }
        return result.reversed()
    }

    fun partOne(data: String): String {
        val numbers = data.split("\n")
            .sumOf { number ->
                number.trim().snafuToDecimal()
            }

        return numbers.decimalToSNAFU()
    }
}
 
