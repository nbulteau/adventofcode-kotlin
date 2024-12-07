package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.*

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
        UP(Pair(-1, 0)), DOWN(Pair(1, 0)), LEFT(Pair(0, -1)), RIGHT(Pair(0, 1));

        fun turnRight() = when (this) {
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            UP -> RIGHT
        }
    }

    private fun Pair<Int, Int>.stepForward(direction: Direction) =
        Pair(this.first + direction.point.first, this.second + direction.point.second)

    fun partOne(data: String): Int {
        val grid = Grid.of(data)
        return process(grid).size
    }

    private fun process(grid: Grid<Char>): MutableSet<Pair<Int, Int>> {
        var guard = grid.findAll('^').first()
        var currentDirection = Direction.UP
        val visited = mutableSetOf<Pair<Int, Int>>()

        do {
            visited.add(guard)
            val front = grid[guard.stepForward(currentDirection)]
            currentDirection = if (front == '#') currentDirection.turnRight() else currentDirection
            guard = if (front != '#') guard.stepForward(currentDirection) else guard
        } while (grid.isInMap(guard))

        return visited
    }

    fun partTwo(data: String): Int {
        val grid = Grid.of(data)
        val pointsToTest = process(grid) - grid.findAll('^').toSet()
        val obstructions = mutableSetOf<Pair<Int, Int>>()

        pointsToTest.parallelStream().forEach { point ->
            val modifiedGrid = grid.copy().apply { this[point] = '#' }
            var guard = modifiedGrid.findAll('^').first()
            var currentDirection = Direction.UP
            val visited = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
            var isLoop = false

            do {
                if (!visited.add(Pair(guard, currentDirection))) {
                    isLoop = true
                    break
                }
                val front = modifiedGrid[guard.stepForward(currentDirection)]
                currentDirection = if (front == '#') currentDirection.turnRight() else currentDirection
                guard = if (front != '#') guard.stepForward(currentDirection) else guard
            } while (modifiedGrid.isInMap(guard))

            if (isLoop) obstructions.add(point)
        }

        return obstructions.size
    }

    private fun Grid<Char>.isInMap(point: Pair<Int, Int>) =
        point.first in 0 until columns && point.second in 0 until rows
}