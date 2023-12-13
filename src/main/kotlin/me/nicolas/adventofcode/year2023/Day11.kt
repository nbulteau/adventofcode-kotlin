package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*

fun main() {
    val data = readFileDirectlyAsText("/year2023/day11/data.txt")
    val day = Day11(2023, 11, "Cosmic Expansion")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day11(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Long {
        val galaxies = parseData(data, 2)

        return lengthOfTheShortestPathBetweenEveryPairOfGalaxies(galaxies)
    }

    fun partTwo(data: String): Long {
        val galaxies = parseData(data, 1_000_000)

        return lengthOfTheShortestPathBetweenEveryPairOfGalaxies(galaxies)
    }

    private fun lengthOfTheShortestPathBetweenEveryPairOfGalaxies(galaxies: List<Pair<Int, Int>>): Long {
        return galaxies.withIndex().sumOf { (index, first) ->
            galaxies.drop(index + 1).sumOf { second ->
                // println("$first -> $second = ${first.manhattanDistance(second)}")
                first.manhattanDistance(second)
            }
        }
    }

    private fun parseData(data: String, expansionFactor: Int): List<Pair<Int, Int>> {
        val lines = data.lines()
        val rows = lines.indices.filter { '#' !in lines[it] }.toSet()
        val cols = (0 until (lines.maxOfOrNull { it.length } ?: 0))
            .filter { col -> lines.all { it.getOrNull(col) != '#' } }
            .toSet()

        return buildList {
            var expandedY = -1
            for ((y, row) in lines.withIndex()) {
                var expandedX = -1
                expandedY += if (y in rows) expansionFactor else 1
                for ((x, char) in row.withIndex()) {
                    expandedX += if (x in cols) expansionFactor else 1
                    if (char == '#') add(Pair(expandedY, expandedX))
                }
            }
        }
    }
}

