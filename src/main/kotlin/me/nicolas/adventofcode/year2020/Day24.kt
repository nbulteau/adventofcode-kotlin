package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 24: Lobby Layout ---
// https://adventofcode.com/2020/day/24
fun main() {
    val data = readFileDirectlyAsText("/year2020/day24/data.txt")
    val day = Day24(2020, 24, "Lobby Layout")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day24(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val instructions = data.split("\n").filter { it.isNotEmpty() }.map { extractDirections(it) }
        val blackTiles = getInitialBlackTiles(instructions)
        return blackTiles.size
    }

    fun partTwo(data: String): Int {
        val instructions = data.split("\n").filter { it.isNotEmpty() }.map { extractDirections(it) }
        var blackTiles = getInitialBlackTiles(instructions)

        repeat(100) {
            blackTiles = applyRules(blackTiles)
        }
        return blackTiles.size
    }

    private fun getInitialBlackTiles(instructions: List<List<Direction>>): Set<Coord> {
        val blackTiles = mutableSetOf<Coord>()
        instructions.forEach { instruction ->
            val finalCoord = instruction.fold(Coord(0, 0)) { acc, dir -> acc.move(dir) }
            if (finalCoord in blackTiles) {
                blackTiles.remove(finalCoord)
            } else {
                blackTiles.add(finalCoord)
            }
        }
        return blackTiles
    }

    private fun applyRules(blackTiles: Set<Coord>): Set<Coord> {
        val newBlackTiles = mutableSetOf<Coord>()
        val whiteTilesToCheck = mutableSetOf<Coord>()

        for (tile in blackTiles) {
            val neighbors = tile.getNeighbors()
            val blackNeighbors = neighbors.count { it in blackTiles }
            if (blackNeighbors in 1..2) {
                newBlackTiles.add(tile)
            }
            whiteTilesToCheck.addAll(neighbors.filter { it !in blackTiles })
        }

        for (tile in whiteTilesToCheck) {
            val blackNeighbors = tile.getNeighbors().count { it in blackTiles }
            if (blackNeighbors == 2) {
                newBlackTiles.add(tile)
            }
        }
        return newBlackTiles
    }

    private fun extractDirections(line: String): List<Direction> {
        val directions = mutableListOf<Direction>()
        var i = 0
        while (i < line.length) {
            when (line[i]) {
                'e' -> {
                    directions.add(Direction.EAST)
                    i++
                }
                'w' -> {
                    directions.add(Direction.WEST)
                    i++
                }
                'n' -> {
                    when (line[i + 1]) {
                        'e' -> directions.add(Direction.NORTH_EAST)
                        'w' -> directions.add(Direction.NORTH_WEST)
                    }
                    i += 2
                }
                's' -> {
                    when (line[i + 1]) {
                        'e' -> directions.add(Direction.SOUTH_EAST)
                        'w' -> directions.add(Direction.SOUTH_WEST)
                    }
                    i += 2
                }
            }
        }
        return directions
    }

    private data class Coord(val x: Int, val y: Int) {
        fun move(direction: Direction): Coord {
            return Coord(x + direction.dx, y + direction.dy)
        }

        fun getNeighbors(): List<Coord> {
            return Direction.values().map { this.move(it) }
        }
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        EAST(1, 0),
        SOUTH_EAST(0, 1),
        SOUTH_WEST(-1, 1),
        WEST(-1, 0),
        NORTH_WEST(0, -1),
        NORTH_EAST(1, -1)
    }
}