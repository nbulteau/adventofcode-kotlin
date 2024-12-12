package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.*

// --- Day 12: ---
// https://adventofcode.com/2024/day/12
fun main() {
    val data = readFileDirectlyAsText("/year2024/day12/data.txt")
    val day = Day12(2024, 12)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day12(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    private enum class Side { UP, DOWN, LEFT, RIGHT }

    private val sides = mapOf(
        Side.DOWN to Point(1, 0),
        Side.UP to Point(-1, 0),
        Side.RIGHT to Point(0, 1),
        Side.LEFT to Point(0, -1)
    )

    private fun countPerimeter(perimeter: Map<Side, List<Point>>) =
        perimeter.values.sumOf { points -> points.count() }

    private fun countSides(perimeter: Map<Side, List<Point>>) =
        perimeter.map { (side, points) ->

            val sorted = points.sortedWith { pointA, pointB ->
                if (pointA.x == pointB.x) {
                    // If the x-coordinates are equal, the points are sorted based on their y-coordinates:
                    pointA.y - pointB.y
                } else {
                    // Otherwise, the points are sorted based on their x-coordinates:
                    pointA.x - pointB.x
                }
            }

            // Group the sorted points by their x or y coordinates depending on the side
            when (side) {
                Side.UP, Side.DOWN -> sorted.groupBy { point -> point.x }.values.sumOf { points ->
                    points.zipWithNext().count { (pointA, pointB) ->
                        pointB.y - pointA.y != 1
                    } + 1
                }

                Side.LEFT, Side.RIGHT -> sorted.groupBy { point -> point.y }.values.sumOf { points ->
                    points.zipWithNext().count { (pointA, pointB) ->
                        pointB.x - pointA.x != 1
                    } + 1
                }
            }
        }.sum()


    private fun Point.isInMap(map: List<CharArray>) = x in map.indices && y in map[0].indices

    private fun solve(data: String, process: (Map<Side, List<Point>>) -> Int): Int {

        val map = data.lines().map { line -> line.toCharArray() }

        val visited = mutableSetOf<Point>()

        fun bfs(start: Point): Int {
            val region = mutableMapOf<Side, MutableList<Point>>(
                Side.DOWN to mutableListOf(),
                Side.UP to mutableListOf(),
                Side.RIGHT to mutableListOf(),
                Side.LEFT to mutableListOf()
            )

            val targetChar = map[start.x][start.y]
            val area = mutableSetOf<Point>()

            val toBeVisited = mutableListOf(start)

            while (toBeVisited.isNotEmpty()) {
                val current = toBeVisited.removeFirst()

                if (current in area) {
                    continue
                }
                area.add(current)

                for ((sideName, side) in sides) {
                    val targetPoint = current + side

                    if (!targetPoint.isInMap(map) || map[targetPoint.x][targetPoint.y] != targetChar) {
                        region[sideName]!!.add(current)
                        continue
                    }

                    toBeVisited.add(targetPoint)
                }
            }

            visited.addAll(area)

            return area.size * process(region)
        }


        return map.indices.sumOf { i ->
            map[i].indices.sumOf { j ->
                if (Point(i, j) in visited) 0 else bfs(Point(i, j))
            }
        }
    }

    fun partOne(data: String): Int {
        return solve(data) { countPerimeter(it) }
    }

    fun partTwo(data: String): Int {
        return solve(data) { countSides(it) }
    }
}
