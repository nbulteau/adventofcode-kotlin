package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 15: Rambunctious Recitation ---
// https://adventofcode.com/2020/day/15
fun main() {
    val data = "14,8,16,0,1,17"
    val day = Day15()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day15(year: Int = 2020, day: Int = 15, title: String = "Rambunctious Recitation") :
    AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val numbers = data.split(",").map { it.toInt() }
        return process(numbers, 2020)
    }

    fun partTwo(data: String): Int {
        val numbers = data.split(",").map { it.toInt() }
        return process(numbers, 30000000)
    }

    private fun process(numbers: List<Int>, lastTurn: Int): Int {

        // Use an IntArray to store the last turn a number was spoken.
        // This avoids boxing and keeping the full spoken history in memory.
        // We allocate size lastTurn + 1 because numbers produced can be up to ~lastTurn.
        val size = lastTurn + 1
        val lastSeen = IntArray(size) // default 0 means "not seen"

        // Initialize lastSeen for all starting numbers except the last one.
        for (i in 0 until numbers.size - 1) {
            val n = numbers[i]
            if (n < size) lastSeen[n] = i + 1
        }

        var last = numbers.last()

        for (turn in numbers.size + 1..lastTurn) {
            val lastSeenAt = if (last < size) lastSeen[last] else 0
            val current = if (lastSeenAt == 0) 0 else (turn - 1 - lastSeenAt)

            if (last < size) lastSeen[last] = turn - 1

            last = current
        }

        return last
    }
}