package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*


fun main() {
    val data = readFileDirectlyAsText("/year2023/day14/data.txt")
    val day = Day14(2023, 14, "Parabolic Reflector Dish")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day14(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val grid = Grid.of(data)

        return grid.tilt(Direction.NORTH).findAll('O').sumOf { point ->
            grid.maxY - point.first + 1
        }
    }

    fun partTwo(data: String): Int {
        var grid = Grid.of(data)

        val cache = mutableMapOf<String, Int>()
        var cycle = 0 // number of cycles done
        val total = 1_000_000_000 // 1 billion
        var cycleLength = 0

        // We need to find the cycle length to avoid doing 1 billion cycles (which would take a long time)
        while (cycle < total) {
            val currentGridKey = grid.map().map { it.value.toString() }.joinTo(StringBuilder(), "").toString()
            if (currentGridKey in cache) {
                cycleLength = cycle - cache[currentGridKey]!!
                break
            }
            // cache the current grid and cycle number
            cache[currentGridKey] = cycle
            // process one cycle and increment the cycle number
            grid = grid.processOneCycle()
            cycle++
        }

        // If we found a cycle length, we can calculate the remaining cycles to do and do them
        if (cycleLength > 0) {
            // remaining cycles to do
            val remainingCycles = (total - cycle) % cycleLength
            for (i in 0 until remainingCycles) {
                grid = grid.processOneCycle()
            }
        }

        return grid.findAll('O').sumOf { point ->
            grid.maxY - point.first + 1
        }
    }

    // Process one cycle (= tilt the grid in all directions)
    private fun Grid<Char>.processOneCycle() =
        this.tilt(Direction.NORTH).tilt(Direction.WEST).tilt(Direction.SOUTH).tilt(Direction.EAST)


    // Tilt the grid in the given direction
    private fun Grid<Char>.tilt(direction: Direction): Grid<Char> {
        val roundRocks = if(direction == Direction.EAST || direction == Direction.SOUTH) this.findAll('O').reversed() else this.findAll('O')
        roundRocks.forEach { start ->
            var destination = start
            while (this[direction.move(destination)] == '.') {
                destination = direction.move(destination)
            }
            this[start] = '.'
            this[destination] = 'O'
        }

        return this
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        NORTH(-1, 0),
        EAST(0, 1),
        SOUTH(1, 0),
        WEST(0, -1);

        fun move(point: Pair<Int, Int>): Pair<Int, Int> {
            return Pair(point.first + dx, point.second + dy)
        }
    }
}

