package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day24/training.txt")
    val data = readFileDirectlyAsText("/year2022/day24/data.txt")

    val inputs = data.split("\n")

    val day = Day24("--- Day 24: Blizzard Basin ---", "https://adventofcode.com/2022/day/24")
    prettyPrintPartOne { day.partOne(inputs) }
    prettyPrintPartTwo { day.partTwo(inputs) }
}

private class Day24(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(inputs: List<String>): Int {
        var valley = buildValley(inputs)

        var steps = 0
        while (!valley.isGoalReached) {
            //valley.display()
            steps++
            valley = valley.processStep()
        }

        return steps
    }

    fun partTwo(inputs: List<String>): Int {
        var valley = buildValley(inputs)
        val width = valley.width
        val height = valley.height
        val start = valley.positions.first()
        val goal = valley.goal

        var steps = 0
        // first trip to the goal
        while (!valley.isGoalReached) {
            steps++
            valley = valley.processStep()
        }

        // trip back to the start
        valley = Valley(width, height, goal, start, valley.blizzards, setOf(goal))
        while (!valley.isGoalReached) {
            steps++
            valley = valley.processStep()
        }

        // trip back to the goal again
        valley = Valley(width, height, start, goal, valley.blizzards, setOf(start))
        while (!valley.isGoalReached) {
            steps++
            valley = valley.processStep()
        }

        return steps
    }

    private enum class Direction(val vec: Pair<Int, Int>) {
        UP(Pair(-1, 0)), RIGHT(Pair(0, 1)), DOWN(Pair(1, 0)), LEFT(Pair(0, -1));

        fun step(position: Pair<Int, Int>): Pair<Int, Int> =
            Pair(vec.first + position.first, vec.second + position.second)

        override fun toString(): String {
            return when (this) {
                UP -> "^"
                RIGHT -> ">"
                DOWN -> "v"
                LEFT -> "<"
            }
        }
    }

    private class Valley(
        val width: Int, val height: Int,
        val start: Pair<Int, Int>,
        val goal: Pair<Int, Int>,
        val blizzards: List<Pair<Pair<Int, Int>, Direction>>,
        val positions: Set<Pair<Int, Int>>
    ) {
        val isGoalReached = positions.contains(goal)

        fun processStep(): Valley {
            val nextBlizzards = blizzards.map { (position, direction) ->
                var (row, column) = direction.step(position)
                if (row == 0) row = height - 2
                if (column == 0) column = width - 2
                if (row == height - 1) row = 1
                if (column == width - 1) column = 1
                Pair(Pair(row, column), direction)
            }

            val nextPositions = positions.flatMap { position ->
                listOf(
                    position,
                    Direction.UP.step(position),
                    Direction.RIGHT.step(position),
                    Direction.DOWN.step(position),
                    Direction.LEFT.step(position)
                )
            }.filter { (row, column) ->
                val current = Pair(row, column)
                current == start || current == goal ||
                        row in 1 until height - 1 && column in 1 until width - 1 &&
                        current !in nextBlizzards.map { it.first }
            }.toSet()

            return Valley(width, height, start, goal, nextBlizzards, nextPositions)
        }

        fun display() {
            val blizzardsGroupByPositions = blizzards.groupBy { it.first }

            (0 until height).forEach { row ->
                (0 until width).forEach { column ->
                    val current = Pair(row, column)
                    if (current in blizzardsGroupByPositions) {
                        if (blizzardsGroupByPositions[current]!!.size > 1) {
                            print(blizzardsGroupByPositions[current]!!.size)
                        } else {
                            print(blizzardsGroupByPositions[current]!!.first().second)
                        }
                    } else if (current == start || current == goal) {
                        print('.')
                    } else if (row == 0 || row == height - 1 || column == 0 || column == width - 1) {
                        print('#')
                    } else {
                        print('.')
                    }
                }
                println()
            }
            println()
        }
    }

    private fun buildValley(inputs: List<String>): Valley {
        val width = inputs.first().length
        val height = inputs.size
        val start = Pair(0, inputs.first().indexOfFirst { char -> char == '.' })
        val goal = Pair(height - 1, inputs.last().indexOfFirst { char -> char == '.' })

        val blizzards = inputs.flatMapIndexed { row, line ->
            line.mapIndexedNotNull() { column, char ->
                when (char) {
                    '^' -> Pair(row, column) to Direction.UP
                    '>' -> Pair(row, column) to Direction.RIGHT
                    'v' -> Pair(row, column) to Direction.DOWN
                    '<' -> Pair(row, column) to Direction.LEFT
                    else -> null
                }
            }
        }

        return Valley(width, height, start, goal, blizzards, setOf(start))
    }
}
 
