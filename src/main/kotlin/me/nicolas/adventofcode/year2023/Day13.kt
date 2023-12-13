package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*

fun main() {
    val data = readFileDirectlyAsText("/year2023/day13/data.txt")
    val day = Day13(2023, 13, "Point of Incidence")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day13(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val patterns = parseData(data)

        return patterns.sumOf { pattern ->
            val horizontals = (1 until pattern.rows).sumOf { axis -> if (reflectsHorizontally(pattern, axis)) axis else 0 }
            val verticals = (1 until pattern.columns).sumOf { axis -> if (reflectsVertically(pattern, axis)) axis else 0 }

            100 * horizontals + verticals
        }
    }

    fun partTwo(data: String): Int {
        val patterns = parseData(data)

        // one smudge: exactly one . or # should be the opposite type
        return patterns.sumOf { pattern ->
            val horizontals = (1 until pattern.rows).sumOf { axis -> if (reflectsHorizontally(pattern, axis, smudge = 1)) axis else 0 }
            val verticals = (1 until pattern.columns).sumOf { axis -> if (reflectsVertically(pattern, axis, smudge = 1)) axis else 0 }

            100 * horizontals + verticals
        }
    }

    // Check if the grid reflects horizontally itself on the axis
    private fun reflectsHorizontally(grid: Grid<Char>, axis: Int, smudge: Int = 0): Boolean {
        val len = minOf(axis, grid.rows - axis)

        return (0 until len).sumOf { row ->
            grid.getRow(axis - row - 1).zip(grid.getRow(axis + row)).count { (char, otherChar) -> char != otherChar }
        } == smudge

    }

    // Check if the grid reflects vertically itself on the axis
    private fun reflectsVertically(grid: Grid<Char>, axis: Int, smudge: Int = 0): Boolean {
        val len = minOf(axis, grid.columns - axis)

        return (0 until len).sumOf { row ->
            grid.getColumn(axis - row - 1).zip(grid.getColumn(axis + row)).count { (char, otherChar) -> char != otherChar }
        } == smudge
    }

    // Parse the data into a list of grids of chars (patterns)
    private fun parseData(data: String): List<Grid<Char>> {
        return data.split("\n\n").map { pattern ->
            Grid.of(pattern)
        }
    }
}

