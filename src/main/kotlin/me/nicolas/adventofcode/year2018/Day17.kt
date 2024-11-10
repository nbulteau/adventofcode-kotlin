package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.*


// WIP : Day 17: Reservoir Research

// https://adventofcode.com/2018/day/17
fun main() {
    val data = readFileDirectlyAsText("/year2018/day17/data.txt")
    val day = Day17(2018, 17, "Reservoir Research")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day17(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private val fountain: Point = Point(500, 0)

    fun partOne(data: String): Int {
        val grid = createMap(data, fountain)

        // Debug : Print the grid
        grid.forEach { row ->
            row.forEach { print(it) }
            println()
        }

        return 42
    }

    fun partTwo(data: String): Int {
        return 42
    }

    private val nonDigits = "[xy=,]".toRegex()

    private fun String.extractDigits(): List<Int> {
        return this.replace(nonDigits, "").split(" ", "..").map { it.toInt() }
    }

    // Create a map of the area with clay spots and the fountain
    private fun createMap(data: String, fountain: Point): Array<CharArray> {
        val spots = data.lines().flatMap { line ->
            val digits = line.extractDigits()
            // If the line starts with "y", the spots are on the y axis
            if (line.startsWith("y")) {
                (digits[1]..digits[2]).map { Point(it, digits[0]) }
            } else {
                (digits[1]..digits[2]).map { Point(digits[0], it) }
            }
        }

        val maxX = spots.maxBy { it.x }.x
        val maxY = spots.maxBy { it.y }.y

        // Initialize the grid
        val grid: Array<CharArray> = (0..maxY).map {
            // Add 2 to maxX to have a margin on the left and right of the grid
            CharArray(maxX + 2).apply { fill('.') }
        }.toTypedArray()

        // Add the fountain
        grid[fountain.y][fountain.x] = '+'

        // Add all clay spots to the grid
        spots.forEach { spot ->
            grid[spot.y][spot.x] = '#'
        }

        return grid
    }
}

