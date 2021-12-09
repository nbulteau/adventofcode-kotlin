package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2021/day/9
@OptIn(ExperimentalTime::class)
fun main() {

    val training = readFileDirectlyAsText("/year2021/day09/training.txt")
    val data = readFileDirectlyAsText("/year2021/day09/data.txt")

    val inputs = data.split("\n")
    val lines = inputs.map { line -> line.toList().map { point -> point.toString().toInt() } }

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day09().partOne(lines) })

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day09().partTwo(lines) })
}

private class Day09 {

    data class Point(val x: Int, val y: Int)

    data class Grid(val lines: List<List<Int>>) {

        private fun adjacentLocations(x: Int, y: Int): List<Point> {
            val adjacentLocations = mutableListOf<Point>()
            // up
            if (y > 0) {
                adjacentLocations.add(Point(x, y - 1))
            }
            // down
            if (y < lines.size - 1) {
                adjacentLocations.add(Point(x, y + 1))
            }
            // left
            if (x > 0) {
                adjacentLocations.add(Point(x - 1, y))
            }
            // right
            if (x < lines[y].size - 1) {
                adjacentLocations.add(Point(x + 1, y))
            }
            return adjacentLocations
        }

        fun findLowPoints(): List<Point> {
            val lowPoints = mutableListOf<Point>()
            for (y in lines.indices) {
                for (x in lines[y].indices) {
                    val value = value(x, y)
                    val adjacentLocations = adjacentLocations(x, y)

                    if (adjacentLocations.map { value(it.x, it.y) }.all { it > value }) {
                        lowPoints.add(Point(x, y))
                    }
                }
            }
            return lowPoints
        }

        fun findBasins(): List<Set<Point>> {
            val basins = mutableListOf<Set<Point>>()
            val lowPoints = findLowPoints()
            for (lowPoint in lowPoints) {
                val basin = mutableSetOf<Point>()
                recurseFindBasins(mutableSetOf(lowPoint), basin)
                basins.add(basin)
            }

            return basins
        }

        fun recurseFindBasins(points: MutableSet<Point>, basin: MutableSet<Point>) {
            if (points.isEmpty()) {
                return
            } else {
                val lowPoint = points.first()
                basin.add(lowPoint)
                points.remove(lowPoint)
                points.addAll(
                    adjacentLocations(lowPoint.x, lowPoint.y)
                        .filter { value(it.x, it.y) < 9 } // Locations of height 9 do not count as being in any basin
                        .filter { !basin.contains(it) }
                )
                recurseFindBasins(points, basin)
            }
        }

        fun value(x: Int, y: Int) = lines[y][x]
    }

    fun partOne(lines: List<List<Int>>): Int {

        val grid = Grid(lines)
        val lowPoints = grid.findLowPoints()

        return lowPoints.map { grid.value(it.x, it.y) }.sumOf { it + 1 }
    }

    fun partTwo(lines: List<List<Int>>): Int {

        val grid = Grid(lines)
        val basins = grid.findBasins()

        return basins.map { it.size }.sortedByDescending { it }.subList(0, 3).fold(1) { total, next -> total * next }
    }

}