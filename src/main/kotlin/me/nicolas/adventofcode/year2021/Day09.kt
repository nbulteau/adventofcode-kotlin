package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2021/day/9
fun main() {
    val data = readFileDirectlyAsText("/year2021/day09/data.txt")
    val day = Day09(2021, 9)
    val inputs = data.split("\n")
    val lines = inputs.map { line -> line.toList().map { point -> point.toString().toInt() } }
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day09(year: Int, day: Int, title: String = "Smoke Basin") : AdventOfCodeDay(year, day, title) {

    fun partOne(lines: List<List<Int>>): Int {

        val grid = Grid(lines)
        val lowPoints = grid.findLowPoints()

        // score
        return lowPoints
            .map { grid.value(it.x, it.y) }
            .sumOf { it + 1 }
    }

    fun partTwo(lines: List<List<Int>>): Int {

        val grid = Grid(lines)
        val basins = grid.findBasins()

        // score
        return basins
            .map { it.size }
            .sorted()
            .takeLast(3)
            .fold(1) { total, next -> total * next }
    }

    data class Point(val x: Int, val y: Int)

    data class Grid(private val lines: List<List<Int>>) {

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
            val lowPoints = findLowPoints()
            return lowPoints.map { lowPoint ->
                val basin = mutableSetOf<Point>()
                recurseFindBasins(mutableSetOf(lowPoint), basin)
                basin
            }
        }

        private fun recurseFindBasins(points: MutableSet<Point>, basin: MutableSet<Point>) {
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
}