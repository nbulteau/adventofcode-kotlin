package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*

fun main() {
    val data = readFileDirectlyAsText("/year2023/day21/data.txt")
    val day = Day21Slow(2023, 21, "")

    println(37730L + 7456L * 26501365L + 7442L * 26501365L * 26501365L)

    prettyPrintPartOne { day.partOneBis(data, 64) }
    prettyPrintPartOne { day.partOne(data, 64) }
    prettyPrintPartTwo { day.partTwoHelper(data) }
    prettyPrintPartTwo { day.partTwo(data, 26501365) }

}

/**
 * This is a slow implementation of the Day21.kt
 * It uses a BFS algorithm to find all the positions reachable in the given number of steps.
 * It is too slow.
 */
class Day21Slow(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String, stepsToDo: Int): Int {
        val map = Grid.of(data)
        val startPoint = map['S'] ?: throw IllegalStateException("No start point")
        val neighbours: NeighbourFunction<Pair<Int, Int>> = { fromPoint ->
            map.getCardinalNeighbors(fromPoint)
                .filter { point -> map[point] != null }
                .filter { point -> map[point] != '#' }
        }

        return findAllPosition(startPoint, stepsToDo, neighbours).size
    }

    fun partOneBis(data: String, stepsToDo: Int): Int {
        val map = Grid.of(data)
        val startPoint = map['S'] ?: throw IllegalStateException("No start point")

        fun neighbours(fromPoint: Pair<Int, Int>) = map.getCardinalNeighbors(fromPoint)
            .filter { point -> map[point] != null }
            .filter { point -> map[point] != '#' }

        fun reachedGardenPlots(steps: Int): Int {
            return (1..steps).fold(setOf(startPoint)) { current, _ -> current.flatMap { neighbours(it) }.toSet() }.size
        }

        return reachedGardenPlots(steps = stepsToDo)
    }

    fun partTwo(data: String, stepsToDo: Int): Int {
        val map = Grid.of(data)
        val startPoint = map['S'] ?: throw IllegalStateException("No start point")
        val maxX = map.maxX
        val maxY = map.maxY

        val neighbours: NeighbourFunction<Pair<Int, Int>> = { fromPoint ->
            map.getCardinalNeighbors(fromPoint)
                .filter { point ->
                    val x = (point.first % maxX + maxX) % maxX
                    val y = (point.second % maxY + maxY) % maxY
                    val moduloPoint = Pair(x, y)
                    map[moduloPoint] != '#'
                }
        }

        return findAllPosition(startPoint, stepsToDo, neighbours).size
    }

    private fun findAllPosition(
        startPoint: Pair<Int, Int>, // The start point
        stepsToDo: Int,
        neighbours: NeighbourFunction<Pair<Int, Int>>,
    ): MutableSet<Pair<Int, Int>> {
        var toVisit = mutableSetOf(startPoint)
        var steps = 1
        // Loop until we reach the number of steps to do
        while (steps <= stepsToDo) {

            val toVisitNext = mutableSetOf<Pair<Int, Int>>()
            toVisit.forEach { currentVertex ->
                // Add all the neighbours of the current vertex to the queue
                val nextPoints = neighbours(currentVertex)
                // Add the points to the queue
                toVisitNext.addAll(nextPoints)
            }
            toVisit = toVisitNext

            steps++
        }

        return toVisit
    }

    fun partTwoHelper(data: String) {
        val map = Grid.of(data)
        val startPoint = map['S'] ?: throw IllegalStateException("No start point")
        val maxX = map.maxX
        val maxY = map.maxY

        fun neighbours(fromPoint: Pair<Int, Int>) = map.getCardinalNeighbors(fromPoint)
            .filter { point ->
                val x = (point.first % maxX + maxX) % maxX
                val y = (point.second % maxY + maxY) % maxY
                val moduloPoint = Pair(x, y)
                map[moduloPoint] != '#'
            }

        fun reachedGardenPlots(steps: Int): Int {
            return (1..steps).fold(setOf(startPoint)) { current, _ -> current.flatMap { neighbours(it) }.toSet() }.size
        }

        println("reachedGardenPlots(steps = 65) = ${reachedGardenPlots(steps = 65)}")
        println("reachedGardenPlots(steps = 65 + 131) = ${reachedGardenPlots(steps = (65 + 131))}")
        println("reachedGardenPlots(steps = 65 + 131 * 2) = ${reachedGardenPlots(steps = (65 + 131 * 2))}")
    }
}

