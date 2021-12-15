package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm

// https://adventofcode.com/2021/day/15
@ExperimentalTime
fun main() {

    val training = readFileDirectlyAsText("/year2021/day15/training.txt")
    val data = readFileDirectlyAsText("/year2021/day15/data.txt")

    val lines = data.split("\n")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day15().partOne(lines) })

    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day15().partTwo(lines) })
}

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm

private class Day15 {

    fun partOne(lines: List<String>): Int {
        val map = arrayOfIntArrays(lines)

        val caveMap = CaveMap(map)

        return caveMap.dijkstra()
    }

    fun partTwo(lines: List<String>): Int {
        val map = arrayOfIntArrays(lines)
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