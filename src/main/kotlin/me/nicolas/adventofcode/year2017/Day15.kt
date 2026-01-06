package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 15: Dueling Generators ---
// https://adventofcode.com/2017/day/15
fun main() {
    val data = readFileDirectlyAsText("/year2017/day15/data.txt")
    val day = Day15()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day15(year: Int = 2017, day: Int = 15, title: String = "Dueling Generators") : AdventOfCodeDay(year, day, title) {

    private val genA = 16807L
    private val genB = 48271L
    private val divisor = 2147483647L

    // mask for lowest 16 bits
    private val low16Mask = (1L shl 16) - 1L

    /**
     * part one
     * Generate 40 million pairs of values. Count the number of times
     * the lowest 16 bits of both values match.
     */
    fun partOne(data: String): Int {
        val (generatorA, generatorB) = parseInput(data)

        val seqA = generator(generatorA, genA)
        val seqB = generator(generatorB, genB)

        // zip the two sequences, take 40_000_000 pairs and count matches on low 16 bits
        return seqA.zip(seqB)
            .take(40_000_000)
            .count { (a, b) -> (a and low16Mask) == (b and low16Mask) }
    }

    /**
     * part two
     * Generate 5 million pairs of values. Generator A produces
     * values that are multiples of 4, and generator B produces
     * values that are multiples of 8. Count the number of times
     * the lowest 16 bits of both values match.
     */
    fun partTwo(data: String): Int {
        val (generatorA, generatorB) = parseInput(data)

        val seqA = generator(generatorA, genA, multipleOf = 4L)
        val seqB = generator(generatorB, genB, multipleOf = 8L)

        return seqA.zip(seqB)
            .take(5_000_000)
            .count { (a, b) -> (a and low16Mask) == (b and low16Mask) }
    }

    // Parse input and return pair of starting values
    private fun parseInput(data: String): Pair<Long, Long> {
        val values = data.lines().mapNotNull { line ->
            val parts = line.split(" ")
            parts.lastOrNull()?.toLongOrNull()
        }

        return Pair(values[0], values[1])
    }

    // Lazy generator sequence producing values after applying factor and modulus
    private fun generator(start: Long, factor: Long, multipleOf: Long = 1L) = sequence {
        var value = start
        while (true) {
            value = (value * factor) % divisor
            if (value % multipleOf == 0L) yield(value)
        }
    }

}
