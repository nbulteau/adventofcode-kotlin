package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day12/training.txt")
    val data = readFileDirectlyAsText("/year2022/day12/data.txt")

    val inputs = data.split("\n")

    val day = Day12("--- Day 12: Hill Climbing Algorithm ---", "https://adventofcode.com/2022/day/12")
    prettyPrintPartOne { day.partOne(inputs) }
    prettyPrintPartTwo { day.partTwo(inputs) }
}

private class Day12(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(inputs: List<String>): Int {
        val (heightmap, end) = buildHeightmap(inputs)
        val visited = heightmap.findPathsFromEndPoint(end)

        for (i in heightmap.indices) {
            for (j in heightmap[0].indices) {
                if (heightmap[i][j] == 'S') {
                    return visited[Pair(i, j)]!!
                }
            }
        }

        return 0
    }

    fun partTwo(inputs: List<String>): Int {
        val (heightmap, end) = buildHeightmap(inputs)
        val visited = heightmap.findPathsFromEndPoint(end)

        var result = Int.MAX_VALUE
        for (i in heightmap.indices) {
            for (j in heightmap[0].indices) {
                if (heightmap[i][j] == 'a') {
                    val nbVisits = visited[Pair(i, j)]
                    if (nbVisits != null) {
                        result = minOf(result, nbVisits)
                    }
                }
            }
        }

        return result
    }

    private fun buildHeightmap(inputs: List<String>): Pair<Array<CharArray>, Pair<Int, Int>> {
        val heightmap = inputs.map { line -> line.map { it }.toCharArray() }.toTypedArray()

        // find start end
        var end: Pair<Int, Int> = Pair(0, 0)
        for (y in heightmap.indices) {
            for (x in heightmap[y].indices) {
                when (heightmap[y][x]) {
                    'E' -> end = Pair(y, x)
                    else -> {}
                }
            }
        }
        return Pair(heightmap, end)
    }

    private fun Array<CharArray>.findPathsFromEndPoint(end: Pair<Int, Int>): MutableMap<Pair<Int, Int>, Int> {
        val arrayDeque = ArrayDeque<Pair<Int, Int>>()
        val visited = mutableMapOf<Pair<Int, Int>, Int>()

        fun addToQueue(point: Pair<Int, Int>, nbVisits: Int) {
            if (point in visited) {
                return
            }
            visited[point] = nbVisits
            arrayDeque += point
        }

        fun Char.elevation(): Int = when (this) {
            'S' -> 0
            'E' -> 'z' - 'a'
            else -> this - 'a'
        }

        fun visit(point: Pair<Int, Int>, nbVisits: Int, height: Int) {
            // point is in map ?
            if (point.first !in this.indices || point.second !in this[0].indices) {
                return
            }
            val elevation = this[point.first][point.second].elevation()
            // the elevation of the destination square can be at most one higher than the elevation of your current square
            if (height <= elevation + 1) {
                addToQueue(point, nbVisits)
            }
        }

        fun Array<CharArray>.breadthFirstSearch() {
            while (arrayDeque.isNotEmpty()) {
                val point = arrayDeque.removeFirst()
                val nbVisits = visited[point]!! + 1
                val elevation = this[point.first][point.second].elevation()

                visit(Pair(point.first - 1, point.second), nbVisits, elevation)
                visit(Pair(point.first + 1, point.second), nbVisits, elevation)
                visit(Pair(point.first, point.second - 1), nbVisits, elevation)
                visit(Pair(point.first, point.second + 1), nbVisits, elevation)
            }
        }

        addToQueue(end, 0)
        breadthFirstSearch()

        return visited
    }
}


