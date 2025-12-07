package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// https://adventofcode.com/2021/day/7
fun main() {
    val data = readFileDirectlyAsText("/year2021/day07/data.txt")
    val day = Day07(2021, 7)
    val positions = data.split(",").map { it.toInt() }
    prettyPrintPartOne { day.partOne(positions) }
    prettyPrintPartTwo { day.partTwo(positions) }
}

class Day07(year: Int, day: Int, title: String = "The Treachery of Whales") : AdventOfCodeDay(year, day, title) {

    fun partOne(positions: List<Int>): Int = solve(positions) { position -> position }

    fun partTwo(positions: List<Int>): Int = solve(positions) { position -> (position * (position + 1) / 2) }


    fun solve(positions: List<Int>, computeFuelConsumption: (position: Int) -> Int): Int {

        val max = positions.maxOf { it }
        val fuelConsumptions = (0..max).associateWith { destination ->
            positions.sumOf { position -> computeFuelConsumption(abs(position - destination)) }
        }
        return fuelConsumptions.values.minOf { it }
    }
}
