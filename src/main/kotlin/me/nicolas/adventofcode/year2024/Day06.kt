package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.*
import me.nicolas.adventofcode.year2024.Day06.Direction.UP

// --- Day 6: Guard Gallivant ---
// https://adventofcode.com/2024/day/6
fun main() {
    val data = readFileDirectlyAsText("/year2024/day06/data.txt")
    val day = Day06(2024, 6, "Guard Gallivant")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day06(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private enum class Direction(val point: Pair<Int, Int>) {
        UP(Pair(-1, 0)), DOWN(Pair(+1, 0)), LEFT(Pair(0, -1)), RIGHT(Pair(0, 1));

        fun turnRight(): Direction {
            return when (this) {
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
                UP -> RIGHT
            }
        }
    }

    private fun Pair<Int, Int>.stepForward(direction: Direction): Pair<Int, Int> =
        Pair(this.first + direction.point.first, this.second + direction.point.second)

    fun partOne(data: String): Int {
        val grid = Grid.of(data)
        val visited = process(grid)

        return visited.size
    }

    private fun process(grid: Grid<Char>): MutableSet<Pair<Int, Int>> {
        var guard = grid.findAll('^').first()
        var currentDirection = UP

        val rows = grid.rows
        val columns = grid.columns
        fun Grid<Char>.isInMap(point: Pair<Int, Int>) =
            point.first in 0..<columns && point.second >= 0 && point.second < rows

        val visited = mutableSetOf<Pair<Int, Int>>()

        do {
            visited.add(guard)
            val front =
                grid[Pair(guard.first + currentDirection.point.first, guard.second + currentDirection.point.second)]
            when (front) {
                '#' -> currentDirection = currentDirection.turnRight()
                else -> guard = guard.stepForward(currentDirection)
            }
        } while (grid.isInMap(guard))
        return visited
    }

    fun partTwo(data: String): Int {
        var grid = Grid.of(data)

        val rows = grid.rows
        val columns = grid.columns

        fun Grid<Char>.isInMap(point: Pair<Int, Int>) =
            point.first in 0..<columns && point.second >= 0 && point.second < rows

        // val pointsToTest = grid.indices - grid.findAll('#').toSet() - grid.findAll('^').toSet()
        val pointsToTest = process(grid) - grid.findAll('^').toSet()

        val obstructions = mutableSetOf<Pair<Int, Int>>()
        pointsToTest.forEach { point ->
            grid = Grid.of(data)
            grid[point] = '#'
            var guard = grid.findAll('^').first()
            var currentDirection = UP

            val visited = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()

            var isLoop = false
            do {
                if (!visited.add(Pair(guard, currentDirection))) {
                    isLoop = true
                    break
                }
                val front =
                    grid[Pair(guard.first + currentDirection.point.first, guard.second + currentDirection.point.second)]
                when (front) {
                    '#' -> currentDirection = currentDirection.turnRight()
                    else -> guard = guard.stepForward(currentDirection)
                }
            } while (grid.isInMap(guard))

            if (isLoop) {
                obstructions.add(point)
            }
        }

        return obstructions.size
    }
}