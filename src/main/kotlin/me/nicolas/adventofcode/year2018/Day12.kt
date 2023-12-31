package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2018/day/12
fun main() {
    val data = readFileDirectlyAsText("/year2018/day12/data.txt")
    val day = Day12(2018, 12, "Subterranean Sustainability")
    prettyPrintPartOne { day.partOne(data, 20) }
    prettyPrintPartTwo { day.partTwo(data, 50_000_000_000) }
}

class Day12(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String, targetGeneration: Int): Long {
        val (initial, rules) = parseInput(data)

        return mutatePlants(initial, rules)
            // Drop all the generations before the target generation.
            .drop(targetGeneration - 1)
            // The result is the sum of the plants.
            .first().second
    }

    fun partTwo(data: String, targetGeneration: Long): Long {
        val (initial, rules) = parseInput(data)

        var previousDiff = 0L
        var previousSize = 0L
        var generationNumber = 0

        // We need to find a generation that grows the same amount of plants as the previous one
        // This means we've found a repeating pattern. We can then calculate the result.
        mutatePlants(initial, rules)
            // Drop all the generations until we find a repeating pattern.
            .dropWhile { generation ->
                // Calculate the difference between the current generation and the previous one.
                val thisDiff = generation.second - previousSize
                // If the difference is the same as the previous one, we're still growing the same amount of plants.
                if (thisDiff != previousDiff) {
                    // Still changing
                    previousDiff = thisDiff
                    previousSize = generation.second
                    generationNumber++
                    true
                } else {
                    // We've found it, stop dropping.
                    false
                }
            }.first()

        // Calculate the result based on the repeating pattern we found.
        println("Found repeating pattern at generation $generationNumber")
        return previousSize + (previousDiff * (targetGeneration - generationNumber))
    }

    // Mutate the plants based on the rules and return a sequence of the result and the sum of the plants.
    private fun mutatePlants(state: List<Int>, rules: List<Rule>): Sequence<Pair<List<Int>, Long>> = sequence {
        var index = 0
        var currentState = state
        while (true) {
            // debug(index, currentState)

            val result = mutableListOf<Int>()

            for (idx in currentState.first() - 2..currentState.last() + 2) {
                val currentPattern = (-2..2).filter { it + idx in currentState }
                if (rules.find { (pattern) -> currentPattern == pattern }?.to == true) {
                    result += idx
                }
            }

            currentState = result
            index++

            yield(Pair(result, result.sumOf { it.toLong() }))
        }
    }

    // Debug : Display the result
    private fun debug(index: Int, result: List<Int>) {
        print("$index: ")
        for (idx in result.first() - 2..result.last() + 2) {
            if (idx in result) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }

    // Data classes for the rules and the result of the mutation sequence.
    private data class Rule(val pattern: List<Int>, val to: Boolean)

    // Parse the input into a list of initial plants and a list of rules.
    private fun parseInput(data: String): Pair<List<Int>, List<Rule>> {
        fun String.parse() = map { it == '#' }.withIndex().filter { it.value }.map { it.index }

        val (initPart, rulesPart) = data.split("\n\n")
        val initial = initPart.substringAfter(": ").parse()

        val rules = rulesPart.lines().map { line ->
            val (pattern, to) = line.split(" => ")
            Rule(pattern.parse().map { it - 2 }, to == "#")
        }

        return Pair(initial, rules)
    }
}