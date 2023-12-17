package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*
import kotlin.math.min

fun main() {
    val data = readFileDirectlyAsText("/year2023/day17/data.txt")
    val day = Day17(2023, 17, "Clumsy Crucible")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day17(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val map = Grid.of(data)

        val neighbours: NeighbourFunction<PointInDirection> =
            { point -> point.neighboursForCrucibles().filter { (point) -> map[point] != null } }
        // End when we reach the bottom right corner
        val endPoint = Pair(map.maxX, map.maxY)
        val end: EndFunction<PointInDirection> = { (point, _) -> point == endPoint }

        return solve(map, neighbours, end)
    }

    fun partTwo(data: String): Int {
        val map = Grid.of(data)

        //
        val neighbours: NeighbourFunction<PointInDirection> =
            { point -> point.neighboursForUltraCrucibles().filter { (point) -> map[point] != null } }
        // End when we reach the bottom right corner, and we have a line of 4
        val endPoint = Pair(map.maxX, map.maxY)
        val end: EndFunction<PointInDirection> = { (point, _, line) -> point == endPoint && line >= 4 }

        return solve(map, neighbours, end)
    }

    private fun solve(
        map: Grid<Char>,
        neighbours: NeighbourFunction<PointInDirection>,
        end: EndFunction<PointInDirection>,
    ): Int {
        val cost: CostFunction<PointInDirection> = { _, (point) -> map[point]!!.digitToInt() }
        // Start in the top left corner and going east (right)
        val startEast = PointInDirection(Pair(0, 0), Direction.EAST, 0)
        val eastPath = findShortestPath(startEast, end, neighbours, cost)
        // Start in the top left corner and going south (down)
        val startSouth = PointInDirection(Pair(0, 0), Direction.SOUTH, 0)
        val southPath = findShortestPath(startSouth, end, neighbours, cost)

        return min(eastPath.getScore(), southPath.getScore())
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

        fun nextPoint(point: Pair<Int, Int>): Pair<Int, Int> {
            return Pair(point.first + dx, point.second + dy)
        }

        fun right(): Direction {
            return when (this) {
                NORTH -> EAST
                EAST -> SOUTH
                SOUTH -> WEST
                WEST -> NORTH
            }
        }

        fun left(): Direction {
            return when (this) {
                NORTH -> WEST
                EAST -> NORTH
                SOUTH -> EAST
                WEST -> SOUTH
            }
        }
    }

    private data class PointInDirection(val point: Pair<Int, Int>, val direction: Direction, val line: Int) {
        fun neighboursForCrucibles(): List<PointInDirection> {
            return buildList {
                if (line < 3) {
                    add(PointInDirection(direction.nextPoint(point), direction, line + 1))
                }
                add(PointInDirection(direction.right().nextPoint(point), direction.right(), 1))
                add(PointInDirection(direction.left().nextPoint(point), direction.left(), 1))
            }
        }

        fun neighboursForUltraCrucibles(): List<PointInDirection> {
            return buildList {
                if (line < 10) {
                    add(PointInDirection(direction.nextPoint(point), direction, line + 1))
                }
                if (line >= 4) {
                    add(PointInDirection(direction.right().nextPoint(point), direction.right(), 1))
                    add(PointInDirection(direction.left().nextPoint(point), direction.left(), 1))
                }
            }
        }
    }
}

