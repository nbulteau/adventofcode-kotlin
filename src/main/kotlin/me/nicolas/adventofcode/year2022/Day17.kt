package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day17/training.txt")
    val data = readFileDirectlyAsText("/year2022/day17/data.txt")

    val day = Day17Pouet(2022, 17, "Pyroclastic Flow", data)
    prettyPrintPartOne { day.partOne(2022) }
    prettyPrintPartTwo { day.partTwo(1_000_000_000_000L) }
}

class Day17Pouet(year: Int, day: Int, title: String, private val jetPatterns: String) :
    AdventOfCodeDay(year, day, title) {

    private val minus = listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0))
    private val plus = listOf(Pair(1, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1), Pair(1, 2))
    private val reverseL = listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(2, 1), Pair(2, 2))
    private val pipe = listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3))
    private val block = listOf(Pair(0, 0), Pair(1, 0), Pair(0, 1), Pair(1, 1))
    private val rocks = listOf(minus, plus, reverseL, pipe, block)

    fun partOne(count: Int): Long {
        val verticalChamber = mutableListOf<Pair<Int, Int>>()
        var highestRock = -1 // First row of rocks on the floor is 0
        var patternIndex = 0

        repeat(count) { rockCount ->
            // Get rock shape and position it 2 from the left & 4 positions higher than the last rock (3 spaces between them)
            val fallingRock = rocks[rockCount % rocks.size].map { it + Pair(2, highestRock + 4) }

            val result = processFallingRock(verticalChamber, fallingRock, patternIndex)
            patternIndex = result.second

            highestRock = verticalChamber.maxOf { it.second }
        }

        return highestRock.toLong() + 1
    }

    /**
     * The general way to solve this problem is to simulate moves until we detect a loop, and then extrapolate the answer from there.
     */
    fun partTwo(count: Long): Long {
        var highestRock = -1 // First row of rocks on the floor is 0
        var patternIndex = 0
        val verticalChamber = mutableListOf<Pair<Int, Int>>()

        var cycleStart: Int? = null
        val seen = mutableSetOf<Int>()
        var cycleStartLastRockCoord: Pair<Int, Int>? = null
        var cycleStartRockCount = -1
        val partialCycleHeights = mutableListOf<Int>()

        // 5K seems to be enough to find a pattern
        repeat(5_000) { rockCount ->
            // Get rock shape and position it 2 from the left & 4 positions higher than the last rock (3 spaces between them)
            var fallingRock = rocks[rockCount % rocks.size].map { it + Pair(2, highestRock + 4) }

            val result = processFallingRock(verticalChamber, fallingRock, patternIndex)
            fallingRock = result.first
            patternIndex = result.second

            highestRock = verticalChamber.maxOf { it.second }

            // Cycle detection: Only after one complete set of rocks
            if (rockCount % rocks.size == rocks.size - 1) {
                if (cycleStart != null && patternIndex % jetPatterns.length == cycleStart) {
                    val cycleHeight = fallingRock.first().second - cycleStartLastRockCoord!!.second
                    val cycleLength = rockCount - cycleStartRockCount
                    val rocksToJumpOver = count - cycleStartRockCount
                    val cyclesToJump = rocksToJumpOver / cycleLength
                    val extraRocks = (rocksToJumpOver % cycleLength).toInt()

                    return (cyclesToJump * cycleHeight) + partialCycleHeights[extraRocks]
                } else if (cycleStart == null) {
                    val instruction = patternIndex % jetPatterns.length
                    // add return true if the element has been added, false if the element is already contained in the set.
                    if (!seen.add(instruction)) {
                        // If seen before we've got a cycle: remember the start of this cycle
                        cycleStart = instruction
                        cycleStartLastRockCoord = fallingRock.first()
                        cycleStartRockCount = rockCount
                    }
                }
            }
            // Remember heights for each step
            if (cycleStartLastRockCoord != null) {
                partialCycleHeights.add(highestRock)
            }
        }

        // No cycle detected before reaching count
        return highestRock.toLong() + 1
    }

    private fun processFallingRock(
        verticalChamber: MutableList<Pair<Int, Int>>,
        fallingRock: List<Pair<Int, Int>>,
        patternIndex: Int,
    ): Pair<List<Pair<Int, Int>>, Int> {
        var newFallingRock = fallingRock
        var patternIndex1 = patternIndex
        while (true) {
            val instruction = jetPatterns[patternIndex1 % jetPatterns.length]
            patternIndex1++
            // Move according to jet patterns, if possible
            if (instruction == '<') {
                val newPossibleFallingRock = newFallingRock.map { it.move(Direction.LEFT) }
                if (newPossibleFallingRock.none { it in verticalChamber || it.first !in 0..6 }) {
                    newFallingRock = newPossibleFallingRock
                }
                // Else continue with current rock
            } else {
                val newPossibleFallingRock = newFallingRock.map { it.move(Direction.RIGHT) }
                if (newPossibleFallingRock.none { it in verticalChamber || it.first !in 0..6 }) {
                    newFallingRock = newPossibleFallingRock
                }
                // Else continue with current rock
            }

            // Move down
            val newPossibleFallingRock = newFallingRock.map { it.move(Direction.DOWN) }
            if (newPossibleFallingRock.none { it in verticalChamber || it.second < 0 }) {
                newFallingRock = newPossibleFallingRock
            } else {
                // Hit rock bottom
                break
            }
        }

        verticalChamber.addAll(newFallingRock)

        return Pair(newFallingRock, patternIndex1)
    }


    private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) =
        Pair(this.first + other.first, this.second + other.second)

    private infix fun Pair<Int, Int>.move(direction: Direction) = this + direction.coord

    private enum class Direction(val coord: Pair<Int, Int>) {
        DOWN(Pair(0, -1)),
        LEFT(Pair(-1, 0)),
        RIGHT(Pair(1, 0)),
    }
}