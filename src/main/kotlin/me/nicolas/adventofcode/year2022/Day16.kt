package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.max

fun main() {
    val training = readFileDirectlyAsText("/year2022/day16/training.txt")
    val data = readFileDirectlyAsText("/year2022/day16/data.txt")

    val inputs = data.split("\n")

    val day = Day16(2022, 16, "Proboscidea Volcanium", inputs)
    prettyPrintPartOne { day.partOne("AA", 30) }
    prettyPrintPartTwo { day.partTwo("AA", 26) }
}

private class Day16(year: Int, day: Int, title: String, inputs: List<String>) : AdventOfCodeDay(year, day, title) {
    private val valves = buildValves(inputs)

    fun partOne(start: String, minutes: Int): Int {
        return depthFirstSearch(
            originalStart = start,
            currentFlow = 0,
            maxFlow = 0,
            currentValve = start,
            visited = mutableSetOf(),
            time = 0,
            minutes = minutes,
            runTwice = false
        )
    }

    fun partTwo(start: String, minutes: Int): Int {
        return depthFirstSearch(
            originalStart = start,
            currentFlow = 0,
            maxFlow = 0,
            currentValve = start,
            visited = mutableSetOf(),
            time = 0,
            minutes = minutes,
            runTwice = true
        )
    }

    data class Valve(val label: String, val flowRate: Int, val nextValves: List<String>) {
        val shortestPaths = nextValves.associateWith { 1 }.toMutableMap()
    }

    private fun buildValves(inputs: List<String>): Map<String, Valve> {
        val regex = Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? ([, A-Z]+)")
        val valves = inputs.associate { line ->
            val (label, rate, nextNodes) = regex.matchEntire(line)!!.groupValues.drop(1).map { it }
            label to Valve(label, rate.toInt(), nextNodes.split(", "))
        }

        return calculateShortestPaths(valves)
    }

    /**
     * This is an implementation of the [Floyd-Warshall algorithm](https://fr.wikipedia.org/wiki/Algorithme_de_Floyd-Warshall#Pseudo-code)
     * for calculating shortest path.
     * This algorithm is good to know the shortest paths from any node to another node.
     */
    private fun calculateShortestPaths(valves: Map<String, Valve>): Map<String, Valve> {
        for (k in valves.keys) {
            for (i in valves.keys) {
                for (j in valves.keys) {
                    val ik = valves[i]!!.shortestPaths[k] ?: Short.MAX_VALUE.toInt()
                    val kj = valves[k]!!.shortestPaths[j] ?: Short.MAX_VALUE.toInt()
                    val ij = valves[i]!!.shortestPaths[j] ?: Short.MAX_VALUE.toInt()
                    if (ij > ik + kj) {
                        valves[i]!!.shortestPaths[j] = ik + kj
                    }
                }
            }
        }

        valves.values.filter { valve -> valve.flowRate == 0 }
            .map(Valve::label).forEach { zeroFlowRateValve ->
                for (valve in valves) {
                    valve.value.shortestPaths.remove(zeroFlowRateValve)
                }
            }

        return valves
    }

    /**
     * A [Depth-first Search](https://en.wikipedia.org/wiki/Depth-first_search#Pseudocode) implementation,
     * making sure to not traverse to nodes that would go past the allotted time ([minutes]).
     *
     */
    private fun depthFirstSearch(
        originalStart: String,
        currentFlow: Int,
        maxFlow: Int,
        currentValve: String,
        visited: Set<String>,
        time: Int,
        minutes: Int,
        runTwice: Boolean,
    ): Int {
        var newMaxFlow = max(maxFlow, currentFlow)
        for ((valve, distance) in valves[currentValve]?.shortestPaths!!) {
            if (!visited.contains(valve) && time + distance + 1 < minutes) {
                newMaxFlow = depthFirstSearch(
                    originalStart = originalStart,
                    currentFlow = currentFlow + (minutes - time - distance - 1) * valves[valve]?.flowRate!!,
                    maxFlow = newMaxFlow,
                    currentValve = valve,
                    visited = visited union listOf(valve),
                    time = time + distance + 1,
                    minutes = minutes,
                    runTwice = runTwice
                )
            }
        }

        /**
         * To solve the second part, we'll repeat the DFS, but we'll start the clock [time] over at zero, and we'll
         * retain the currently visited nodes, so we don't repeat them.
         */
        return if (runTwice) {
            depthFirstSearch(
                originalStart,
                currentFlow,
                newMaxFlow,
                originalStart,
                visited,
                time = 0,
                minutes,
                runTwice = false // otherwise infinite recursion...
            )
        } else {
            newMaxFlow
        }
    }
}


