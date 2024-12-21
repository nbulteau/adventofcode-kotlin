package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.*
import java.util.*
import kotlin.math.abs

// --- Day 20: ---
// https://adventofcode.com/2024/day/20
fun main() {
    val data = readFileDirectlyAsText("/year2024/day20/data.txt")
    val day = Day20(2024, 20)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

/**
A program must navigate a racetrack represented by a grid, starting at 'S' and ending at 'E'.
The program can move up, down, left, or right, and it can cheat by passing through walls for up to 2 picoseconds in Part One and up to 20 picoseconds in Part Two.
The goal is to find the shortest path from 'S' to 'E' and calculate the number of cheats that save a certain amount of time.

Part One and Part Two Methods:
- partOne and partTwo methods take the input data and a threshold value to calculate the number of cheats that save at least the given threshold of picoseconds.
- Both methods use a breadth-first search (BFS) algorithm to find the shortest path from 'S' to 'E'.

Breadth-First Search (BFS):
- The bfs method performs a BFS to find the shortest path from the start to the end position.
- It uses a queue to explore each position and its neighbors, keeping track of the number of steps taken to reach each position.
- The BFS ensures that the program does not move into walls ('#').

Cheat Calculation:
- The partOne method calculates the number of cheats that save at least 100 picoseconds by considering cheats that last up to 2 picoseconds.
- The partTwo method extends this calculation to consider cheats that last up to 20 picoseconds.
- Both methods use a set of valid offsets to determine the positions that can be reached by cheating.

Helper Methods:
- manhattanDistance calculate the Manhattan distance from the origin (0,0) to the point.
 */

class Day20(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String, threshold: Int = 100): Int {
        val map = SimpleGrid.of(data)
        val start = map.findOne { it == 'S' }
        val end = map.findOne { it == 'E' }
        val path = map.bfs(start, end)

        // ..x..
        // .xxx.
        // xxxxx
        // .xxx.
        // ..x..
        val validOffsets = (-2..2).flatMap { x ->
            (-2..2).map { y ->
                Point(x, y)
            }
        }.filter { it.manhattanDistance <= 2 }

        return path.entries.sumOf { (pos, length) ->
            validOffsets.count {
                path.getOrDefault(pos + it, 0) - length >= (threshold + 2)
            }
        }
    }

    fun partTwo(data: String, threshold: Int = 100): Int {
        val map = SimpleGrid.of(data)
        val start = map.findOne { it == 'S' }
        val end = map.findOne { it == 'E' }
        val path = map.bfs(start, end)

        val validOffsets = (-20..20).flatMap { x ->
            (-20..20).map { y ->
                Point(x, y)
            }
        }.filter { it.manhattanDistance <= 20 }

        return path.entries.sumOf { (pos, length) ->
            validOffsets.count {
                path.getOrDefault(pos + it, 0) - length >= (threshold + it.manhattanDistance)
            }
        }
    }

    private fun SimpleGrid<Char>.bfs(
        start: Point,
        end: Point,
    ): Map<Point, Int> {
        val queue = LinkedList<Pair<Point, Int>>()
        val visited = mutableMapOf<Point, Int>()

        queue.add(Pair(start, 0))
        visited[start] = 0

        while (queue.isNotEmpty()) {
            val (current, steps) = queue.poll()

            if (current == end) {
                break
            }

            for (neighbor in current.cardinalNeighbors()) {
                if (neighbor in this && neighbor !in visited && this[neighbor] != '#') {
                    queue.add(Pair(neighbor, steps + 1))
                    visited[neighbor] = steps + 1
                }
            }
        }

        return visited
    }

    // Calculate the Manhattan distance from the origin (0,0) to the point.
    private val Point.manhattanDistance get() = abs(x) + abs(y)
}