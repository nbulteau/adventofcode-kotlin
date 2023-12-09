package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2021/day/12
fun main() {

    val training = readFileDirectlyAsText("/year2021/day12/training.txt")
    val slightlyLargerTraining = readFileDirectlyAsText("/year2021/day12/slightly-larger-training.txt")
    val largerTraining = readFileDirectlyAsText("/year2021/day12/larger-training.txt")

    val data = readFileDirectlyAsText("/year2021/day12/data.txt")
    val inputs = data.split("\n")

    Day12().solve(inputs)
}

private class Day12 {

    fun solve(inputs: List<String>) {
        val graph = initCavesGraph(inputs)
        val cavesAllowedVisits = initCavesAllowedVisits(graph)
        val distinctPaths = mutableSetOf<List<String>>()

        prettyPrint(
            message = "Part one answer",
            measureTimedValue { Day12().partOne(graph, cavesAllowedVisits, distinctPaths) })

        prettyPrint(
            message = "Part two answer",
            measureTimedValue { Day12().partTwo(graph, cavesAllowedVisits, distinctPaths) })
    }

    fun partOne(
        graph: Map<String, Set<String>>,
        caveAllowedVisits: Map<String, Int>,
        distinctPaths: MutableSet<List<String>>,
    ): Int {
        recurseFindPaths("start", graph, mutableListOf(), distinctPaths, caveAllowedVisits)

        return distinctPaths.size
    }

    fun partTwo(
        graph: Map<String, Set<String>>,
        caveAllowedVisits: Map<String, Int>,
        distinctPaths: MutableSet<List<String>>,
    ): Int {
        caveAllowedVisits
            .filter { node ->
                node.key != "start" && node.key != "end" && node.key.all { it.isLowerCase() }
            }
            .forEach { (key, _) ->
                val allowedVisitsCopy = caveAllowedVisits.toMutableMap()
                allowedVisitsCopy[key] = 2
                recurseFindPaths("start", graph, mutableListOf(), distinctPaths, allowedVisitsCopy)
            }

        return distinctPaths.size
    }

    private fun initCavesAllowedVisits(graph: Map<String, MutableSet<String>>): Map<String, Int> {
        return graph
            .map { node -> node.key to if (node.key.all { it.isLowerCase() }) 1 else Int.MAX_VALUE }
            .toMap()
    }

    private fun initCavesGraph(inputs: List<String>): Map<String, MutableSet<String>> {
        // init graph : find all nodes
        val graph = inputs
            .flatMap { arc ->
                arc.split("-")
            }.associateWith { mutableSetOf<String>() }

        // init connection between caves
        inputs.forEach { arc ->
            val (a, b) = arc.split("-")
            graph[a]?.add(b)
            graph[b]?.add(a)
        }

        return graph
    }

    private fun recurseFindPaths(
        cave: String,
        graph: Map<String, Set<String>>,
        path: List<String>,
        paths: MutableSet<List<String>>,
        allowedVisits: Map<String, Int>,
    ) {
        val currentPath = path.toMutableList()
        currentPath.add(cave)
        if (cave == "end") { // the end
            paths.add(currentPath)
        } else if (allowedVisits[cave]!! > 0) {
            for (caveToVisit in graph[cave]!!) {
                recurseFindPaths(caveToVisit, graph, currentPath, paths, allowedVisits.decrease(cave))
            }
        }
    }

    private fun Map<String, Int>.decrease(cave: String): Map<String, Int> {
        val map = this.toMutableMap()
        map[cave] = map[cave]!! - 1
        return map
    }
}


