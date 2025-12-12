package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// --- Day 12: Rain Risk ---
// https://adventofcode.com/2020/day/12
fun main() {
    val data = readFileDirectlyAsText("/year2020/day12/data.txt")
    val day = Day12(2020, 12, "Rain Risk")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day12(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val instructions = parseInstructions(data)
        val taxicabGeometry = TaxicabGeometry()
        taxicabGeometry.move(instructions)
        return taxicabGeometry.path
    }

    fun partTwo(data: String): Int {
        val instructions = parseInstructions(data)
        val taxicabGeometry = TaxicabGeometryWithWayPoint()
        taxicabGeometry.move(instructions)
        return taxicabGeometry.path
    }

    private fun parseInstructions(data: String): List<Instruction> {
        return data.split("\n").filter { it.isNotEmpty() }
            .map { line -> Instruction(line.first(), line.substring(1).toInt()) }
    }

    data class Instruction(val action: Char, val value: Int)

    class TaxicabGeometry(startDirection: Direction = Direction.EAST) {
        var currentDirection = startDirection
        private var eastWest = 0
        private var northSouth = 0

        fun move(instructions: List<Instruction>) {
            instructions.forEach { instruction -> move(instruction) }
        }

        fun move(instruction: Instruction) {
            when (instruction.action) {
                'N', 'E', 'S', 'W' -> move(Direction.byLabel(instruction.action), instruction.value)
                'L' -> currentDirection = rotateLeft(instruction.value)
                'R' -> currentDirection = rotateRight(instruction.value)
                'F' -> move(currentDirection, instruction.value)
            }
        }

        private fun move(direction: Direction, value: Int) {
            eastWest += value * direction.dx
            northSouth += value * direction.dy
        }

        private fun rotateLeft(value: Int) = currentDirection.getNextDirection(360 - value)
        private fun rotateRight(value: Int) = currentDirection.getNextDirection(value)

        val path: Int
            get() = abs(eastWest) + abs(northSouth)
    }

    class TaxicabGeometryWithWayPoint(startWaypointEastWest: Int = -10, startWaypointNorthSouth: Int = 1) {
        var waypointEastWest = startWaypointEastWest
        var waypointNorthSouth = startWaypointNorthSouth
        private var eastWest = 0
        private var northSouth = 0

        fun move(instructions: List<Instruction>) {
            instructions.forEach { instruction -> move(instruction) }
        }

        fun move(instruction: Instruction) {
            when (instruction.action) {
                'N', 'E', 'S', 'W' -> moveWaypoint(Direction.byLabel(instruction.action), instruction.value)
                'L' -> rotateWaypointLeft(instruction.value)
                'R' -> rotateWaypointRight(instruction.value)
                'F' -> moveForward(instruction.value)
            }
        }

        private fun moveWaypoint(direction: Direction, value: Int) {
            waypointEastWest += value * direction.dx
            waypointNorthSouth += value * direction.dy
        }

        private fun rotateWaypointLeft(value: Int) = when (value) {
            90 -> {
                val previousWaypointEastWest = waypointEastWest
                waypointEastWest = waypointNorthSouth
                waypointNorthSouth = -previousWaypointEastWest
            }
            180 -> {
                waypointEastWest = -waypointEastWest
                waypointNorthSouth = -waypointNorthSouth
            }
            270 -> {
                val previousWaypointEastWest = waypointEastWest
                waypointEastWest = -waypointNorthSouth
                waypointNorthSouth = previousWaypointEastWest
            }

            else -> {
            }
        }

        private fun rotateWaypointRight(value: Int) = when (value) {
            90 -> {
                val previousWaypointEastWest = waypointEastWest
                waypointEastWest = -waypointNorthSouth
                waypointNorthSouth = previousWaypointEastWest
            }
            180 -> {
                waypointEastWest = -waypointEastWest
                waypointNorthSouth = -waypointNorthSouth
            }
            270 -> {
                val previousWaypointEastWest = waypointEastWest
                waypointEastWest = waypointNorthSouth
                waypointNorthSouth = 0 - previousWaypointEastWest
            }

            else -> {
            }
        }

        private fun moveForward(value: Int) {
            eastWest += waypointEastWest * value
            northSouth += waypointNorthSouth * value
        }

        val path: Int
            get() = abs(eastWest) + abs(northSouth)
    }

    enum class Direction(val label: Char, val dx: Int, val dy: Int) {
        NORTH('N', 0, 1),
        EAST('E', -1, 0),
        SOUTH('S', 0, -1),
        WEST('W', 1, 0);

        companion object {
            fun byLabel(label: Char) = entries.first { direction -> label == direction.label }
        }

        fun getNextDirection(value: Int): Direction = entries[(ordinal + (value / 90)) % 4]
    }
}