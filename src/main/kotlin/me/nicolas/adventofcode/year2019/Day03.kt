package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs


// --- Day 3: Crossed Wires ---
// https://adventofcode.com/2019/day/3
fun main() {

    println("--- Day 3: Crossed Wires ---")
    println()

    val training = readFileDirectlyAsText("/year2019/day03/training.txt")
    val data = readFileDirectlyAsText("/year2019/day03/data.txt")

    val paths = data.split("\n").map { str: String -> str.split(",") }

    // Part One
    Day03().partOne(paths)

    // Part Two
    Day03().partTwo(paths)
}

private class Day03 {

    private enum class Direction(val label: Char, val dx: Int, val dy: Int) {

        RIGHT('R', 1, 0),
        UP('U', 0, 1),
        LEFT('L', -1, 0),
        DOWN('D', 0, -1);

        companion object {
            fun byLabel(label: Char) = entries.first { direction -> label == direction.label }
        }
    }

    private data class Coord(var x: Int, var y: Int)

    private fun buildWire(wire: List<String>): MutableSet<Coord> {
        val coords = mutableSetOf<Coord>()
        var currentCoord = Coord(0, 0)

        wire.forEach { move ->
            val direction = Direction.byLabel(move[0])
            val value = move.drop(1).toInt()
            for (i in 1..value) {
                val coord = Coord(currentCoord.x + direction.dx, currentCoord.y + direction.dy)
                coords.add(coord)
                currentCoord = coord
            }
        }
        return coords
    }

    /**
     * The number of steps a wire takes is the total number of grid squares the wire has entered to get to that location
     */
    private fun getNumberOfStepsToTarget(wire: List<String>, target: Coord): Int {
        var path = 0
        var currentCoord = Coord(0, 0)

        wire.forEach { move ->
            val direction = Direction.byLabel(move[0])
            val value = move.drop(1).toInt()
            for (i in 1..value) {
                val coord = Coord(currentCoord.x + direction.dx, currentCoord.y + direction.dy)
                path++
                if (coord == target) {
                    return path
                } else {
                    currentCoord = coord
                }
            }
        }
        throw RuntimeException()
    }


    fun partOne(paths: List<List<String>>) {

        val path1 = buildWire(paths[0])
        val path2 = buildWire(paths[1])
        val intersections = path1.intersect(path2)
        val result = intersections
            .map { coord -> abs(coord.x) + abs(coord.y) }
            .minOf { it }

        println("Part one $result")
    }

    fun partTwo(paths: List<List<String>>) {
        val path1 = buildWire(paths[0])
        val path2 = buildWire(paths[1])
        val intersections = path1.intersect(path2)
        val result = intersections
            .map { coord -> getNumberOfStepsToTarget(paths[0], coord) + getNumberOfStepsToTarget(paths[1], coord) }
            .minOf { it }

        println("Part two $result")
    }
}


