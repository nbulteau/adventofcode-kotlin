package me.nicolas.adventofcode.year2020

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 15: Rambunctious Recitation ---
// https://adventofcode.com/2020/day/15
@ExperimentalTime
fun main() {

    println("--- Day 15: Rambunctious Recitation ---")
    println()

    val numbers = listOf(14, 8, 16, 0, 1, 17)

    // Part One
    Day15().partOne(numbers, 2020)

    // Part Two
    val duration = measureTime { Day15().partTwo(numbers, 30000000) }
    println("Part two duration : $duration")
}

class Day15 {

    fun partOne(numbers: List<Int>, lastTurn: Int) {

        val last = process(numbers, lastTurn)

        println("Part one = $last")
    }

    fun partTwo(numbers: List<Int>, lastTurn: Int) {

        val last = process(numbers, lastTurn)

        println("Part two = $last")
    }

    private fun process(numbers: List<Int>, lastTurn: Int): Int {

        val spoken = numbers.toMutableList()
        // List of Pair
        val memory = numbers.mapIndexed { index, it -> it to Pair<Int?, Int?>(index + 1, null) }.toMap().toMutableMap()

        for (turn in numbers.size + 1..lastTurn) {
            val last = spoken.last()

            // get current
            val current: Int = when {
                memory[last]!!.first == null -> 0
                memory[last]!!.second == null -> 0
                else -> (memory[last]!!.second!! - memory[last]!!.first!!)

            }

            // update memory
            memory[current] = when {
                memory[current] == null || memory[current]!!.first == null -> Pair(turn, null)
                memory[current]!!.second == null -> Pair(memory[current]!!.first, turn)
                else -> Pair(memory[current]!!.second, turn)
            }

            spoken.add(current)
        }
        return spoken.last()
    }
}