package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.Point
import me.nicolas.adventofcode.utils.SimpleGrid
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 7: Laboratories ---
// https://adventofcode.com/2025/day/7
fun main() {
    val data = readFileDirectlyAsText("/year2025/day07/data.txt")
    val day = Day07(2025, 7)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07(year: Int, day: Int, title: String = "Laboratories") : AdventOfCodeDay(year, day, title) {


    /**
     * Simulates a beam traveling down through a manifold and counts the number of unique splitters encountered.
     * When a beam hits a splitter ('^'), it creates two new beams that diverge left and right.
     *
     * Algorithm:
     * - Track all active beams as they move down row by row
     * - When a beam hits a splitter, count it (only once per splitter position)
     * - Split the beam into left and right beams at the splitter position
     * - Continue until all beams exit the bottom of the manifold
     */
    fun partOne(data: String): Int {
        // Create SimpleGrid without trimming lines to preserve spaces
        val grid = SimpleGrid.of(data)

        // Find starting position 'S'
        val startPoint = grid.findOne { it == 'S' }

        // Track active beams and count splits
        var splitCount = 0
        var activeBeams = mutableSetOf<Point>()
        val visitedSplits = mutableSetOf<Point>() // Track which splitters we've already counted

        // Start with a beam at S
        activeBeams.add(startPoint)

        // Process beams row by row until all beams exit
        while (activeBeams.isNotEmpty()) {
            val newBeams = mutableSetOf<Point>()

            for (beam in activeBeams) {
                // Move down: increment row (x in SimpleGrid convention)
                val nextPoint = Point(beam.x + 1, beam.y)

                // Check if beam exits the manifold (reached bottom)
                if (nextPoint !in grid) {
                    continue
                }

                val nextCell = grid[nextPoint]

                // Check what's at the next position
                when (nextCell) {
                    '.', 'S' -> {
                        // Empty space or start -> continue downward
                        newBeams.add(nextPoint)
                    }

                    '^' -> {
                        // Hit a splitter -> count it if we haven't seen it before
                        if (!visitedSplits.contains(nextPoint)) {
                            visitedSplits.add(nextPoint)
                            splitCount++
                        }

                        // Create two beams at left and right positions relative to the splitter
                        val leftPoint = Point(nextPoint.x, nextPoint.y - 1)
                        val rightPoint = Point(nextPoint.x, nextPoint.y + 1)

                        // Only add beams if they're still within the grid bounds
                        if (leftPoint in grid) {
                            newBeams.add(leftPoint)
                        }
                        if (rightPoint in grid) {
                            newBeams.add(rightPoint)
                        }
                    }
                }
            }

            activeBeams = newBeams
        }

        return splitCount
    }


    /**
     * Count quantum timelines (using dynamic programming).
     *
     * In a quantum tachyon manifold, a single particle takes BOTH paths at each splitter,
     * creating parallel timelines (many-worlds interpretation). This function counts the
     * total number of distinct timelines that emerge after the particle completes its journey.
     *
     * First approach was to simulate each timeline separately => exponential growth in timelines.
     *
     * Final algorithm:
     * - Track the count of paths at each column in the current row
     * - Process row by row from start to bottom
     * - When paths encounter empty space, they continue straight down
     * - When paths hit a splitter ('^'), they split: each path becomes two paths (left and right)
     * - Count paths that exit the grid (reaching the bottom, note: going off the sides is not possible here)
     *
     * Complexity: O(rows × cols) instead of O(2^splitters) for my first naive simulation
     */
    fun partTwo(data: String): Long {
        val grid = SimpleGrid.of(data)
        val startPoint = grid.findOne { it == 'S' }

        // Dynamic programming: Map of column -> number of paths reaching that column in current row
        var currentRowPaths = mutableMapOf<Int, Long>()
        currentRowPaths[startPoint.y] = 1L  // Start with 1 path at the starting column

        val maxRow =  grid.rows.last

        // Accumulator for timelines that have exited the manifold
        var totalTimelines = 0L

        // Process each row from start to end
        for (row in startPoint.x..maxRow) {
            val nextRowPaths = mutableMapOf<Int, Long>()

            // For each column that has paths in the current row
            for ((col, pathCount) in currentRowPaths) {
                val nextPoint = Point(row + 1, col)

                // Check if paths exit the manifold (reached bottom)
                if (nextPoint !in grid) {
                    // All paths at this position exit => count them as completed timelines
                    totalTimelines += pathCount
                    continue
                }

                val nextCell = grid[nextPoint]

                when (nextCell) {
                    '.', 'S' -> {
                        // Empty space => paths continue straight down to the same column in next row
                        nextRowPaths[col] = nextRowPaths.getOrDefault(col, 0L) + pathCount
                    }

                    '^' -> {
                        // Splitter => each path splits into two paths (quantum branching)
                        // The paths diverge to adjacent columns on the splitter's row
                        val leftCol = col - 1
                        val rightCol = col + 1

                        val leftPoint = Point(row + 1, leftCol)
                        val rightPoint = Point(row + 1, rightCol)

                        // Left paths: add to next row if within bounds, else they exit
                        if (leftPoint in grid) {
                            nextRowPaths[leftCol] = nextRowPaths.getOrDefault(leftCol, 0L) + pathCount
                        } else {
                            // Left paths exit immediately (went off the left edge: impossible here but for completeness)
                            totalTimelines += pathCount
                        }

                        // Right paths: add to next row if within bounds, else they exit
                        if (rightPoint in grid) {
                            nextRowPaths[rightCol] = nextRowPaths.getOrDefault(rightCol, 0L) + pathCount
                        } else {
                            // Right paths exit immediately (went off the right edge: impossible here but for completeness)
                            totalTimelines += pathCount
                        }
                    }
                }
            }

            // Move to next row
            currentRowPaths = nextRowPaths
        }

        return totalTimelines
    }
}