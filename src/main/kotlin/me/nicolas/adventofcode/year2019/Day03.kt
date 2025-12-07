package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs


// --- Day 3: Crossed Wires ---
// https://adventofcode.com/2019/day/3
fun main() {
    val training = readFileDirectlyAsText("/year2019/day03/training.txt")
    val data = readFileDirectlyAsText("/year2019/day03/data.txt")
    val day = Day03(2019, 3)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day03(year: Int, day: Int, title: String = "Crossed Wires") : AdventOfCodeDay(year, day, title) {

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

    // Build the set of coordinates visited by the wire (excluding the origin)
    private fun buildWire(wire: List<String>): Set<Coord> {
        val coords = mutableSetOf<Coord>()
        var x = 0
        var y = 0

        for (move in wire) {
            val direction = Direction.byLabel(move[0])
            val steps = move.substring(1).toInt()
            repeat(steps) {
                x += direction.dx
                y += direction.dy
                coords.add(Coord(x, y))
            }
        }

        return coords
    }

    /**
     * The number of steps a wire takes is the total number of grid squares the wire has entered to get to that location
     */
    private fun getNumberOfStepsToTarget(wire: List<String>, target: Coord): Int {
        // Count steps until reaching the target coordinate
        var steps = 0
        var x = 0
        var y = 0
        val targetX = target.x
        val targetY = target.y

        for (move in wire) {
            val direction = Direction.byLabel(move[0])
            val value = move.substring(1).toInt()
            repeat(value) {
                x += direction.dx
                y += direction.dy
                steps++
                if (x == targetX && y == targetY) {
                    return steps
                }
            }
        }

        throw IllegalStateException("Target not reached by the provided wire")
    }


    fun partOne(data: String): Int {
        val paths = data.split("\n").map { str: String -> str.split(",") }

        val path1 = buildWire(paths[0])
        val path2 = buildWire(paths[1])
        val intersections = path1.intersect(path2)

        return intersections
            .map { coord -> abs(coord.x) + abs(coord.y) }
            .minOf { it }
    }

    fun partTwo(data: String): Int {
        val paths = data.split("\n").map { str: String -> str.split(",") }

        val path1 = buildWire(paths[0])
        val path2 = buildWire(paths[1])
        val intersections = path1.intersect(path2)

        return intersections
            .map { coord -> getNumberOfStepsToTarget(paths[0], coord) + getNumberOfStepsToTarget(paths[1], coord) }
            .minOf { it }
    }
}


