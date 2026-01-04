package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 12: Digital Plumber ---
// https://adventofcode.com/2017/day/12
fun main() {
    val data = readFileDirectlyAsText("/year2017/day12/data.txt")
    val day = Day12()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day12(year: Int = 2017, day: Int = 12, title: String = "Digital Plumber") : AdventOfCodeDay(year, day, title) {

    /**
     * Part One
     * Find all nodes connected to node 0 in an undirected graph.
     */
    fun partOne(data: String): Int {
        val graph = buildGraph(data)

        // Perform depth-first search (DFS) to find all nodes connected to node 0
        val visited = mutableSetOf<Int>()
        val toVisit = ArrayDeque<Int>()
        toVisit.add(0)

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (current !in visited) {
                visited.add(current)
                graph[current]?.forEach { neighbor ->
                    if (neighbor !in visited) {
                        toVisit.add(neighbor)
                    }
                }
            }
        }

        return visited.size
    }

    /**
     * Part Two
     * Count the number of connected components in the undirected graph.
     */
    fun partTwo(data: String): Int {
        val graph = buildGraph(data)

        val visited = mutableSetOf<Int>()
        var groupCount = 0
        for (node in graph.keys) {
            if (node !in visited) {
                groupCount++
                // Perform DFS to mark all nodes in this group as visited
                val toVisit = ArrayDeque<Int>()
                toVisit.add(node)

                while (toVisit.isNotEmpty()) {
                    val current = toVisit.removeFirst()
                    if (current !in visited) {
                        visited.add(current)
                        graph[current]?.forEach { neighbor ->
                            if (neighbor !in visited) {
                                toVisit.add(neighbor)
                            }
                        }
                    }
                }
            }
        }

        return groupCount
    }

    private fun buildGraph(data: String): Map<Int, List<Int>> {
        val graph = mutableMapOf<Int, MutableList<Int>>()
        data.lines().forEach { line ->
            val parts = line.split(" <-> ")
            val node = parts[0].toInt()
            val connections = parts[1].split(", ").map { it.toInt() }
            graph[node] = connections.toMutableList()
        }

        return graph
    }

}