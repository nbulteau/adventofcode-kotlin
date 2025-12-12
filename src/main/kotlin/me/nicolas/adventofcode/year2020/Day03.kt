package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 03: Toboggan Trajectory ---
// https://adventofcode.com/2020/day/3
fun main() {
    val data = readFileDirectlyAsText("/year2020/day03/data.txt")
    val day = Day03(2020, 3, "Toboggan Trajectory")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day03(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        val maze = data.split("\n").filter { it.isNotEmpty() }
        return treesCount(maze, 1, 3)
    }

    fun partTwo(data: String): Long {
        val maze = data.split("\n").filter { it.isNotEmpty() }
        return treesCount(maze, 1, 1) *
                treesCount(maze, 1, 3) *
                treesCount(maze, 1, 5) *
                treesCount(maze, 1, 7) *
                treesCount(maze, 2, 1)
    }

    private fun treesCount(maze: List<String>, vStep: Int, hStep: Int): Long {
        val width = maze[0].length
        val height = maze.size
        var x = 0
        var y = 0
        var count = 0L

        while (y < height) {
            if (maze[y][x] == '#') {
                count++
            }
            x = (x + hStep) % width
            y += vStep
        }
        return count
    }
}
