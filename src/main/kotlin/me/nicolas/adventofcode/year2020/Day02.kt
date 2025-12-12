package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 02: Password Philosophy ---
// https://adventofcode.com/2020/day/2
fun main() {
    val data = readFileDirectlyAsText("/year2020/day02/data.txt")
    val day = Day02()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day02(year: Int = 2020, day: Int = 2, title: String = "Password Philosophy") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val list = data.split("\n").filter { it.isNotEmpty() }
        return list.count { string ->
            val (policy, password) = string.split(": ")
            val (range, letter) = policy.split(" ")
            val (min, max) = range.split("-").map { it.toInt() }
            val count = password.count { it == letter.first() }
            count in min..max
        }
    }

    fun partTwo(data: String): Int {
        val list = data.split("\n").filter { it.isNotEmpty() }
        return list.count { string ->
            val (policy, password) = string.split(": ")
            val (positions, letter) = policy.split(" ")
            val (pos1, pos2) = positions.split("-").map { it.toInt() }
            (password[pos1 - 1] == letter.first()) xor (password[pos2 - 1] == letter.first())
        }
    }
}
