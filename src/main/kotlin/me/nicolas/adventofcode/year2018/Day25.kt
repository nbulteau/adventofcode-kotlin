package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs


// https://adventofcode.com/2018/day/25
fun main() {
    val data = readFileDirectlyAsText("/year2018/day25/data.txt")
    val day = Day25(2018, 25, "Four-Dimensional Adventure")
    prettyPrintPartOne { day.partOne(data) }
}

class Day25(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private data class Point4D(val x: Int, val y: Int, val z: Int, val w: Int) {
        fun manhattanDistance(other: Point4D) =
            abs(x - other.x) + abs(y - other.y) + abs(z - other.z) + abs(w - other.w)
    }

    private class UnionFind(size: Int) {
        private val parent = IntArray(size) { it }
        private val rank = IntArray(size) { 0 }

        fun find(x: Int): Int {
            if (parent[x] != x) {
                parent[x] = find(parent[x]) // path compression
            }
            return parent[x]
        }

        fun union(x: Int, y: Int) {
            val rootX = find(x)
            val rootY = find(y)

            if (rootX != rootY) {
                // union by rank
                when {
                    rank[rootX] < rank[rootY] -> parent[rootX] = rootY
                    rank[rootX] > rank[rootY] -> parent[rootY] = rootX
                    else -> {
                        parent[rootY] = rootX
                        rank[rootX]++
                    }
                }
            }
        }

        fun countGroups(): Int {
            return parent.indices.count { find(it) == it }
        }
    }

    private fun parseInput(data: String): List<Point4D> {
        val points = data.lines()
            .filter { it.isNotBlank() }
            .map { line ->
                val (x, y, z, w) = line.split(",").map { parts -> parts.trim().toInt() }
                Point4D(x, y, z, w)
            }
        return points
    }

    fun partOne(data: String): Int {
        // Parse input: convert each line into a Point4D with coordinates (x, y, z, w)
        val points = parseInput(data)

        // Initialize Union-Find structure to track constellations
        // Each point starts in its own constellation
        val uf = UnionFind(points.size)

        // Connect all points that are within Manhattan distance 3
        // If two points are close enough, merge their constellations
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                if (points[i].manhattanDistance(points[j]) <= 3) {
                    uf.union(i, j)
                }
            }
        }

        // Count the number of distinct constellations
        // This equals the number of separate connected components
        return uf.countGroups()
    }
}