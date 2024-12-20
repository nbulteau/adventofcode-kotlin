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

    private val Point.manhattanDistance get() = abs(x) + abs(y)
}