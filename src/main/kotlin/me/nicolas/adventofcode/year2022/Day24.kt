package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day24/training.txt")
    val data = readFileDirectlyAsText("/year2022/day24/data.txt")

    val inputs = data.split("\n")

    val day = Day24(2022, 24, "Blizzard Basin", inputs)
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo() }
}

/**
 * Faster solution using Breadth-First Search.
 */
private class Day24(year: Int, day: Int, title: String, inputs: List<String>) : AdventOfCodeDay(year, day, title) {

    private val valley: Valley = buildValley(inputs)
    private val start: Pair<Int, Int> = Pair(inputs.first().indexOfFirst { it == '.' }, 0)
    private val goal: Pair<Int, Int> = Pair(inputs.last().indexOfFirst { it == '.' }, inputs.lastIndex)

    fun partOne(): Int {
        return solve(this.start, this.goal, this.valley, 0).first
    }


    fun partTwo(): Int {
        val toGoal = solve(this.start, this.goal, this.valley, 0)
        val backToStart = solve(this.goal, this.start, toGoal.second, toGoal.first)
        return solve(this.start, this.goal, backToStart.second, backToStart.first).first
    }

    private fun solve(
        start: Pair<Int, Int>,
        goal: Pair<Int, Int>,
        startState: Valley,
        minutes: Int,
    ): Pair<Int, Valley> {
        val mapStates = mutableMapOf(Pair(minutes, startState))
        val queue = mutableListOf(Path(minutes, start))
        val seen = mutableSetOf<Path>()

        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            if (path !in seen) {
                seen += path
                val nextMapState = mapStates.computeIfAbsent(path.minutes + 1) { key ->
                    mapStates.getValue(key - 1).nextState()
                }
                val neighbours = path.location.neighbours()

                // Is goal reached?
                if (goal in neighbours) {
                    return Pair(path.minutes + 1, nextMapState)
                }

                // Add neighbours that will be open to move to on the next turn.
                neighbours
                    .filter { it == this.start || (nextMapState.inInBounds(it) && nextMapState.isOpen(it)) }
                    .forEach { neighbour ->
                        queue.add(path.next(neighbour))
                    }
            }
        }
        throw RuntimeException("No path to goal")
    }

    private data class Valley(val height: Int, val width: Int, val blizzards: Set<Blizzard>) {
        private val unsafeLocations = blizzards.map { it.location }.toSet()

        fun isOpen(place: Pair<Int, Int>): Boolean =
            place !in unsafeLocations

        fun inInBounds(place: Pair<Int, Int>): Boolean =
            place.first in 1..height && place.second in 1..width

        fun nextState(): Valley =
            Valley(height, width, blizzards.map { it.next(Pair(height, width)) }.toSet())
    }

    private data class Blizzard(val location: Pair<Int, Int>, val direction: Pair<Int, Int>) {

        fun next(boundary: Pair<Int, Int>): Blizzard {
            var nextLocation = Pair(location.first + direction.first, location.second + direction.second)
            when {
                nextLocation.first == 0 -> nextLocation = Pair(boundary.first, location.second)
                nextLocation.first > boundary.first -> nextLocation = Pair(1, location.second)
                nextLocation.second == 0 -> nextLocation = Pair(location.first, boundary.second)
                nextLocation.second > boundary.second -> nextLocation = Pair(location.first, 1)
            }
            return Blizzard(nextLocation, direction)
        }
    }

    private data class Path(val minutes: Int, val location: Pair<Int, Int>) {
        fun next(place: Pair<Int, Int>): Path =
            Path(minutes + 1, place)
    }

    private fun Pair<Int, Int>.neighbours() = setOf(
        Pair(first, second),
        Pair(first - 1, second),
        Pair(first + 1, second),
        Pair(first, second - 1),
        Pair(first, second + 1)
    )

    private fun buildValley(inputs: List<String>): Valley =
        Valley(
            inputs.first().lastIndex - 1, inputs.lastIndex - 1,
            inputs.flatMapIndexed { y, row ->
                row.mapIndexedNotNull { x, char ->
                    when (char) {
                        '>' -> Blizzard(Pair(x, y), Pair(1, 0))
                        '<' -> Blizzard(Pair(x, y), Pair(-1, 0))
                        'v' -> Blizzard(Pair(x, y), Pair(0, 1))
                        '^' -> Blizzard(Pair(x, y), Pair(0, -1))
                        else -> null
                    }
                }
            }.toSet()
        )
}
