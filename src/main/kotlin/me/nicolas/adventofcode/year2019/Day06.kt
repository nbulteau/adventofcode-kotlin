package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2019/day/6
fun main() {

    val training = readFileDirectlyAsText("/year2019/day06/training.txt")
    val trainingPartTwo = readFileDirectlyAsText("/year2019/day06/trainingPartTwo.txt")

    val data = readFileDirectlyAsText("/year2019/day06/data.txt")

    val inputs = data.split("\n")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day06().partOne(inputs) })
    prettyPrint(
        message = "Part one bis answer",
        measureTimedValue { Day06().partOneBis(inputs) })
    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day06().partTwo(inputs) })
}

private class Day06 {

    fun partOne(inputs: List<String>): Int {
        var totalNumberOfOrbits = 0

        val graph = initGraph(inputs)
        graph.keys.map { key ->
            var prev = graph[key]
            while (prev != null) {
                prev = graph[prev]
                totalNumberOfOrbits++
            }
        }

        return totalNumberOfOrbits
    }

    fun partOneBis(inputs: List<String>): Int {

        val graph = initGraph(inputs)

        return graph.keys.sumOf { graph.pathFrom(it, mutableListOf()).size }
    }

    fun partTwo(inputs: List<String>): Int {
        val graph = initGraph(inputs)

        val youToRoot = graph.pathFrom("YOU")
        val santaToRoot = graph.pathFrom("SAN")
        val intersection = youToRoot.intersect(santaToRoot.toSet()).first()

        return (youToRoot.indexOf(intersection) - 1) + (santaToRoot.indexOf(intersection) - 1)
    }

    private fun initGraph(inputs: List<String>): Map<String, String> {
        return inputs
            .map { line -> line.split(")") }
            .associate { it.last() to it.first() }
    }

    private fun Map<String, String>.pathFrom(orbit: String, path: MutableList<String> = mutableListOf()): List<String> {
        return this[orbit]?.let { from ->
            path.add(orbit)
            this.pathFrom(from, path)
        } ?: path
    }
}

