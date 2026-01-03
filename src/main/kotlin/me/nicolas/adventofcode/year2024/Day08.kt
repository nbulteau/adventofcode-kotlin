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

        val grid = SimpleGrid.of(data)

        fun getAntinodesFor(a: Point, b: Point): List<Point> {
            val deltaX = b.x - a.x
            val deltaY = b.y - a.y

            return mutableListOf(
                Point(a.y - deltaY, a.x - deltaX),
                Point(b.y + deltaY, b.x + deltaX)
            )
                .filter { point -> grid.contains(point) }
        }

        return antennas.flatMap { (_, positions) ->
            positions.combinations(2)
                .toList()
                .flatMap { sequence -> getAntinodesFor(sequence.first(), sequence.last()) }
        }
            .distinct()
            .count()
    }

    private fun extractAntennas(data: String): Map<Char, List<Point>> {
        val antennas = SimpleGrid.of(data).entries
            .filter { (_, value) -> value != '.' }
            .map { (position, value) -> Pair(value, position) }
            .groupBy({ value -> value.first }) { value -> value.second }

        return antennas
    }

    fun partTwo(data: String): Int {

        val antennas = extractAntennas(data)

        val grid = SimpleGrid.of(data)

        fun getAntinodesFor(a: Point, b: Point): List<Point> {
            val deltaX = b.x - a.x
            val deltaY = b.y - a.y

            val antinodes = mutableListOf<Point>()
            var aAntinode = a.copy()
            while (grid.contains(aAntinode)) {
                antinodes.add(aAntinode)
                aAntinode = Point(aAntinode.x - deltaX, aAntinode.y - deltaY)
            }

            var bAntinode = b.copy()
            while (grid.contains(bAntinode)) {
                antinodes.add(bAntinode)
                bAntinode = Point(bAntinode.x + deltaX, bAntinode.y + deltaY)
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