package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 25: Combo Breaker ---
// https://adventofcode.com/2020/day/25
fun main() {
    val data = "19241437\n17346587"
    val day = Day25(2020, 25, "Combo Breaker")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day25(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        val (cardPublicKey, doorPublicKey) = data.split("\n").map { it.toInt() }
        val cardLoopSize = findLoopSize(cardPublicKey)
        return transform(doorPublicKey, cardLoopSize)
    }

    fun partTwo(data: String): String {
        return "Merry Christmas!"
    }

    private fun findLoopSize(publicKey: Int): Int {
        var value = 1L
        var loopSize = 0
        while (value != publicKey.toLong()) {
            value = (value * 7) % 20201227
            loopSize++
        }
        return loopSize
    }

    private fun transform(subjectNumber: Int, loopSize: Int): Long {
        var value = 1L
        repeat(loopSize) {
            value = (value * subjectNumber) % 20201227
        }
        return value
    }
}