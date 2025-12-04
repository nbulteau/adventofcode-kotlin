package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.SimpleGrid
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 4: Printing Department ---
// https://adventofcode.com/2025/day/4
fun main() {
    val data = readFileDirectlyAsText("/year2025/day04/data.txt")
    val day = Day04Bis(2025, 4)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
    prettyPrintPartTwo { day.partTwoBis(data) }

}

class Day04Bis(year: Int, day: Int, title: String = "Printing Department") : AdventOfCodeDay(year, day, title) {

    /**
     * Count all rolls (@) that are accessible (have fewer than 4 adjacent rolls).
     * A roll is accessible if it can be reached without being blocked by 4 or more adjacent rolls.
     * Uses SimpleGrid utility to simplify grid operations and point neighbor calculations.
     */
    fun partOne(data: String): Int {
        val grid = SimpleGrid.of(data)

        return grid.findAll('@')
            .count { point ->
                val adjacentRolls = point.neighbors().count { neighbor ->
                    grid.contains(neighbor) && grid[neighbor] == '@'
                }
                adjacentRolls < 4
            }
    }

    /**
     * Iteratively remove all accessible rolls until no more can be removed.
     * Each iteration:
     * 1. Find all rolls (@) in the entire grid
     * 2. Filter those with fewer than 4 adjacent rolls
     * 3. Remove them by replacing with '.'
     * 4. Repeat until no accessible rolls remain
     */
    fun partTwo(data: String): Int {
        val grid = SimpleGrid.of(data)

        var totalRemoved = 0
        var hasRemoved = true

        while (hasRemoved) {
            hasRemoved = false
            val toRemove = grid.findAll('@')
                .filter { point ->
                    val adjacentRolls = point.neighbors().count { neighbor ->
                        grid.contains(neighbor) && grid[neighbor] == '@'
                    }
                    adjacentRolls < 4
                }

            if (toRemove.isNotEmpty()) {
                hasRemoved = true
                toRemove.forEach { point ->
                    grid[point] = '.'
                }
                totalRemoved += toRemove.size
            }
        }

        return totalRemoved
    }

    /**
     * Optimized version of partTwo that only tracks affected points.
     * Instead of scanning the entire grid each iteration, maintains a set of points to check.
     * Each iteration:
     * 1. Check only the points that are still '@' in the tracking set
     * 2. Filter those with fewer than 4 adjacent rolls
     * 3. Remove them from the grid and tracking set
     * 4. Repeat until no accessible rolls remain in the tracking set
     * Returns the total number of rolls removed.
     */
    fun partTwoBis(data: String): Int {
        val grid = SimpleGrid.of(data)

        var totalRemoved = 0
        val pointsToCheck = grid.findAll('@').toMutableSet()

        while (pointsToCheck.isNotEmpty()) {
            val toRemove = pointsToCheck.filter { point ->
                val adjacentRolls = point.neighbors().count { neighbor ->
                    grid.contains(neighbor) && grid[neighbor] == '@'
                }
                adjacentRolls < 4
            }

            if (toRemove.isEmpty()) break

            toRemove.forEach { point ->
                grid[point] = '.'
                pointsToCheck.remove(point)
            }

            totalRemoved += toRemove.size
        }

        return totalRemoved
    }

}
