package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*

fun main() {
    val data = readFileDirectlyAsText("/year2023/day08/data.txt")
    val day = Day08(2023, 8, "Haunted Wasteland")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day08(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val (instructions, network) = extractData(data)
        return countSteps("AAA", network, instructions) { it != "ZZZ" }
    }

    fun partTwo(data: String): Long {
        val (instructions, network) = extractData(data)
        val startPositions = network.keys.filter { it.endsWith('A') }
        val endPositions = startPositions.map { start ->
            countSteps(start, network, instructions) { !it.endsWith('Z') }
        }
        return endPositions.map { it.toLong() }.lcm()
    }

    // The condition is used to stop the loop when we reach the end of the network
    private fun countSteps(
        start: String,
        network: Map<String, Pair<String, String>>,
        instructions: CircularList<Char>,
        condition: (String) -> Boolean
    ): Int {
        var current = start
        var steps = 0
        do {
            // We get the next node to visit depending on the current instruction
            current = network.getValue(current).let {
                when (instructions[steps]) {
                    'L' -> it.first
                    'R' -> it.second
                    else -> throw IllegalArgumentException("Unknown direction: ${instructions[steps]}")
                }
            }
            steps++
        } while (condition(current))
        return steps
    }

    // The key is the current route (AAA in the example).
    // The value is a Pair<String, String> where :
    // the first element of the pair represents where to go when turning left from the key
    // and second represents where to go when turning right from the key
    private fun extractData(data: String): Pair<CircularList<Char>, Map<String, Pair<String, String>>> {
        val (instructions, network) = data.split("\n\n")
        return CircularList(instructions.toList()) to network.split("\n").associate { line ->
            val (name, value) = line.split(" = ")
            val parts = value.split(", ")
            name to Pair(parts.first().trim('('), parts.last().trim(')'))
        }
    }
}

