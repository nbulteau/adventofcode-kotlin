package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.*

// --- Day 8: Resonant Collinearity ---
// https://adventofcode.com/2024/day/8
fun main() {
    val data = readFileDirectlyAsText("/year2024/day08/data.txt")
    val day = Day08(2024, 8)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day08(year: Int, day: Int, title: String = "Resonant Collinearity") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {

        val antennas = extractAntennas(data)

        val grid = Grid.of(data)

        fun getAntinodesFor(a: Pair<Int, Int>, b: Pair<Int, Int>): List<Pair<Int, Int>> {
            val deltaX = b.second - a.second
            val deltaY = b.first - a.first

            return mutableListOf<Pair<Int, Int>>(
                Pair(a.first - deltaY, a.second - deltaX),
                Pair(b.first + deltaY, b.second + deltaX)
            )
                .filter { point -> grid.isValid(point) }
        }

        return antennas.flatMap { (_, positions) ->
            positions.combinations(2)
                .toList()
                .flatMap { sequence -> getAntinodesFor(sequence.first(), sequence.last()) }
        }
            .distinct()
            .count()
    }

    private fun extractAntennas(data: String): Map<Char, List<Pair<Int, Int>>> {
        val antennas = Grid.of(data).toMap()
            .filter { (_, value) -> value != '.' }
            .map { (position, value) -> Pair(value, position) }
            .groupBy({ value -> value.first }) { value -> value.second }
        return antennas
    }

    fun partTwo(data: String): Int {

        val antennas = extractAntennas(data)

        val grid = Grid.of(data)

        fun getAntinodesFor(a: Pair<Int, Int>, b: Pair<Int, Int>): List<Pair<Int, Int>> {
            val deltaX = b.second - a.second
            val deltaY = b.first - a.first

            val antinodes = mutableListOf<Pair<Int, Int>>()
            var aAntinode = a.copy()
            while (grid.isValid(aAntinode)) {
                antinodes.add(aAntinode)
                aAntinode = Pair(aAntinode.first - deltaY, aAntinode.second - deltaX)
            }

            var bAntinode = b.copy()
            while (grid.isValid(bAntinode)) {
                antinodes.add(bAntinode)
                bAntinode = Pair(bAntinode.first + deltaY, bAntinode.second + deltaX)
            }

            return antinodes
        }

        return antennas.flatMap { (_, positions) ->
            positions.combinations(2)
                .toList()
                .flatMap { sequence ->
                    getAntinodesFor(sequence.first(), sequence.last())
                }
        }
            .distinct()
            .count()
    }
}