package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 4: High-Entropy Passphrases ---
// https://adventofcode.com/2017/day/4
fun main() {
    val data = readFileDirectlyAsText("/year2017/day04/data.txt")
    val day = Day04()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day04(year: Int = 2017, day: Int = 4, title: String = "High-Entropy Passphrases") : AdventOfCodeDay(year, day, title) {

    /**
     * partOne finds the number of valid passphrases in the input data.
     * A valid passphrase is one where no word appears more than once.
     */
    fun partOne(data: String): Int {
        val lines = data.lines()
        val validPassphrases = lines.count { line ->
            val words = line.split(" ")
            words.size == words.toSet().size
        }

        return validPassphrases
    }

    /**
     * partTwo finds the number of valid passphrases in the input data.
     * A valid passphrase is one where no two words are anagrams of each other.
     */
    fun partTwo(data: String): Int {
        val lines = data.lines()
        val validPassphrases = lines.count { line ->
            val words = line.split(" ")
            val sortedWords = words.map { it.toCharArray().sorted().joinToString("") }
            sortedWords.size == sortedWords.toSet().size
        }
        return validPassphrases
    }
}