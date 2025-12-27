package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 2: Corruption Checksum ---
// https://adventofcode.com/2017/day/2
fun main() {
    val data = readFileDirectlyAsText("/year2017/day02/data.txt")
    val day = Day02()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day02(year: Int = 2017, day: Int = 2, title: String = "Corruption Checksum") : AdventOfCodeDay(year, day, title) {
    // Helper to parse a single line into integers (handles spaces/tabs)
    private fun parseLineToInts(line: String): List<Int> =
        line.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.map { it.toInt() }

    /*
     * partOne:
     * - For each non-empty input line, parse the integers.
     * - Compute the difference between the maximum and minimum value in the row.
     * - Sum these differences across all rows and return the total checksum.
     */
    fun partOne(data: String): Int {
        return data.lines()
            .map { line -> line.trim() }
            .filter { line -> line.isNotEmpty() }
            .sumOf { line ->
                val nums = parseLineToInts(line)
                if (nums.isEmpty()) {
                    0
                } else {
                    (nums.maxOrNull()!! - nums.minOrNull()!!)
                }
            }
    }

    /*
     * partTwo:
     * - For each non-empty input line, parse the integers.
     * - Find the only pair of numbers in the row where one evenly divides the other (a % b == 0).
     *   We sort the row in descending order so larger candidates are tested first; this ensures
     *   that when a % b == 0 we use the quotient a/b. We stop at the first valid divisible pair
     *   per row.
     * - Sum the quotients across all rows and return the total.
     *
     * Notes and correctness:
     * - The puzzle guarantees there is exactly one divisible pair per row for valid inputs,
     *   so breaking early is safe. For robustness we return 0 for rows with fewer than 2 numbers
     *   or if no divisible pair is found.
     *
     * Complexity: O(R * C log C + R * C^2) due to sorting each row and pairwise checks; with
     * typical small row sizes this is perfectly fine.
     */
    fun partTwo(data: String): Int {
        return data.lines()
            .map { line -> line.trim() }
            .filter { line -> line.isNotEmpty() }
            .sumOf { line ->
                val nums = parseLineToInts(line)
                if (nums.size < 2) {
                    return@sumOf 0
                }

                // Sort descending so we only need to test j > i and can break early
                val sorted = nums.sortedDescending()
                var quotient = 0
                for (i in sorted.indices) {
                    for (j in i + 1 until sorted.size) {
                        val a = sorted[i]
                        val b = sorted[j]
                        if (a % b == 0) {
                            quotient = a / b
                            break
                        }
                    }
                    if (quotient != 0) {
                        break
                    }
                }
                quotient
            }
    }
}