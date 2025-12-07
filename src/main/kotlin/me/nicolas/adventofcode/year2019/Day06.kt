package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2019/day/6
fun main() {

    val training = readFileDirectlyAsText("/year2019/day06/training.txt")
    val trainingPartTwo = readFileDirectlyAsText("/year2019/day06/trainingPartTwo.txt")

    val data = readFileDirectlyAsText("/year2019/day06/data.txt")
    val day = Day06(2019, 6)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartOne { day.partOneBis(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day06(year: Int, day: Int, title: String = "Universal Orbit Map") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val inputs = data.split("\n")

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

    fun partOneBis(data: String): Int {
        val inputs = data.split("\n")

        val graph = initGraph(inputs)

        return graph.keys.sumOf { graph.pathFrom(it, mutableListOf()).size }
    }

    fun partTwo(data: String): Int {
        val inputs = data.split("\n")

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

