package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.*
import java.util.LinkedList

// --- Day 18: RAM Run ---
// https://adventofcode.com/2024/day/18
fun main() {
    val data = readFileDirectlyAsText("/year2024/day18/data.txt")
    val day = Day18(2024, 18)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

/**
 * The code simulates bytes falling onto a grid and uses a breadth-first search (BFS) algorithm to find the shortest path from the top-left corner to the bottom-right corner.
 *
 * Part One:
 * - The partOne function initializes a grid and parses the input data to get the coordinates of the bytes.
 * - It simulates the first nbBytes bytes falling onto the grid by marking the corresponding coordinates as corrupted ('#').
 * - It then uses the bfs function to find the shortest path from the top-left corner to the bottom-right corner and returns the number of steps.
 *
 * Part Two:
 * - The partTwo function also initializes the grid and parses the input data.
 * - It simulates bytes falling onto the grid one by one, checking after each byte if the path to the exit is still reachable using the bfs function.
 * - If the path is blocked, it returns the coordinates of the byte that caused the blockage.
 *
 * BFS Function:
 * - The bfs function performs a breadth-first search to find the shortest path from the start to the end point.
 * - It uses a queue to explore each position and its neighbors, keeping track of the number of steps taken to reach each position.
 * - If the end point is reached, it returns the number of steps; otherwise, it returns -1 if no path is found.
 */
class Day18(year: Int, day: Int, title: String = "", val gridSize: Int = 71, val nbBytes: Int = 1024) :
    AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val grid = SimpleGrid(gridSize)

        val coordinates = data.lines().map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Point(x, y)
        }

        // Simulate the first nbBytes bytes falling onto the grid
        coordinates.take(nbBytes).forEach { coord ->
            grid[Point(coord.y, coord.x)] = '#'
        }

        // BFS to find the shortest path
        return bfs(grid, Point(0, 0), Point(gridSize - 1, gridSize - 1))
    }

    fun partTwo(data: String): String {
        val coordinates = data.lines().map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Point(x, y)
        }

        val grid = SimpleGrid(gridSize)

        var cutSize = nbBytes

        coordinates.take(cutSize).forEach { coord ->
            grid[Point(coord.y, coord.x)] = '#'
        }

        while (true) {
            val corrupted = coordinates[cutSize - 1]
            grid[Point(corrupted.y, corrupted.x)] = '#'

            // BFS to find the shortest path
            val steps = bfs(grid, Point(0, 0), Point(gridSize - 1, gridSize - 1))
            if (steps == -1) {
                break
            }

            cutSize++
        }

        val blockingCoordinate = coordinates[cutSize - 1]

        return "${blockingCoordinate.x},${blockingCoordinate.y}"
    }

    private fun bfs(grid: SimpleGrid<Char>, start: Point, end: Point): Int {
        val queue = LinkedList<Pair<Point, Int>>()
        val visited = mutableSetOf<Point>()

        queue.add(Pair(start, 0))
        visited.add(start)

        while (queue.isNotEmpty()) {
            val (current, steps) = queue.poll()

            if (current == end) {
                return steps
            }

            for (next in current.cardinalNeighbors()) {
                if (grid.contains(next) && next !in visited && grid[next] != '#') {
                    queue.add(Pair(next, steps + 1))
                    visited.add(next)
                }
            }
        }

        return -1
    }
}