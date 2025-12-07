package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2021/day/12
fun main() {
    val data = readFileDirectlyAsText("/year2021/day12/data.txt")
    val day = Day12(2021, 12)
    val inputs = data.split("\n")
    prettyPrintPartOne { day.partOne(inputs) }
    prettyPrintPartTwo { day.partTwo(inputs) }
}

class Day12(year: Int, day: Int, title: String = "Passage Pathing") : AdventOfCodeDay(year, day, title) {

    fun partOne(inputs: List<String>): Int {
        val graph = initCavesGraph(inputs)
        val cavesAllowedVisits = initCavesAllowedVisits(graph)
        val distinctPaths = mutableSetOf<List<String>>()
        recurseFindPaths("start", graph, mutableListOf(), distinctPaths, cavesAllowedVisits)
        return distinctPaths.size
    }

    fun partTwo(inputs: List<String>): Int {
        val graph = initCavesGraph(inputs)
        val cavesAllowedVisits = initCavesAllowedVisits(graph)
        val distinctPaths = mutableSetOf<List<String>>()
        cavesAllowedVisits
            .filter { node ->
                node.key != "start" && node.key != "end" && node.key.all { it.isLowerCase() }
            }
            .forEach { (key, _) ->
                val allowedVisitsCopy = cavesAllowedVisits.toMutableMap()
                allowedVisitsCopy[key] = 2
                recurseFindPaths("start", graph, mutableListOf(), distinctPaths, allowedVisitsCopy)
            }
        return distinctPaths.size
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
