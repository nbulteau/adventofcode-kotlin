package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day25/data.txt")
    val day = Day25Slow(2023, 25, "Snowverload")
    prettyPrintPartOne { day.partOne(data) }
}

class Day25Slow(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    // Calculates the product of the sizes of the two largest groups after removing the 3 most common edges in the graph
    fun partOne(data: String): Int {
        val graph = buildGraph(data)

        // Compute the frequency of each edge in the graph using BFS
        val edgeFrequency = mutableMapOf<Set<String>, Int>()
        val vertexes = graph.map.keys.toMutableList()
        vertexes.indices.forEach { i ->
            val start = vertexes[i]
            for (j in i + 1 until vertexes.size) {
                val target = vertexes[j]
                graph.markEdges(start, target, edgeFrequency)
            }
        }
        // cut 3 edges with the highest frequency
        edgeFrequency.entries.sortedByDescending { (_, value) -> value }
            .take(3)
            .forEach { (key, _) -> graph.cutEdge(key) }

        val part1Size = graph.findConnectedSize(graph.map.keys.first())
        val part2Size = graph.map.size - part1Size

        return part1Size * part2Size
    }

    private data class Graph(val map: Map<String, MutableSet<String>>) {
        // findConnectedSize function calculates the size of the group that the start node belongs to
        fun findConnectedSize(start: String): Int {
            val queue = ArrayDeque<String>()
            val visited = mutableSetOf<String>()
            queue.add(start)
            visited.add(start)
            while (queue.isNotEmpty()) {
                val curr = queue.removeFirst()
                this.map[curr]!!
                    .filter { node -> visited.contains(node).not() }
                    .forEach { node ->
                        queue.add(node)
                        visited.add(node)
                    }
            }

            return visited.size
        }

        // cutEdge function removes an edge from the graph
        fun cutEdge(edge: Set<String>) {
            val it = edge.iterator()
            val a = it.next()
            val b = it.next()
            this.map[a]?.remove(b)
            this.map[b]?.remove(a)
        }

        // Step data class represents a step in the BFS algorithm
        private data class Step(val vertex: String, val edges: List<Set<String>>)

        // markEdges function marks the edges that are part of the shortest path between start and target
        fun markEdges(start: String, target: String, edgeFrequency: MutableMap<Set<String>, Int>) {
            val queue = ArrayDeque<Step>()
            val visited = mutableSetOf<String>()
            queue.add(Step(start, emptyList()))
            visited.add(start)
            while (queue.isNotEmpty()) {
                val (vertex, edges) = queue.removeFirst()
                if (target == vertex) {
                    edges.forEach { edge ->
                        val v = edgeFrequency.getOrDefault(edge, 0)
                        edgeFrequency[edge] = v + 1
                    }
                    return
                }
                this.map[vertex]!!.filter { node -> visited.contains(node).not() }
                    .forEach { node: String ->
                        val nextEdges = edges.toMutableList()
                        nextEdges.add(setOf(vertex, node))
                        queue.add(Step(node, nextEdges))
                        visited.add(node)
                    }
            }
        }
    }

    // buildGraph function builds a graph from the input data
    private fun buildGraph(data: String): Graph {
        val nodes = mutableMapOf<String, MutableSet<String>>()
        data.lines().forEach { line ->
            val parts = line.split(Regex(": "))
            val name = parts[0]
            val cons = parts[1].split(Regex(" "))
            for (con in cons) {
                nodes.computeIfAbsent(name) { mutableSetOf() }.add(con)
                nodes.computeIfAbsent(con) { mutableSetOf() }.add(name)
            }
        }

        return Graph(nodes)
    }
}
