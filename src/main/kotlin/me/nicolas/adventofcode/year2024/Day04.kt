package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 4: Ceres Search ---
// https://adventofcode.com/2024/day/4
fun main() {
    val data = readFileDirectlyAsText("/year2024/day04/data.txt")
    val day = Day04(2024, 4, "Ceres Search")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day04(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {

        val grid = data.lines().map { it.toCharArray() }

        val rows = grid.size
        val columns = grid[0].size

        fun isValid(x: Int, y: Int) = x >= 0 && x < rows && y >= 0 && y < columns

        val word = "XMAS"

        val directions = listOf(
            Pair(0, 1),
            Pair(1, 0),
            Pair(0, -1),
            Pair(-1, 0),
            Pair(-1, -1),
            Pair(-1, 1),
            Pair(1, -1),
            Pair(1, 1)
        )

        fun checkDirection(x: Int, y: Int, dx: Int, dy: Int): Boolean {
            for (i in word.indices) {
                val nextX = x + i * dx
                val nextY = y + i * dy
                if (!isValid(nextX, nextY) || grid[nextX][nextY] != word[i]) {
                    return false
                }
            }
            return true
        }

        var count = 0
        for (x in 0 until rows) {
            for (y in 0 until columns) {
                if (grid[x][y] == word[0]) {
                    for ((dx, dy) in directions) {
                        if (checkDirection(x, y, dx, dy)) {
                            count++
                        }
                    }
                }
            }
        }

        return count
    }

    fun partTwo(data: String): Int {

        val grid = data.lines().map { it.toCharArray() }

        val rows = grid.size
        val columns = grid[0].size

        fun isValid(x: Int, y: Int) = x >= 0 && x < rows && y >= 0 && y < columns

        fun isMS(first: Char, second: Char) = (second == 'M' && first == 'S') || (second == 'S' && first == 'M')

        var count = 0
        for (x in 1 until rows - 1) {
            for (y in 1 until columns - 1) {
                if (grid[x][y] == 'A') {
                    val upLeft = if (isValid(x - 1, y - 1)) grid[x - 1][y - 1] else break
                    val upRight = if (isValid(x - 1, y + 1)) grid[x - 1][y + 1] else break
                    val downLeft = if (isValid(x + 1, y - 1)) grid[x + 1][y - 1] else break
                    val downRight = if (isValid(x + 1, y + 1)) grid[x + 1][y + 1] else break

                    if (isMS(downRight, upLeft) && isMS(downLeft, upRight)) {
                        count++
                    }
                }
            }
        }

        return count
    }
}