package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// --- Day 11: Hex Ed ---
// https://adventofcode.com/2017/day/11
fun main() {
    val data = readFileDirectlyAsText("/year2017/day11/data.txt")
    val day = Day11()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day11(year: Int = 2017, day: Int = 11, title: String = "Hex Ed") : AdventOfCodeDay(year, day, title) {

    private data class HexCoord(val x: Int = 0, val y: Int = 0, val z: Int = 0) {

        fun move(direction: String): HexCoord {
            return when (direction) {
                "n" -> HexCoord(x, y + 1, z - 1)
                "s" -> HexCoord(x, y - 1, z + 1)
                "ne" -> HexCoord(x + 1, y, z - 1)
                "sw" -> HexCoord(x - 1, y, z + 1)
                "nw" -> HexCoord(x - 1, y + 1, z)
                "se" -> HexCoord(x + 1, y - 1, z)
                else -> this
            }
        }

        fun distance(): Int = maxOf(abs(x), abs(y), abs(z))
    }

    /**
     * partOne calculates the distance from the origin after following a series of hexagonal grid steps.
     * Uses HexCoord immutable helper.
     */
    fun partOne(data: String): Int {
        val trimmed = data.trim()
        if (trimmed.isEmpty()) return 0
        val steps = trimmed.split(',').map { it.trim() }.filter { it.isNotEmpty() }

        var pos = HexCoord()
        for (s in steps) {
            pos = pos.move(s)
        }

        return pos.distance()
    }

    /**
     * partTwo calculates the maximum distance from the origin reached at any point during the path.
     */
    fun partTwo(data: String): Int {
        val trimmed = data.trim()
        if (trimmed.isEmpty()) return 0
        val steps = trimmed.split(',').map { it.trim() }.filter { it.isNotEmpty() }

        var pos = HexCoord()
        var maxDistance = 0

        for (s in steps) {
            pos = pos.move(s)
            val dist = pos.distance()
            if (dist > maxDistance) maxDistance = dist
        }

        return maxDistance
    }
}