package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 1: Secret Entrance ---
// https://adventofcode.com/2025/day/1
fun main() {
    val data = readFileDirectlyAsText("/year2025/day01/data.txt")
    val day = Day01(2025, 1, "Secret Entrance")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day01(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    private data class Rotation(val direction: Char, val distance: Long)

    private fun parseRotations(data: String): Sequence<Rotation> {
        return data.lineSequence()
            .map { line -> line.trim() }
            .filter { line -> line.isNotEmpty() }
            .map { line ->
                Rotation(
                    direction = line[0],
                    distance = line.substring(1).toLong()
                )
            }
    }

    /**
     * Part One: Count how many rotations end exactly at position 0.
     *
     * We have a circular dial with 100 positions (0 to 99).
     * Starting at 50, we follow rotation instructions and count each time we stop at 0.
     * L means rotate left (toward smaller numbers), R means rotate right (toward larger numbers).
     */
    fun partOne(data: String): Int {
        var position = 50
        var count = 0

        parseRotations(data).forEach { rotation ->
            position = when (rotation.direction) {
                'L' -> (position - rotation.distance.toInt()).mod(100)
                'R' -> (position + rotation.distance.toInt()).mod(100)
                else -> position
            }

            if (position == 0) {
                count++
            }
        }

        return count
    }

    /**
     * Part Two: Count every time the dial passes through 0 during any rotation.
     *
     * Now we count each click that lands on 0, not just the final position.
     * A rotation of 150 positions means 150 individual clicks.
     *
     * For a rotation of distance d:
     * - Every 100 clicks completes one full circle, passing through 0 once
     * - For the remaining clicks, we check if they cross position 0:
     *   - Going right: we cross 0 if we reach or pass position 100
     *   - Going left: we cross 0 if we're not already at 0 and would pass it
     *
     * Example: From position 50, rotating right 1000 times does 10 full circles,
     * so we pass through 0 exactly 10 times.
     */
    fun partTwo(data: String): Int {
        var position = 50
        var count = 0L

        parseRotations(data).forEach { rotation ->
            val fullRotations = rotation.distance / 100
            val remaining = (rotation.distance % 100).toInt()

            // Each full rotation passes 0 exactly once
            count += fullRotations

            when (rotation.direction) {
                'R' -> {
                    // Remaining right steps: check if we cross 0
                    if (remaining > 0 && position + remaining >= 100) {
                        count++
                    }
                    position = (position + remaining).mod(100)
                }

                'L' -> {
                    // Remaining left steps: check if we cross 0
                    if (remaining > 0 && position > 0 && position <= remaining) {
                        count++
                    }
                    position = (position - remaining).mod(100)
                }
            }
        }

        return count.toInt()
    }
}
