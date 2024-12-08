package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 10: Elves Look, Elves Say ---
// https://adventofcode.com/2015/day/10
fun main() {
    //val data = readFileDirectlyAsText("/year2015/day10/data.txt")
    val day = Day10(2015, 10)
    prettyPrintPartOne { day.partOne("1321131112") }
    prettyPrintPartTwo { day.partTwo("1321131112") }
}

class Day10(year: Int, day: Int, title: String = "Elves Look, Elves Say") : AdventOfCodeDay(year, day, title) {

    private fun lookAndSay(input: String): String {
        val result = StringBuilder()
        var count = 1

        for (i in 1 until input.length) {
            if (input[i] == input[i - 1]) {
                count++
            } else {
                appendCountAndChar(result, count, input[i - 1])
                count = 1
            }
        }

        // Append the last run of digits
        appendCountAndChar(result, count, input[input.length - 1])

        return result.toString()
    }

    private fun appendCountAndChar(sb: StringBuilder, count: Int, char: Char) {
        sb.append(count)
        sb.append(char)
    }

    fun partOne(data: String): Int = calculateSequenceLength(data, 40)

    fun partTwo(data: String): Int = calculateSequenceLength(data, 50)

    private fun calculateSequenceLength(data: String, iterations: Int): Int {
        var sequence = data

        repeat(iterations) {
            sequence = lookAndSay(sequence)
        }

        return sequence.length
    }
}
