package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 10: ---
// https://adventofcode.com/2024/day/10
fun main() {
    val data = readFileDirectlyAsText("/year2024/day10/data.txt")
    val day = Day10(2024, 10)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day10(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String) = processMap(data) { map, x, y -> dfsPartOne(map, x, y) }

    fun partTwo(data: String) = processMap(data) { map, x, y -> dfsPartTwo(map, x, y) }

    private fun processMap(data: String, dfsFunction: (List<List<Int>>, Int, Int) -> Int): Int {
        val map = data.lines().map { string -> string.trim().map { digit -> digit.digitToInt() } }
        val trailheadScores = mutableListOf<Int>()

        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == 0) {
                    val score = dfsFunction(map, x, y)
                    trailheadScores.add(score)
                }
            }
        }

        return trailheadScores.sum()
    }

    // Performs a Depth-First Search (DFS) to count the number of cells with a height of 9 that can be reached from a given starting point.
    // Each step must move to an adjacent cell with a height exactly one greater than the current cell.
    private fun dfsPartOne(map: List<List<Int>>, startX: Int, startY: Int): Int {
        val rows = map[0].size
        val columns = map.size
        val visited = Array(columns) { BooleanArray(rows) }
        var count = 0

        fun dfsHelper(x: Int, y: Int) {
            if (x < 0 || x >= rows || y < 0 || y >= columns || visited[y][x]) return

            visited[y][x] = true

            val currentHeight = map[y][x]

            // Explore up
            if (y > 0 && map[y - 1][x] == currentHeight + 1) {
                dfsHelper(x, y - 1)
            }
            // Explore down
            if (y < columns - 1 && map[y + 1][x] == currentHeight + 1) {
                dfsHelper(x, y + 1)
            }
            // Explore left
            if (x > 0 && map[y][x - 1] == currentHeight + 1) {
                dfsHelper(x - 1, y)
            }
            // Explore right
            if (x < rows - 1 && map[y][x + 1] == currentHeight + 1) {
                dfsHelper(x + 1, y)
            }

            // After exploring all possible paths from this point
            if (currentHeight == 9) {
                count++
            }
        }

        dfsHelper(startX, startY)

        return count
    }

    // Performs a Depth-First Search (DFS) to find all unique paths in a grid that start from a given point
    // Each step must move to an adjacent cell with a height exactly one greater than the current cell.
    // Returns the number of such unique paths that end at a cell with a height of 9.
    fun dfsPartTwo(map: List<List<Int>>, startX: Int, startY: Int): Int {
        val paths = mutableSetOf<List<Pair<Int, Int>>>()
        val rows = map[0].size
        val columns = map.size

        fun dfsHelper(x: Int, y: Int, path: MutableList<Pair<Int, Int>>, visited: Array<BooleanArray>) {
            if (x < 0 || x >= rows || y < 0 || y >= columns) return

            visited[y][x] = true
            path.add(Pair(x, y))

            val currentHeight = map[y][x]

            // Explore up
            if (y > 0 && map[y - 1][x] == currentHeight + 1) {
                dfsHelper(x, y - 1, path, visited)
            }
            // Explore down
            if (y < columns - 1 && map[y + 1][x] == currentHeight + 1) {
                dfsHelper(x, y + 1, path, visited)
            }
            // Explore left
            if (x > 0 && map[y][x - 1] == currentHeight + 1) {
                dfsHelper(x - 1, y, path, visited)
            }
            // Explore right
            if (x < rows - 1 && map[y][x + 1] == currentHeight + 1) {
                dfsHelper(x + 1, y, path, visited)
            }

            // After exploring all possible paths from this point, add the path to the set
            if (currentHeight == 9) {
                paths.add(path.toList())
            }
        }

        val initialVisited = Array(columns) { BooleanArray(rows) }
        dfsHelper(startX, startY, mutableListOf(), initialVisited)

        return paths.size
    }
}