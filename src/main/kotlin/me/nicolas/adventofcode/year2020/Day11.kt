package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 11: Seating System ---
// https://adventofcode.com/2020/day/11
fun main() {
    val data = readFileDirectlyAsText("/year2020/day11/data.txt")
    val day = Day11(2020, 11, "Seating System")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day11(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private data class Layout(val rows: List<String>) {
        fun processLayoutPartOne(): Layout {
            val height = this.rows.size
            val width = this.rows[0].length
            val newLayoutRows = mutableListOf<String>()

            for (y in 0 until height) {
                var newString = ""
                for (x in 0 until width) {
                    newString += when (this.rows[y][x]) {
                        '.' -> '.'
                        'L' -> if (this.lookAround(x, y) == 0) '#' else 'L'
                        '#' -> if (this.lookAround(x, y) >= 4) 'L' else '#'
                        else -> this.rows[y][x]
                    }
                }
                newLayoutRows += newString
            }
            return Layout(newLayoutRows)
        }

        fun processLayoutPartTwo(): Layout {
            val height = this.rows.size
            val width = this.rows[0].length
            val newLayoutRows = mutableListOf<String>()

            for (y in 0 until height) {
                var newString = ""
                for (x in 0 until width) {
                    newString += when (this.rows[y][x]) {
                        '.' -> '.'
                        'L' -> if (this.lookInDirection(x, y) == 0) '#' else 'L'
                        '#' -> if (this.lookInDirection(x, y) >= 5) 'L' else '#'
                        else -> this.rows[y][x]
                    }
                }
                newLayoutRows += newString
            }
            return Layout(newLayoutRows)
        }

        fun count(charToCount: Char) = this.rows.sumOf { string -> string.count { char -> char == charToCount } }

        private fun lookAround(x: Int, y: Int) = Direction.entries.toTypedArray()
            .sumOf { direction -> this.lookAround(x, y, direction) }

        private fun lookAround(x: Int, y: Int, direction: Direction): Int {
            val nextX = x + direction.dx
            val nextY = y + direction.dy
            return if (nextX in this.rows[0].indices && nextY in this.rows.indices && this.rows[nextY][nextX] == '#') 1 else 0
        }

        private fun lookInDirection(x: Int, y: Int) = Direction.entries.toTypedArray()
            .sumOf { direction -> this.lookInDirection(x, y, direction) }

        private fun lookInDirection(x: Int, y: Int, direction: Direction): Int {
            var nextX = x + direction.dx
            var nextY = y + direction.dy

            while (nextX in this.rows[0].indices && nextY in this.rows.indices) {
                if (this.rows[nextY][nextX] == 'L') return 0
                if (this.rows[nextY][nextX] == '#') return 1
                nextX += direction.dx
                nextY += direction.dy
            }
            return 0
        }
    }

    fun partOne(data: String): Int {
        val initialLayout = Layout(data.split("\n").filter { it.isNotEmpty() })
        var newLayout = initialLayout
        var previousLayout: Layout
        do {
            previousLayout = newLayout
            newLayout = previousLayout.processLayoutPartOne()
        } while (newLayout != previousLayout)
        return newLayout.count('#')
    }

    fun partTwo(data: String): Int {
        val initialLayout = Layout(data.split("\n").filter { it.isNotEmpty() })
        var newLayout = initialLayout
        var previousLayout: Layout
        do {
            previousLayout = newLayout
            newLayout = previousLayout.processLayoutPartTwo()
        } while (newLayout != previousLayout)
        return newLayout.count('#')
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        NORTH(0, -1),
        NORTH_EAST(1, -1),
        EAST(1, 0),
        SOUTH_EAST(1, 1),
        SOUTH(0, 1),
        SOUTH_WEST(-1, 1),
        WEST(-1, 0),
        NORTH_WEST(-1, -1);
    }
}
