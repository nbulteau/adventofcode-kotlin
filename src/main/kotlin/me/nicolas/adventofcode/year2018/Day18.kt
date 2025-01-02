package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.*

// --- Day 18: Settlers of The North Pole ---
// https://adventofcode.com/2018/day/18
fun main() {
    val data = readFileDirectlyAsText("/year2018/day18/data.txt")
    val day = Day18()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day18(year: Int = 2018, day: Int = 18, title: String = "Settlers of The North Pole") :
    AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val grid = SimpleGrid.of(data)
        val newGrid = SimpleGrid(grid.width, grid.height)
        val minutes = 10

        repeat(minutes) {
            updateGrid(grid, newGrid)
            grid.swap(newGrid)
        }

        val wooded = grid.findAll('|').size
        val lumberyards = grid.findAll('#').size

        return wooded * lumberyards
    }

    fun partTwo(data: String): Int {
        val grid = SimpleGrid.of(data)
        val newGrid = SimpleGrid(grid.width, grid.height)
        val seenStates = mutableMapOf<String, Int>()
        var cycleStart = 0
        var cycleLength = 0

        for (minute in 0 until 1_000_000_000) {
            val gridState = grid.toString()
            if (gridState in seenStates) {
                cycleStart = seenStates[gridState]!!
                cycleLength = minute - cycleStart
                break
            }
            seenStates[gridState] = minute

            updateGrid(grid, newGrid)
            grid.swap(newGrid)
        }

        val remainingMinutes = (1_000_000_000 - cycleStart) % cycleLength
        repeat(remainingMinutes) {
            updateGrid(grid, newGrid)
            grid.swap(newGrid)
        }

        val wooded = grid.findAll('|').size
        val lumberyards = grid.findAll('#').size

        return wooded * lumberyards
    }

    private fun updateGrid(grid: SimpleGrid<Char>, newGrid: SimpleGrid<Char>) {
        for (point in grid.points) {
            newGrid[point] = grid.getNextState(point)
        }
    }

    private fun SimpleGrid<Char>.getNextState(point: Point): Char {
        val adjacent = this.getAdjacent(point)
        return when (this[point]) {
            '.' -> if (adjacent.count { it == '|' } >= 3) '|' else '.'
            '|' -> if (adjacent.count { it == '#' } >= 3) '#' else '|'
            '#' -> if (adjacent.count { it == '#' } >= 1 && adjacent.count { it == '|' } >= 1) '#' else '.'
            else -> this[point]
        }
    }

    private fun SimpleGrid<Char>.getAdjacent(point: Point): List<Char> {
        val adjacent = mutableListOf<Char>()
        for (dy in -1..1) {
            for (dx in -1..1) {
                if (dx == 0 && dy == 0) continue
                val nx = point.x + dx
                val ny = point.y + dy
                Point(nx, ny).takeIf { it in this.points }?.let { adjacent.add(this[it]) }
            }
        }
        return adjacent
    }

    private fun SimpleGrid<Char>.swap(other: SimpleGrid<Char>) {
        for (point in this.points) {
            this[point] = other[point]
        }
    }
}