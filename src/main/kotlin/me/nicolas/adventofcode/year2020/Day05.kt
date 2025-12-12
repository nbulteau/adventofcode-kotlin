package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 05: Binary Boarding ---
// https://adventofcode.com/2020/day/5
fun main() {
    val data = readFileDirectlyAsText("/year2020/day05/data.txt")
    val day = Day05(2020, 5, "Binary Boarding")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day05(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val seats = data.split("\n").filter { it.isNotEmpty() }
        return computeAllSeats(seats).maxOrNull() ?: 0
    }

    fun partTwo(data: String): Int {
        val seats = data.split("\n").filter { it.isNotEmpty() }
        val allSeats = computeAllSeats(seats)
        val completelyFullFlight = allSeats.minOrNull()!!.rangeTo(allSeats.maxOrNull()!!)
        return completelyFullFlight.first { it !in allSeats }
    }

    private fun computeAllSeats(seats: List<String>): List<Int> {
        return seats.map { seat ->
            val row = seat.substring(0, 7).replace('F', '0').replace('B', '1').toInt(2)
            val column = seat.substring(7, 10).replace('L', '0').replace('R', '1').toInt(2)
            row * 8 + column
        }
    }
}
