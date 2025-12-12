package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.floor


// --- Day 1: The Tyranny of the Rocket Equation ---
// https://adventofcode.com/2019/day/1

fun main() {
    val data = readFileDirectlyAsText("/year2019/day01/data.txt")
    val day = Day01(2019, 1)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day01(year: Int, day: Int, title: String = "The Tyranny of the Rocket Equation") :
    AdventOfCodeDay(year, day, title) {

    /**
     * Part One
     * Goal:
     *  - Given a list of module masses (one integer per line), compute the total fuel
     *    required. Fuel for a single module is calculated as: floor(mass / 3) - 2.
     * Algorithm:
     *  - Parse the input into integers (one mass per line).
     *  - For each mass compute floor(mass / 3.0) - 2 and sum the results.
     */
    fun partOne(data: String): Int {
        val masses = data.lines().map { line -> line.toInt() }

        return masses.sumOf { floor(it.div(3.0)) - 2 }.toInt()
    }

    /**
     * Part Two
     * Goal:
     *  - Compute the total fuel requirement for each module including the fuel's mass.
     *    Each added fuel amount itself requires additional fuel, computed with the same
     *    formula, until the additional fuel is zero or negative.
     * Algorithm:
     *  - Parse input into integers (one mass per line).
     *  - For each mass, compute the recursive fuel requirement using `recurse`:
     *      - fuel = floor(mass / 3) - 2
     *      - if fuel <= 0 stop, otherwise add fuel + recurse(fuel)
     *  - Sum the recursive fuel amounts for all modules.
     */
    fun partTwo(data: String): Int {
        val masses = data.lines().map { line -> line.toInt() }

        return masses.sumOf { recurse(it) }
    }

    private fun recurse(input: Int): Int {
        val fuel = (floor(input.div(3.0)) - 2).toInt()
        if (fuel <= 0) {
            return 0
        }
        return fuel + recurse((fuel))
    }
}
