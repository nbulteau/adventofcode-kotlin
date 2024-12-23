package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 23: LAN Party ---
// https://adventofcode.com/2024/day/23
fun main() {
    val data = readFileDirectlyAsText("/year2024/day23/data.txt")
    val day = Day23(2024, 23)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

/**
 * The code simulates a LAN party where the participants are connected to each other by cables.
 *
 * Part One
 * Finding Triangles:
 * - It finds all triangles in the graph by iterating over all pairs of neighbors of a node and checking if they are connected to each other.
 * - It counts the number of triangles that contain at least one node starting with the letter 't'.
 *
 * Part Two
 * Finding Largest Clique:
 * - It finds the largest clique in the graph by backtracking to try to add each node to the current clique.
 * - A node can be added to a clique if it is connected to all other nodes in the clique.
 * - It returns the largest clique as a list of nodes.
 */
class Day23(year: Int, day: Int, title: String = "LAN Party") {

    fun partOne(data: String): Int {
        val connections = parseConnections(data)

        return findTriangles(connections).count { triangle -> triangle.any { node -> node.startsWith('t') } }
    }

    fun partTwo(data: String): String {
        val connections = parseConnections(data)
        val largestClique = findLargestClique(connections)

        return largestClique.sorted().joinToString(",")
    }

    private fun findLargestClique(connections: Map<String, Set<String>>): List<String> {
        val nodes = connections.keys.toList()
        var maxClique = emptyList<String>()

        // Check if a node can be added to a clique by verifying that it is connected to all other nodes in the clique
        fun String.canBeAddToClique(clique: List<String>): Boolean =
            clique.all { cliqueNode -> cliqueNode in connections[this]!! }

        // Backtrack to find the largest clique by trying to add each node to the current clique
        fun backtrack(currentClique: List<String>, index: Int) {
            // If we have reached the end of the list of nodes, update the max clique if needed
            if (index == nodes.size) {
                if (currentClique.size > maxClique.size) {
                    maxClique = currentClique
                }
                return
            }

            val node = nodes[index]
            if (node.canBeAddToClique(currentClique)) {
                backtrack(currentClique + node, index + 1)
            }
            backtrack(currentClique, index + 1)
        }

        backtrack(emptyList(), 0)

        return maxClique
    }

    private fun parseConnections(data: String): Map<String, Set<String>> {
        val adjacencyList = mutableMapOf<String, MutableSet<String>>()
        data.lines().forEach { line ->
            val (node1, node2) = line.split('-')
            adjacencyList.getOrPut(node1) { mutableSetOf() }.add(node2)
            adjacencyList.getOrPut(node2) { mutableSetOf() }.add(node1)
        }

        return adjacencyList
    }


    // Find all triangles in the graph by iterating over all pairs of neighbors of a node
    // and checking if they are connected to each other
    // This is a brute-force solution that works well for small graphs
    private fun findTriangles(connections: Map<String, Set<String>>): List<Set<String>> {
        val triangles = mutableListOf<Set<String>>()
        for ((node1, neighbors1) in connections) {
            for (node2 in neighbors1) {
                if (node1 < node2) { // Ensure we only count each triangle once
                    val neighbors2 = connections[node2] ?: continue
                    for (node3 in neighbors2) {
                        if (node2 < node3 && connections.containsKey(node3) && connections[node3]!!.contains(node1)) {
                            triangles.add(setOf(node1, node2, node3))
                        }
                    }
                }
            }
        }

        return triangles
    }
}