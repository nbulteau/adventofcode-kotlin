package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*
import java.util.*

fun main() {
    val data = readFileDirectlyAsText("/year2023/day23/data.txt")
    val day = Day23(2023, 23, "A Long Walk")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day23(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private enum class Direction(val dx: Int, val dy: Int) {
        NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

        fun nextPoint(point: Pair<Int, Int>): Pair<Int, Int> {
            return Pair(point.first + dx, point.second + dy)
        }

        companion object {
            fun of(char: Char): Direction {
                return when (char) {
                    '^' -> NORTH
                    '>' -> EAST
                    'v' -> SOUTH
                    '<' -> WEST
                    else -> throw IllegalArgumentException(char.toString())
                }
            }
        }
    }

    // State of the BFS algorithm (Breadth-First Search) to find the longest route from start to end in the grid map
    private data class State(
        val point: Pair<Int, Int>,
        val steps: Int,
        val visited: Set<Pair<Int, Int>>,
        val direction: Direction? = null,
    )

    fun partOne(data: String): Int {
        val map = Grid.of(data)

        val start = Pair(0, 1)
        val end = Pair(map.maxX, map.maxY - 1)

        // Find the longest route from start to end using a BFS algorithm (Breadth-First Search)
        val stack = Stack<State>()
        stack.add(State(start, 0, setOf()))
        val routes = mutableListOf<Int>()
        while (stack.isNotEmpty()) {
            val (node, steps, visited, direction) = stack.pop()

            map.getCardinalNeighbors(node).forEach { next ->
                val char = map[next]!!

                if (next == end) {
                    routes.add(steps + 1)
                } else if (next !in visited && (direction == null || next == direction.nextPoint(node))) {
                    when (char) {
                        '.' -> stack.add(State(next, steps + 1, visited + node))
                        '<', '>', '^', 'v' -> stack.add(State(next, steps + 1, visited + node, Direction.of(char)))
                    }
                }
            }
        }

        return routes.max()
    }

    fun partTwo(data: String): Int {
        val map = Grid.of(data)

        val start = Pair(0, 1)
        val end = Pair(map.maxX, map.maxY - 1)

        // Find all crossroads (points with more than 2 neighbors) to reduce the search space
        val crossroads = map.indices.filter { point ->
            map[point] != '#' && map.getCardinalNeighbors(point).filter { map[it] != '#' }.size > 2
        } + end + start

        // Find the distance between all crossroads
        val crossroadsDistances = crossroads.associateWith { mutableSetOf<Pair<Pair<Int, Int>, Int>>() }
        for (crossroad in crossroads) {
            val queue = mutableListOf(crossroad to 0)
            val visited = mutableSetOf<Pair<Int, Int>>()
            while (queue.isNotEmpty()) {
                val (node, dist) = queue.removeAt(0)
                if (node in visited) continue
                visited.add(node)
                if (node in crossroads && node != crossroad) {
                    crossroadsDistances[crossroad]!!.add(node to dist)
                    crossroadsDistances[node]!!.add(crossroad to dist)
                    continue
                }
                map.getCardinalNeighbors(node).forEach { next ->
                    if (map[next] != '#') {
                        queue.add(next to dist + 1)
                    }
                }
            }
        }

        // Find the longest route from start to end using a BFS algorithm (using the crossroadsDistances)
        val stack = Stack<State>()
        stack.add(State(start, 0, setOf()))
        val routes = mutableListOf<Int>()
        while (stack.isNotEmpty()) {
            val (node, steps, visited) = stack.pop()
            if (node in visited) continue
            val nextCrossroads = crossroadsDistances[node]!!

            nextCrossroads.forEach { (next, dist) ->

                if (next == end) {
                    routes.add(steps + dist)
                } else if (next !in visited) {
                    // No need of direction for the part two
                    stack.add(State(next, steps + dist, visited + node))
                }
            }
        }

        return routes.max()
    }
}

