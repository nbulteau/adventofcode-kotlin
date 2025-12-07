package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.*

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
// https://adventofcode.com/2021/day/15
fun main() {
    val data = readFileDirectlyAsText("/year2021/day15/data.txt")
    val day = Day15(2021, 15)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day15(year: Int, day: Int, title: String = "Chiton") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val map = arrayOfIntArrays(data.split("\n"))

        val caveMap = CaveMap(map)

        return caveMap.dijkstra()
    }

    fun partTwo(data: String): Int {
        val map = arrayOfIntArrays(data.split("\n"))
        val bigMap = map.enlarge5Times()

        val caveMap = CaveMap(bigMap)

        return caveMap.dijkstra()
    }

    private fun arrayOfIntArrays(lines: List<String>): Array<IntArray> {
        val riskLevels = lines.map { row ->
            row.map { risk ->
                risk.digitToInt()
            }.toIntArray()
        }.toTypedArray()

        return riskLevels
    }

    private class CaveMap(private val squares: Array<IntArray>) {

        private val destination = Point(squares.first().lastIndex, squares.lastIndex)

        private data class Point(val x: Int, val y: Int) {
            fun adjacentPoints(): List<Point> =
                listOf(Point(x, y + 1), Point(x, y - 1), Point(x + 1, y), Point(x - 1, y))
        }

        // totalRisk it took to get to the point
        private class Path(val point: Point, val totalRisk: Int) : Comparable<Path> {
            // Returns zero if this object is equal to the specified other object,
            // a negative number if it's less than other, or a positive number if it's greater than other.
            override fun compareTo(other: Path) = this.totalRisk - other.totalRisk
        }

        fun dijkstra(): Int {
            // Path with a lower totalRisk will always come before a Path with a higher totalRisk (Comparable)
            val toBeEvaluated = PriorityQueue<Path>().apply { add(Path(Point(0, 0), 0)) }
            val visited = mutableSetOf<Point>()

            while (toBeEvaluated.isNotEmpty()) {
                val currentPath = toBeEvaluated.poll()
                if (currentPath.point == destination) {
                    return currentPath.totalRisk
                }
                if (currentPath.point !in visited) {
                    visited.add(currentPath.point)
                    adjacentLocations(currentPath.point)
                        .forEach { point ->
                            toBeEvaluated.offer(Path(point, currentPath.totalRisk + this[point]))
                        }
                }
            }
            error("No path found !!")
        }

        private fun adjacentLocations(point: Point): List<Point> {
            val adjacentLocations = mutableListOf<Point>()
            // up
            if (point.y > 0) {
                adjacentLocations.add(Point(point.x, point.y - 1))
            }
            // down
            if (point.y < squares.size - 1) {
                adjacentLocations.add(Point(point.x, point.y + 1))
            }
            // left
            if (point.x > 0) {
                adjacentLocations.add(Point(point.x - 1, point.y))
            }
            // right
            if (point.x < squares[point.y].size - 1) {
                adjacentLocations.add(Point(point.x + 1, point.y))
            }
            return adjacentLocations
        }

        private operator fun get(point: Point) = squares[point.y][point.x]
    }

    private fun Array<IntArray>.enlarge5Times(): Array<IntArray> {
        val maxRow = this.first().size
        val maxCol = this.size

        val bigMap = Array(maxRow * 5) { IntArray(maxCol * 5) { 0 } }
        for (x in 0..4) {
            for (y in 0..4) {
                this.forEachIndexed { indexY, intArray ->
                    intArray.forEachIndexed { indexX, riskLevel ->
                        val newRiskLevel = riskLevel + x + y
                        bigMap[maxCol * y + indexY][maxRow * x + indexX] =
                            newRiskLevel.takeIf { it <= 9 } ?: (newRiskLevel - 9)
                    }
                }
            }
        }
        return bigMap
    }
}