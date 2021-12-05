package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2021/day/5
fun main() {

    val training = readFileDirectlyAsText("/year2021/day05/training.txt")
    val data = readFileDirectlyAsText("/year2021/day05/data.txt")

    val input = data.split("\n")

    Day05().solve(1000, input)
}

private class Day05 {

    data class Point(val x: Int, val y: Int)

    data class Line(val start: Point, val end: Point)

    data class Grid(val size: Int) {

        val values: Array<IntArray> = Array(size) { IntArray(size) { 0 } }

        fun addLine(line: Line, includeDiagonalLines: Boolean = false) {
            if (line.start.x == line.end.x) {
                val x = line.start.x
                val start = min(line.start.y, line.end.y)
                val end = max(line.start.y, line.end.y)
                for (y in start..end) {
                    values[y][x] += 1
                }

            } else if (line.start.y == line.end.y) {
                val y = line.start.y
                val start = min(line.start.x, line.end.x)
                val end = max(line.start.x, line.end.x)
                for (x in start..end) {
                    values[y][x] += 1
                }
            } else if (includeDiagonalLines) {
                val rangeX = if (line.start.x < line.end.x) {
                    (line.start.x..line.end.x).map { it }
                } else {
                    (line.start.x downTo line.end.x).map { it }
                }
                val rangeY = if (line.start.y < line.end.y) {
                    (line.start.y..line.end.y).map { it }
                } else {
                    (line.start.y downTo line.end.y).map { it }
                }
                for (index in rangeX.indices) {
                    val x = rangeX[index]
                    val y = rangeY[index]
                    values[y][x] += 1
                }
            }
        }

        fun getNumberOfPointsToAvoid(): Int {
            return values.sumOf { intArray ->
                intArray.count { value -> value >= 2 }
            }
        }

        fun display() {
            values.forEach { row -> println(row.joinToString(separator = ":")) }
        }
    }

    fun solve(gridSize: Int, input: List<String>) {

        val lines = input.map { line ->
            val (start, end) = line.split(" -> ").map { point ->
                val (x, y) = point.split(",")
                Point(x.toInt(), y.toInt())
            }
            Line(start, end)
        }

        println("Part one answer = ${partOne(gridSize, lines)}")

        println("Part one answer = ${partTwo(gridSize, lines)}")
    }


    private fun partOne(gridSize: Int, lines: List<Line>): Int {
        val grid = Grid(gridSize)
        lines.forEach { line ->
            grid.addLine(line, includeDiagonalLines = false)
        }
        return grid.getNumberOfPointsToAvoid()
    }

    private fun partTwo(gridSize: Int, lines: List<Line>): Int {
        val grid = Grid(gridSize)
        lines.forEach { line ->
            grid.addLine(line, includeDiagonalLines = true)
        }
        grid.display()
        return grid.getNumberOfPointsToAvoid()
    }
}