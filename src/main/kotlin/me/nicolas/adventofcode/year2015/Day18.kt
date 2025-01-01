package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.*

// --- Day 18: ---
// https://adventofcode.com/2015/day/18
fun main() {
    val data = readFileDirectlyAsText("/year2015/day18/data.txt")
    val day = Day18(2015, 18)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day18(year: Int, day: Int, title: String = "Like a GIF For Your Yard") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String, steps: Int = 100): Int {
        val grid = SimpleGrid.of(data)

        return simulate(grid, steps)
    }

    fun partTwo(data: String, steps: Int = 100): Int {
        val grid = SimpleGrid.of(data)
        return simulateWithCornersAlwaysOn(grid, steps)
    }

    private fun simulate(grid: SimpleGrid<Char>, steps: Int): Int {
        var currentGrid = grid
        //println(currentGrid)
        //println()
        repeat(steps) {
            currentGrid = nextGridState(currentGrid)
            //println(currentGrid)
            //println()
        }

        return currentGrid.items.count { it == '#' }
    }

    private fun simulateWithCornersAlwaysOn(grid: SimpleGrid<Char>, steps: Int): Int {
        var currentGrid = grid
        setCornersAlwaysOn(currentGrid)
        repeat(steps) {
            currentGrid = nextGridState(currentGrid)
            setCornersAlwaysOn(currentGrid)
        }

        return currentGrid.items.count { it == '#' }
    }


    private fun setCornersAlwaysOn(grid: SimpleGrid<Char>) {
        val corners = listOf(
            Point(0, 0),
            Point(0, grid.width - 1),
            Point(grid.height - 1, 0),
            Point(grid.height - 1, grid.width - 1)
        )

        for (corner in corners) {
            grid[corner] = '#'
        }
    }

    private fun nextGridState(grid: SimpleGrid<Char>): SimpleGrid<Char> {
        val newGrid = SimpleGrid(grid.width, grid.height)
        for (point in grid.points) {
            val onNeighbors = countOnNeighbors(grid, point)
            val newState = when (val currentState = grid[point]) {
                '#' -> if (onNeighbors == 2 || onNeighbors == 3) {
                    '#'
                } else {
                    '.'
                }

                '.' -> if (onNeighbors == 3) {
                    '#'
                } else {
                    '.'
                }

                else -> currentState
            }
            newGrid[point] = newState
        }

        return newGrid
    }

    private val directions = listOf(
        Point(-1, -1), Point(-1, 0), Point(-1, 1),
        Point(0, -1), Point(0, 1),
        Point(1, -1), Point(1, 0), Point(1, 1)
    )

    private fun countOnNeighbors(grid: SimpleGrid<Char>, point: Point): Int {
        return directions.count { direction ->
            val neighbor = Point( point.x + direction.x, point.y + direction.y,)
            neighbor in grid && grid[neighbor] == '#'
        }
    }
}