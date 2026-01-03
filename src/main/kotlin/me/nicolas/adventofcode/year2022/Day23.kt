package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day23/training.txt")
    val data = readFileDirectlyAsText("/year2022/day23/data.txt")

    val inputs = data.split("\n")

    val day = Day23(2022, 23, "Unstable Diffusion", inputs)
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo() }
}

private class Day23(year: Int, day: Int, title: String, inputs: List<String>) : AdventOfCodeDay(year, day, title) {

    private val elves: Set<Pair<Int, Int>> = inputs.flatMapIndexed { column: Int, line: String ->
        line.mapIndexedNotNull { row, char ->
            if (char == '#') Pair(row, column) else null
        }
    }.toSet()

    private enum class Direction(var dx: Int, var dy: Int) {
        NORTH(0, -1), SOUTH(0, 1), WEST(-1, 0), EAST(1, 0);

        fun moveOneStep(currentLocation: Pair<Int, Int>) = Pair(currentLocation.first + dx, currentLocation.second + dy)

        fun nextDirection() = entries[(ordinal + 1) % entries.size]
    }

    private fun Pair<Int, Int>.lookDirection(direction: Direction, actual: Set<Pair<Int, Int>>): Boolean {
        return when (direction) {
            Direction.NORTH -> {
                !(Pair(this.first - 1, this.second - 1) in actual ||
                        Pair(this.first, this.second - 1) in actual ||
                        Pair(this.first + 1, this.second - 1) in actual)
            }

            Direction.SOUTH -> {
                !(Pair(this.first - 1, this.second + 1) in actual ||
                        Pair(this.first, this.second + 1) in actual ||
                        Pair(this.first + 1, this.second + 1) in actual)
            }

            Direction.WEST -> {
                !(Pair(this.first - 1, this.second - 1) in actual ||
                        Pair(this.first - 1, this.second) in actual ||
                        Pair(this.first - 1, this.second + 1) in actual)
            }

            Direction.EAST -> {
                !(Pair(this.first + 1, this.second - 1) in actual ||
                        Pair(this.first + 1, this.second) in actual ||
                        Pair(this.first + 1, this.second + 1) in actual)
            }
        }
    }

    private fun Pair<Int, Int>.lookAround(actual: Set<Pair<Int, Int>>): Boolean {
        return Pair(this.first - 1, this.second - 1) in actual ||
                Pair(this.first, this.second - 1) in actual ||
                Pair(this.first + 1, this.second - 1) in actual ||
                Pair(this.first - 1, this.second + 1) in actual ||
                Pair(this.first, this.second + 1) in actual ||
                Pair(this.first - 1, this.second) in actual ||
                Pair(this.first + 1, this.second) in actual ||
                Pair(this.first + 1, this.second + 1) in actual
    }

    fun partOne(): Int {
        var actual = elves
        var currentDirection = Direction.NORTH
        repeat(10) {
            val (newElves, _) = processRound(actual, currentDirection)
            actual = newElves
            currentDirection = currentDirection.nextDirection()
        }

        return (actual.maxOf { it.first } - actual.minOf { it.first } + 1) * (actual.maxOf { it.second } - actual.minOf { it.second } + 1) - actual.size
    }

    fun partTwo(): Int {
        var actual = elves
        var currentDirection = Direction.NORTH
        var iteration = 0
        do {
            val (newElves, moveCount) = processRound(actual, currentDirection)
            actual = newElves
            currentDirection = currentDirection.nextDirection()
            iteration++
        } while (moveCount != 0)

        return iteration
    }

    private fun processRound(actual: Set<Pair<Int, Int>>, currentDirection: Direction): Pair<Set<Pair<Int, Int>>, Int> {
        // display(actual)

        // Elf looks in each of four directions in the following order and proposes moving one step in the first valid direction
        val proposedMoves = actual.associateWith { elf ->
            var move: Pair<Int, Int>? = null
            var lookDirection = currentDirection
            var index = 0
            do {
                if (!elf.lookAround(actual)) {
                    move = elf
                } else {
                    if (elf.lookDirection(lookDirection, actual)) {
                        move = lookDirection.moveOneStep(elf)
                    } else {
                        lookDirection = lookDirection.nextDirection()
                    }
                    index++
                    if (index == 4 && move == null) {
                        move = elf
                    }
                }
            } while (move == null)
            move
        }

        // Elf moves to their proposed destination tile if they were the only Elf to propose moving to that position
        val proposedMoveCounts = proposedMoves.values.groupingBy { it }.eachCount()
        var moveCount = 0
        return actual.map { elf ->
            val proposedMove = proposedMoves[elf]!!
            val count = proposedMoveCounts[proposedMove]!!
            if (count == 1 && proposedMove != elf) {
                moveCount++
                proposedMove
            } else {
                elf
            }
        }.toSet() to moveCount
    }

    private fun display(elves: List<Pair<Int, Int>>) {
        for (y in elves.minOf { it.second } - 1..elves.maxOf { it.second } + 1) {
            for (x in elves.minOf { it.first } - 1..elves.maxOf { it.first } + 1) {
                val pair = Pair(x, y)
                if (elves.contains(pair)) {
                    print("#")
                } else {
                    print('.')
                }
            }
            println()
        }
        println()
    }
}
 
