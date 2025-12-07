package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 16: Flawed Frequency Transmission ---
// https://adventofcode.com/2019/day/16
fun main() {
    val data = readFileDirectlyAsText("/year2019/day16/data.txt")
    val day = Day16(2019, 16)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day16(year: Int, day: Int, title: String = "Flawed Frequency Transmission") : AdventOfCodeDay(year, day, title) {
    /**
     * Applies the Flawed Frequency Transmission (FFT) algorithm for 100 phases.
     * In each phase, a new list is constructed based on the input list.
     * Each element in the new list is calculated by multiplying every value in the input list
     * by a value in a repeating pattern (0, 1, 0, -1) and summing the results.
     * The pattern is repeated a number of times equal to the position of the output element.
     * Only the ones digit of the sum is kept.
     * After 100 phases, the first eight digits of the final list are returned.
     */
    fun partOne(data: String): String {
        var input = data.map { it.toString().toInt() }
        val basePattern = listOf(0, 1, 0, -1)

        repeat(100) {
            val output = MutableList(input.size) { 0 }
            for (i in input.indices) {
                var sum = 0L
                for (j in input.indices) {
                    val patternValue = basePattern[((j + 1) / (i + 1)) % 4]
                    sum += input[j] * patternValue
                }
                output[i] = (sum.toString().last()).toString().toInt()
            }
            input = output
        }

        return input.take(8).joinToString("")
    }

    /**
     * Applies an optimized FFT algorithm for the second part of the puzzle.
     * The real signal is the input signal repeated 10,000 times.
     * The message offset is given by the first seven digits of the input signal.
     * Since the offset is large (more than half the signal length), the FFT calculation for any element
     * at an index `i` greater than `total_size / 2` simplifies. The pattern for such an element consists of
     * zeros for the first `i` elements, followed by ones until the end of the list.
     * This means the new value at index `i` is the sum of all values from `i` to the end of the list, modulo 10.
     *
     * The calculation can be optimized by computing the values from the end of the list backwards.
     * `output[i] = (input[i] + input[i+1] + ... + input[end]) % 10`
     * `output[i-1] = (input[i-1] + input[i] + ... + input[end]) % 10`
     * This leads to the recurrence relation: `output[i-1] = (input[i-1] + output[i]) % 10`.
     *
     * This function first determines the relevant part of the signal (from the offset to the end),
     * then applies this optimized calculation for 100 phases.
     * Finally, it returns the first eight digits of the resulting message.
     */
    fun partTwo(data: String): String {
        val offset = data.trim().take(7).toInt()
        val input = data.map { it.toString().toInt() }
        val realInputSize = input.size * 10000

        val relevantInput = IntArray(realInputSize - offset) { i ->
            input[(offset + i) % input.size]
        }

        repeat(100) {
            for (i in relevantInput.size - 2 downTo 0) {
                relevantInput[i] = (relevantInput[i] + relevantInput[i + 1]) % 10
            }
        }

        return relevantInput.take(8).joinToString("")
    }
}