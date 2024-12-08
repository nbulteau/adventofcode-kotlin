package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 10: ---
// https://adventofcode.com/2015/day/10
fun main() {
    //val data = readFileDirectlyAsText("/year2015/day10/data.txt")
    val day = Day10(2015, 10)
    prettyPrintPartOne { day.partOne("1321131112") }
    prettyPrintPartTwo { day.partTwo("1321131112") }
}

class Day10(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    private fun lookAndSay(input: String): String {
        val result = StringBuilder()
        var count = 1

        for (i in 1 until input.length) {
            if (input[i] == input[i - 1]) {
                count++
            } else {
                result.append(count)
                result.append(input[i - 1])
                count = 1
            }
        }

        // Append the last run of digits
        result.append(count)
        result.append(input[input.length - 1])

        return result.toString()
    }


    fun partOne(data: String): Int {
        var sequence = data

        for (i in 0 until 40) {
            sequence = lookAndSay(sequence)
        }

        return sequence.length
    }

    fun partTwo(data: String): Int {
        var sequence = data

        for (i in 0 until 50) {
            sequence = lookAndSay(sequence)
        }

        return sequence.length
    }
}