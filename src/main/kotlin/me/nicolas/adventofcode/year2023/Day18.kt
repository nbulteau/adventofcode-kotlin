package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*

fun main() {
    val data = readFileDirectlyAsText("/year2023/day18/data.txt")
    val day = Day18(2023, 18, "")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}


class Day18(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        val digPlan = parseDataPartOne(data)

        return solve(digPlan)
    }

    // This is a brut force solution, it works for the first part but not for the second part.
    // It consumes too much memory to be able to solve the second part.
    private fun solveBrutForce(digPlan: List<Pair<Direction, Long>>): Long {
        val grid = Grid<Char>()
        var current = Pair(0, 0)
        grid[current] = '#'

        digPlan.forEach { (direction, distance) ->
            repeat(distance.toInt()) {
                current = direction.nextPoint(current)
                grid[current] = '#'
            }
        }

        // Fill the grid with '.' where there is no '#'
        val minX = grid.minX
        val minY = grid.minY
        val maxX = grid.maxX
        val maxY = grid.maxY
        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                if (grid[Pair(x, y)] == null) {
                    grid[Pair(x, y)] = '.'
                }
            }
        }
        // Food fill the grid with '#' starting from (1, 1)
        grid.floodFill(Pair(1, 1), '#')
        // grid.display()

        return grid.count('#').toLong()
    }

    fun partTwo(data: String): Long {
        val digPlan = parseDataPartTwo(data)

        return solve(digPlan)
    }

    private fun solve(digPlan: List<Pair<Direction, Long>>): Long {
        val map = mutableListOf<Pair<Long, Long>>()
        var current = Pair(0L, 0L)
        var sumLengths = 0L // sum of all the lengths of the dig plan
        digPlan.forEach { (direction, distance) ->
            sumLengths += distance
            val x = current.second + (direction.dx * distance)
            val y = current.first + (direction.dy * distance)
            current = Pair(y, x)
            // Add next point of the line to the map
            map.add(Pair(current.first, current.second))
        }

        // Used Shoelace  https://en.wikipedia.org/wiki/Shoelace_formula to calculate area
        // And we can use Pick's theorem (https://en.wikipedia.org/wiki/Pick%27s_theorem) to calculate the interior points (area).
        // A = I + b / 2 -1  -> I = - b /2 + A + 1
        // This is needed as our line enclosing the area also hase width.
        // Interior + Exterior =  answers
        val interiorSum = shoelaceArea(map) - sumLengths / 2 + 1

        return interiorSum + sumLengths
    }

    private fun parseDataPartOne(data: String): List<Pair<Direction, Long>> {
        return data.lines().map { line ->
            val parts = line.trim().split(" ")
            val distance = parts[1].toLong()
            val direction = when (val move = parts[0]) {
                "R" -> Direction.EAST
                "L" -> Direction.WEST
                "U" -> Direction.NORTH
                "D" -> Direction.SOUTH
                else -> throw IllegalArgumentException("Unknown move: $move")
            }
            direction to distance
        }
    }

    private fun parseDataPartTwo(data: String): List<Pair<Direction, Long>> {
        return data.lines().map { line ->
            val parts = line.trim().split(" ")
            val distance = parts[2].subSequence(2, 7).toString().toLong(16) //
            val move = parts[2][7].toString() // 7 is the index of the direction
            val direction = when (move) {
                "0" -> Direction.EAST
                "2" -> Direction.WEST
                "3" -> Direction.NORTH
                "1" -> Direction.SOUTH
                else -> throw IllegalArgumentException("Unknown move: $move")
            }
            direction to distance
        }
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

        fun nextPoint(point: Pair<Int, Int>): Pair<Int, Int> {
            return Pair(point.first + dx, point.second + dy)
        }
    }

    /** Flood fill the grid with a new value starting from a specific point */
    private fun Grid<Char>.floodFill(startPoint: Pair<Int, Int>, newValue: Char): Grid<Char> {
        val originalValue = get(startPoint) ?: return this
        val visited = mutableSetOf<Pair<Int, Int>>()
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(startPoint)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current in visited || get(current) != originalValue) {
                continue
            }
            this[current] = newValue
            visited.add(current)
            queue.addAll(getCardinalNeighbors(current))
        }

        return this
    }
}

