package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

//--- Day 6: Probably a Fire Hazard ---
// https://adventofcode.com/2015/day/6
fun main() {
    val data = readFileDirectlyAsText("/year2015/day06/data.txt")
    val day = Day06(2015, 6, "Probably a Fire Hazard ")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day06(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val grid = Array(1000) { Array(1000) { 0 } }

        val lines = data.split("\n")
        lines.forEach { line ->
            when {
                line.startsWith("turn on") -> {
                    val (start, end) = extractCoordinates(line.removePrefix("turn on "))
                    for (i in start.first..end.first) {
                        for (j in start.second..end.second) {
                            grid[i][j] = 1
                        }
                    }
                }

                line.startsWith("turn off") -> {
                    val (start, end) = extractCoordinates(line.removePrefix("turn off "))
                    for (i in start.first..end.first) {
                        for (j in start.second..end.second) {
                            grid[i][j] = 0
                        }
                    }
                }

                line.startsWith("toggle") -> {
                    val (start, end) = extractCoordinates(line.removePrefix("toggle "))
                    for (i in start.first..end.first) {
                        for (j in start.second..end.second) {
                            grid[i][j] = if (grid[i][j] == 1) 0 else 1
                        }
                    }
                }
            }
        }

        return grid.sumOf(Array<Int>::sum)
    }

    fun partTwo(data: String): Int {
        val grid = Array(1000) { Array(1000) { 0 } }

        val lines = data.split("\n")
        lines.forEach { line ->
            when {
                line.startsWith("turn on") -> {
                    val (start, end) = extractCoordinates(line.removePrefix("turn on "))
                    for (i in start.first..end.first) {
                        for (j in start.second..end.second) {
                            grid[i][j]++
                        }
                    }
                }

                line.startsWith("turn off") -> {
                    val (start, end) = extractCoordinates(line.removePrefix("turn off "))
                    for (i in start.first..end.first) {
                        for (j in start.second..end.second) {
                            grid[i][j] = if (grid[i][j] > 0) grid[i][j] - 1 else 0
                        }
                    }
                }

                line.startsWith("toggle") -> {
                    val (start, end) = extractCoordinates(line.removePrefix("toggle "))
                    for (i in start.first..end.first) {
                        for (j in start.second..end.second) {
                            grid[i][j] += 2
                        }
                    }
                }
            }
        }

        return grid.sumOf(Array<Int>::sum)
    }

    private fun extractCoordinates(input: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val parts = input.split(" through ")
        val start = parts[0].trim().split(",").map { it.toInt() }
        val end = parts[1].trim().split(",").map { it.toInt() }

        return Pair(Pair(start[0], start[1]), Pair(end[0], end[1]))
    }
}