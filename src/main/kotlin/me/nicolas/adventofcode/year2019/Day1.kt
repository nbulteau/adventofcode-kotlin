package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.math.floor


// --- Day 1: The Tyranny of the Rocket Equation ---
// https://adventofcode.com/2019/day/1
fun main(args: Array<String>) {

    println("--- Day 1: The Tyranny of the Rocket Equation ---")
    println()

    val training = readFileDirectlyAsText("/year2019/day1/training.txt")
    val data = readFileDirectlyAsText("/year2019/day1/data.txt")

    val masses = data.split("\n").map { string -> string.toInt() }

    // Part One
    partOne(masses)

    // Part Two
    partTwo(masses)
}

private fun partOne(numbers: List<Int>) {
    val result = numbers.sumOf { floor(it.div(3.0)) - 2 }.toInt()

    println("Part one $result")
}

private fun partTwo(numbers: List<Int>) {
    val result = numbers.sumOf { recurse(it) }

    println("Part two $result")
}

private fun recurse(input: Int): Int {
    val fuel = (floor(input.div(3.0)) - 2).toInt()
    if (fuel <= 0) {
        return 0
    }
    return fuel + recurse((fuel))
}