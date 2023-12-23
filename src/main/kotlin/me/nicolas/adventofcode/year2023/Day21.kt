package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


fun main() {
    val data = readFileDirectlyAsText("/year2023/day21/data.txt")
    val day = Day21(2023, 21, "Step Counter")
    prettyPrintPartOne { day.partOne(data, 64) }
    prettyPrintPartTwo { day.partTwo(data, 26501365) }
}

class Day21(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String, stepsToDo: Int): Int {
        val lines = data.lines().toTypedArray()
        val (obstacles, start) = extractData(lines)

        val maxX = lines[0].length - 1
        val maxY = lines.size - 1

        var currentPositions = mutableSetOf<Pair<Int, Int>>()
        currentPositions.add(start)

        var nextPositions = mutableSetOf<Pair<Int, Int>>()
        for (i in 1..stepsToDo) {
            for (point in currentPositions) {
                for (nextPoint in point.getAdjacentPoints()) {
                    if (nextPoint.first < 0 || nextPoint.first > maxX || nextPoint.second < 0 || nextPoint.second > maxY) {
                        continue
                    }
                    if (obstacles.contains(nextPoint)) {
                        continue
                    }
                    nextPositions.add(nextPoint)
                }
            }
            currentPositions = nextPositions
            nextPositions = mutableSetOf()
        }
        return currentPositions.size
    }

    // The map is 131 x 131
    // The start point is at (65, 65)
    // 26501365 = 65 + 202300 * 131.
    // So we can do 202300 steps of 131 and 65 steps of 65

    fun partTwo(data: String, stepsToDo: Int): Long {
        val lines = data.lines().toTypedArray()
        val (obstacles, start) = extractData(lines)

        val maxX = lines[0].length
        val maxY = lines.size

        var currentPositions = mutableSetOf<Pair<Int, Int>>()
        currentPositions.add(start)

        var lastCycleCount = 0L
        var lastCountIncrease = 0L
        var lastDiffIncreasePerCycle = 0L
        var step = 0

        var nextPositions = mutableSetOf<Pair<Int, Int>>()
        for (index in 1..stepsToDo) {
            for (point in currentPositions) {
                for (nextPoint in point.getAdjacentPoints()) {
                    val x = (nextPoint.first % maxX + maxX) % maxX
                    val y = (nextPoint.second % maxY + maxY) % maxY
                    if (obstacles.contains(Pair(x, y))) {
                        continue
                    }
                    nextPositions.add(nextPoint)
                }
            }
            currentPositions = nextPositions
            nextPositions = mutableSetOf()

            if(index % 10 == 0)
                println("GFG.Data($index,${currentPositions.size}),")

            // We are looking for a cycle
            if (index % 131 == 65) {
                val countIncrease = currentPositions.size - lastCycleCount
                val increaseDifference = countIncrease - lastCountIncrease
                val increaseDifferenceDifference = increaseDifference - lastDiffIncreasePerCycle
                lastCycleCount = currentPositions.size.toLong()
                lastCountIncrease = countIncrease
                lastDiffIncreasePerCycle = increaseDifference
                // If the difference between the difference of the increase is 0, we have found the cycle
                if (increaseDifferenceDifference == 0L) {
                    step = index
                    break
                }
            }
        }

        println("step: $step, lastCycleCount: $lastCycleCount, lastCountIncrease: $lastCountIncrease, lastDiffIncreasePerCycle: $lastDiffIncreasePerCycle")

        // Repeat the last cycle until we reach the number of steps to take (26501365)
        while (step < stepsToDo) {
            lastCountIncrease += lastDiffIncreasePerCycle
            lastCycleCount += lastCountIncrease
            step += 131
        }

        return lastCycleCount
    }

    // Extract the data from the input : we just need the obstacles and the start point
    private fun extractData(lines: Array<String>): Pair<HashSet<Pair<Int, Int>>, Pair<Int, Int>> {
        val obstacles = HashSet<Pair<Int, Int>>()
        var start: Pair<Int, Int> = Pair(0, 0)
        for (y in lines.indices) {
            val line = lines[y]
            for (x in line.indices) {
                val c = line[x]
                if (c == '#') {
                    obstacles.add(Pair(x, y))
                } else if (c == 'S') {
                    start = Pair(x, y)
                }
            }
        }
        return Pair(obstacles, start)
    }

    //
    private fun Pair<Int, Int>.getAdjacentPoints() =
        listOf(Pair(first - 1, second), Pair(first + 1, second), Pair(first, second - 1), Pair(first, second + 1))
}




