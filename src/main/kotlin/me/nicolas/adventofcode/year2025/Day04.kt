package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 4: Printing Department ---
// https://adventofcode.com/2025/day/4
fun main() {
    val data = readFileDirectlyAsText("/year2025/day04/data.txt")
    val day = Day04(2025, 4)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day04(year: Int, day: Int, title: String = "Printing Department") : AdventOfCodeDay(year, day, title) {

    // Directions for the 8 adjacent positions
    private val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,
        0 to -1,           0 to 1,
        1 to -1,  1 to 0,  1 to 1
    )

    /**
     * Count all rolls (@) that are accessible (have fewer than 4 adjacent rolls).
     * A roll is accessible if it can be reached without being blocked by 4 or more adjacent rolls.
     */
    fun partOne(data: String): Int {
        val grid = data.lines().map { line -> line.toList() }
        var accessibleCount = 0

        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == '@' && countAdjacentRolls(grid, row, col) < 4) {
                    accessibleCount++
                }
            }
        }

        return accessibleCount
    }

    /**
     * Iteratively remove all accessible rolls until no more can be removed.
     * Each iteration:
     * 1. Find all rolls with fewer than 4 adjacent rolls
     * 2. Remove them by replacing with '.'
     * 3. Repeat until no accessible rolls remain
     */
    fun partTwo(data: String): Int {
        val grid = data.lines().map { line -> line.toMutableList() }.toMutableList()
        var totalRemoved = 0
        var hasRemoved = true

        while (hasRemoved) {
            val toRemove = mutableListOf<Pair<Int, Int>>()

            // Find all accessible rolls in current state
            for (row in grid.indices) {
                for (col in grid[row].indices) {
                    if (grid[row][col] == '@' && countAdjacentRolls(grid, row, col) < 4) {
                        toRemove.add(row to col)
                    }
                }
            }

            // Remove all accessible rolls
            hasRemoved = toRemove.isNotEmpty()
            toRemove.forEach { (row, col) ->
                grid[row][col] = '.'
            }
            totalRemoved += toRemove.size
        }

        return totalRemoved
    }

    private fun countAdjacentRolls(grid: List<List<Char>>, row: Int, col: Int): Int {
        var count = 0
        val rows = grid.size
        val cols = grid[0].size

        for ((dr, dc) in directions) {
            val newRow = row + dr
            val newCol = col + dc

            if (newRow in 0 until rows && newCol in 0 until cols && grid[newRow][newCol] == '@') {
                count++
            }
        }
        return count
    }
}
