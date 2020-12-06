package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.math.round

// --- Day 5: Binary Boarding ---
// https://adventofcode.com/2020/day/5
fun main() {

    val training = readFileDirectlyAsText("/year2020/day5/training.txt")
    val data = readFileDirectlyAsText("/year2020/day5/data.txt")

    val seats = data.split("\n")

    // Part One
    partOne(seats)

    // Part Two
    partTwo(seats)
}

private fun partOne(seats: List<String>) {

    println("highest seat ID : ${computeAllSeats(seats).maxOrNull()}")
}


private fun partTwo(seats: List<String>) {

    val allSeats = computeAllSeats(seats)
    val completelyFullFlight = allSeats.minOf { it }.rangeTo(allSeats.maxOf { it })

    println("my seat ID : ${completelyFullFlight.filter { it !in allSeats }}")
}

private fun computeAllSeats(seats: List<String>): List<Int> {

    return seats.map { seat ->
        val row = computeOneSeat(seat.subSequence(0, 7), 127)
        val column = computeOneSeat(seat.subSequence(7, 10), 7)
        row * 8 + column
    }
}

private fun computeOneSeat(chars: CharSequence, upperBound: Int): Int {

    var lower = 0
    var upper = upperBound
    chars.forEach { char ->
        when (char) {
            'L', 'F' -> upper = round(upper - (upper - lower).div(2.0) - 0.5).toInt()
            'R', 'B' -> lower = round(lower + (upper - lower).div(2.0) + 0.5).toInt()
        }
    }
    return lower
}


