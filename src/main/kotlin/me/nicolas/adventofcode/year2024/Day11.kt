package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 11: Plutonian Pebbles ---
// https://adventofcode.com/2024/day/11
fun main() {
    val data = readFileDirectlyAsText("/year2024/day11/data.txt")
    val day = Day11(2024, 11)
    prettyPrintPartOne { day.partOne(data, 25) }
    prettyPrintPartTwo { day.partTwo(data, 75) }
}

class Day11(year: Int, day: Int, title: String = "Plutonian Pebbles") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String, steps: Int) = solve(data, steps)

    fun partTwo(data: String, steps: Int) = solve(data, steps)

    private fun solve(data: String, steps: Int): Long {
        val initialStones = data.split(" ").associate { it.toLong() to 1L }

        return (1..steps)
            .fold(initialStones) { stones, _ -> blink(stones) }
            .map { (_, count) -> count }.sum()
    }

    private fun blink(stones: Map<Long, Long>): Map<Long, Long> {
        val nextStones = mutableMapOf<Long, Long>()

        stones.forEach { (stone, count) ->
            if (stone == 0L) {
                nextStones[1] = (nextStones[1] ?: 0) + count
            } else if (stone.toString().length % 2 == 0) {
                val midIndex = stone.toString().length / 2
                val leftStone = stone.toString().take(midIndex).toLong()
                // val rightStone = stone.toString().substring(midIndex).toLong()
                val rightStone = stone.toString().takeLast(midIndex).toLong()

                nextStones[leftStone] = (nextStones[leftStone] ?: 0) + count
                nextStones[rightStone] = (nextStones[rightStone] ?: 0) + count
            } else {
                val newStone = stone * 2024
                nextStones[newStone] = (nextStones[newStone] ?: 0) + count
            }
        }

        return nextStones
    }
}