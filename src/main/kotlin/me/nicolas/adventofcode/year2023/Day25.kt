package me.nicolas.adventofcode.year2023

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer
import edu.uci.ics.jung.graph.Graph
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day25/data.txt")
    val day = Day25(2023, 25, "Snowverload")
    prettyPrintPartOne { day.partOne(data) }
}


/**
 * This solution uses the JUNG library to find the number of clusters in the graph.
 * The number of clusters is the product of the sizes of the two largest groups after removing the 3 most common edges in the graph
 * The JUNG library is a Java library for graph analysis and visualization.
 * It is available at https://github.com/jrtom/jung
 */
class Day25(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {

        val graph: Graph<String, String> = buildGraph(data)

        val numEdgesToRemove = 3
        // Finds the 3 edges with the highest betweenness value.
        // These edges are the most likely to be between clusters in the graph
        val edgeBetweennessClusterer = EdgeBetweennessClusterer<String, String>(numEdgesToRemove)

        // Finds the set of clusters which have the strongest "community structure".
        // The more edges removed the smaller and more cohesive the clusters.
        val edgeBetweennessClusters = edgeBetweennessClusterer.apply(graph)!!

        return edgeBetweennessClusters.let {
            it.fold(1) { acc, cluster ->
                acc * cluster.size
            }
        }
    }

    // Builds a graph from the input data using the JUNG library
    private fun buildGraph(data: String): Graph<String, String> {
        val graph: Graph<String, String> = UndirectedSparseGraph()
        val input = data.lines()
        for (i in input.indices) {
            val (first, rest) = input[i].split(": ")
            val cs = rest.split(" ")

            graph.addVertex(first)

            for (c in cs) {
                graph.addVertex(c)
                graph.addEdge("$first--$c", first, c)
            }
        }
        return graph
    }
}

