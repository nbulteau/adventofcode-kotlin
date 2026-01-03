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
        repeat(stepsToDo) {
            for (point in currentPositions) {
                for (nextPoint in point.getAdjacentPoints()) {
                    if (nextPoint.first !in 0..maxX || nextPoint.second < 0 || nextPoint.second > maxY) {
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

        // The map is a square
        val width = lines.size

        fun Pair<Int, Int>.floorMod() = Pair(Math.floorMod(first, width), Math.floorMod(second, width))

        val visited = mutableSetOf<Pair<Int, Int>>()

        var frontier = mutableSetOf<Pair<Int, Int>>()
        frontier.add(start)

        var count = 0L
        var countOther = 0L

        val frontiers = Array(width) { 0 }
        val delta1 = Array(width) { 0 }
        val delta2 = Array(width) { 0 }

        var step = 0
        while (step < stepsToDo) {
            val newFrontier = mutableSetOf<Pair<Int, Int>>()

            // For each point in the frontier, compute the adjacent points
            frontier.forEach { point ->
                point.getAdjacentPoints().forEach { neighbour ->
                    if (neighbour.floorMod() !in obstacles) {
                        // We have not visited this point yet : add it to the new frontier
                        if (visited.add(neighbour)) {
                            newFrontier.add(neighbour)
                        }
                    }
                }
            }
            count = (countOther + newFrontier.size).also { countOther = count }

            // compute the deltas
            val index = step % width
            if (step >= width) {
                val delta = newFrontier.size - frontiers[index]
                delta2[index] = delta - delta1[index]
                delta1[index] = delta
            }
            frontiers[index] = newFrontier.size

            frontier = newFrontier
            step++

            println("step: $step, count: $count, countOther: $countOther")

            // iterate until the deltas are stable and we have done at least 2 cycles
            if (step >= 2 * width && delta2.all { it == 0 }) {
                // We found the cycle
                break
            }
        }


        // interpolate the remaining steps
        (step..<stepsToDo).forEach { currentStep ->
            (currentStep % width).let { index ->
                frontiers[index] += delta1[index]
                count = (countOther + frontiers[index]).also { countOther = count }
            }
        }

        return count
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




