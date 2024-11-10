package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// --- Day 5: Doesn't He Have Intern-Elves For This? ---
// https://adventofcode.com/2015/day/5
fun main() {
    val data = readFileDirectlyAsText("/year2015/day05/data.txt")
    val day = Day05(2015, 5, "Perfectly Spherical Houses in a Vacuum")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day05(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data : String): Int {
        val strings = data.split("\n")
        val forbiddenStrings = listOf("ab", "cd", "pq", "xy")

        return strings.count { string ->
            val vowels = string.count { it in "aeiou" }
            val doubleLetter = string.zipWithNext().any { it.first == it.second }
            val forbiddenString = forbiddenStrings.any { string.contains(it) }

            vowels >= 3 && doubleLetter && !forbiddenString
        }
    }

    fun partTwo(data : String): Int {
        val strings = data.split("\n")
        return strings.count { string ->
            hasRepeatingPair(string) && hasRepeatingLetterWithOneBetween(string)
        }
    }

    private fun hasRepeatingPair(string: String): Boolean {
        for (i in 0 until string.length - 1) {
            val pair = string.substring(i, i + 2)
            if (string.indexOf(pair, i + 2) != -1) {
                return true
            }
        }
        return false
    }

    private fun hasRepeatingLetterWithOneBetween(string: String): Boolean {
        for (i in 0 until string.length - 2) {
            if (string[i] == string[i + 2]) {
                return true
            }
        }
        return false
    }
}
