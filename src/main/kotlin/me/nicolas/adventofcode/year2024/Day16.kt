package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.Point
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.PriorityQueue

// --- Day 16: Reindeer Maze ---
// https://adventofcode.com/2024/day/16
fun main() {
    val data = readFileDirectlyAsText("/year2024/day16/data.txt")
    val day = Day16(2024, 16)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day16(year: Int, day: Int, title: String = "Reindeer Maze") : AdventOfCodeDay(year, day, title) {

    // Tile class to represent a point in the maze with a direction
    private data class Tile(val point: Point, val dx: Int, val dy: Int) {

        fun neighbors(reindeerMaze: List<CharArray>, distance: Int): List<Pair<Tile, Int>> {

            val turnLeft = Tile(point, dy, -dx)
            val turnRight = Tile(point, -dy, dx)

            val forward = Tile(Point(point.x + dx, point.y + dy), dx, dy)
            if (reindeerMaze[forward.point.y][forward.point.x] == '#') {
                return listOf(turnLeft to distance + 1000, turnRight to distance + 1000)
            }

            return listOf(turnLeft to distance + 1000, turnRight to distance + 1000, forward to distance + 1)
        }
    }

    private fun List<CharArray>.findStart(): Tile {
        for (y in this.indices) {
            for (x in this[0].indices) {
                if (this[y][x] == 'S') {
                    return Tile(Point(x, y), 1, 0)
                }
            }
        }
        throw IllegalStateException("No start found")
    }

    private fun List<CharArray>.findEnd(): Point {
        for (y in this.indices) {
            for (x in this[0].indices) {
                if (this[y][x] == 'E') {
                    return Point(x, y)
                }
            }
        }
        throw IllegalStateException("No end found")
    }

    // Recursive function to paint the path from the end to the start
    private fun paintPaths(visited: MutableSet<Tile>, map: Map<Tile, Pair<Int, List<Tile>>>, current: Tile): Set<Tile> {
        if (current in visited) return visited
        visited.add(current)
        for (node in map[current]!!.second) {
            paintPaths(visited, map, node)
        }
        return visited
    }

    fun partOne(data: String): Int {
        val reindeerMaze = data.lines().map {
            it.toCharArray()
        }

        // Pair of Tile and distance to reach it : priority queue to get the closest tile to the end first
        val unvisitedSet = PriorityQueue<Pair<Tile, Int>> { tile1, tile2 -> tile1.second - tile2.second }

        val start = reindeerMaze.findStart()
        val visited = mutableSetOf(start)
        unvisitedSet.add(start to 0)
        val end = reindeerMaze.findEnd()

        while (unvisitedSet.isNotEmpty()) {
            val tile = unvisitedSet.remove()
            val neighbors = tile.first.neighbors(reindeerMaze, tile.second)
            for (neighbor in neighbors) {
                if (neighbor.first in visited) {
                    continue
                }
                visited.add(neighbor.first)
                unvisitedSet.add(neighbor)
                if (end == neighbor.first.point) {
                    return neighbor.second
                }
            }
        }

        throw IllegalStateException("No end found")
    }



    fun partTwo(data: String): Int {
        val reindeerMaze = data.lines().map {
            it.toCharArray()
        }
        val unvisitedSet = PriorityQueue<Pair<Tile, Int>> { tile1, tile2 -> tile1.second - tile2.second }
        val start = reindeerMaze.findStart()
        val visited = mutableMapOf(start to (0 to mutableListOf<Tile>()))
        unvisitedSet.add(start to 0)
        val end = reindeerMaze.findEnd()

        while (unvisitedSet.isNotEmpty()) {
            val node = unvisitedSet.remove()
            val neighbors = node.first.neighbors(reindeerMaze, node.second)
            for (neighbor in neighbors) {
                val first = if (end == neighbor.first.point) {
                    Tile(neighbor.first.point, 0, 0) // This is the end
                } else {
                    neighbor.first
                }
                if (first in visited) {
                    val seen = visited[first]!!
                    if (neighbor.second == seen.first) {
                        seen.second.add(node.first)
                    }
                    continue
                }
                visited[first] = neighbor.second to mutableListOf(node.first)
                if (end != first) { // Not the end
                    unvisitedSet.add(neighbor)
                }
            }
        }

        val allTiles = paintPaths(mutableSetOf(), visited, Tile(end, 0, 0))
        for (tile in allTiles) {
            reindeerMaze[tile.point.y][tile.point.x] = 'O'
        }

        return reindeerMaze.sumOf { line -> line.count { it == 'O' } }
    }
}
