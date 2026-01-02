package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 10: Knot Hash ---
// https://adventofcode.com/2017/day/10
fun main() {
    val data = readFileDirectlyAsText("/year2017/day10/data.txt")
    val day = Day10()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day10(year: Int = 2017, day: Int = 10, title: String = "Knot Hash") : AdventOfCodeDay(year, day, title) {

    /**
     * Part One
     *
     * Implements a single round of the "knot" algorithm:
     * - Start with a list of integers from 0 until `size - 1`.
     * - Parse the input `data` as a comma-separated sequence of lengths.
     * - For each length L (in order), reverse the sublist of L elements starting
     *   at the current position (the list is treated as circular).
     * - After reversing, move the current position forward by (L + skip)
     *   where `skip` is the index of the current length (0-based for a single pass).
     * - The function returns the product of the first two numbers in the resulting list.
     */
    fun partOne(data: String, size: Int = 256): Int {
        // Parse comma-separated lengths; ignore empty tokens (robustness for empty input)
        val lengths = data.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.toInt() }

        if (lengths.isEmpty()) return 0

        val list = MutableList(size) { it }
        var current = 0

        for ((skip, len) in lengths.withIndex()) {
            // reverse the sublist of length len starting at current, circularly
            for (i in 0 until len / 2) {
                val a = (current + i) % size
                val b = (current + len - 1 - i) % size
                val tmp = list[a]
                list[a] = list[b]
                list[b] = tmp
            }
            current = (current + len + skip) % size
        }

        return list[0] * list[1]
    }

    /**
     * Part Two
     *
     * Full Knot Hash algorithm:
     * - Treat input as ASCII bytes (ignore leading/trailing whitespace).
     * - Convert to sequence of byte values, append the standard suffix
     *   [17, 31, 73, 47, 23].
     * - Run 64 rounds of the single-round knot operation, preserving current
     *   position and skip size between rounds.
     * - Reduce the resulting 256-number sparse hash into a 16-number dense
     *   hash by XOR'ing each consecutive block of 16 numbers.
     * - Return the hexadecimal representation (32 lowercase hex digits,
     *   two hex digits per dense byte).
     */
    fun partTwo(data: String): String {
        // Prepare lengths from ASCII codes of the trimmed input + suffix
        val trimmed = data.trim()
        val inputBytes = trimmed.toByteArray(Charsets.US_ASCII).map { it.toInt() and 0xFF }
        val suffix = listOf(17, 31, 73, 47, 23)
        val lengths = inputBytes + suffix

        val size = 256
        val list = MutableList(size) { it }
        var current = 0
        var skip = 0

        // 64 rounds
        repeat(64) {
            for (len in lengths) {
                // reverse sublist of length len starting at current (circular)
                for (i in 0 until len / 2) {
                    val a = (current + i) % size
                    val b = (current + len - 1 - i) % size
                    val tmp = list[a]
                    list[a] = list[b]
                    list[b] = tmp
                }
                current = (current + len + skip) % size
                skip++
            }
        }

        // Build dense hash by XOR'ing blocks of 16
        val dense = (0 until 16).map { block ->
            val start = block * 16
            list.subList(start, start + 16).reduce { acc, v -> acc xor v }
        }

        // Convert to hex string, two lowercase digits per value
        return dense.joinToString(separator = "") { "%02x".format(it) }
    }
}