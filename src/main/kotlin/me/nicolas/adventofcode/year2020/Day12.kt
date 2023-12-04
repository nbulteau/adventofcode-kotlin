package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// --- Day 12: Rain Risk ---
// https://adventofcode.com/2020/day/12
fun main() {

    println("--- Day 12: Rain Risk ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day12/training.txt")
    val data = readFileDirectlyAsText("/year2020/day12/data.txt")

    val instructions =
        data.split("\n").map { line -> Day12.Instruction(line.elementAt(0), line.substring(1).toInt()) }

    // Part One
    Day12().partOne(instructions)

    // Part Two
    Day12().partTwo(instructions)
}

class Day12 {

    fun partOne(instructions: List<Instruction>) {

        val taxicabGeometry = TaxicabGeometry()
        taxicabGeometry.move(instructions)

        println("Part one = ${taxicabGeometry.path}")
    }

    fun partTwo(instructions: List<Instruction>) {

        val taxicabGeometry = TaxicabGeometryWithWayPoint()
        taxicabGeometry.move(instructions)

        println("Part two = ${taxicabGeometry.path}")
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

        private fun rotateLeft(value: Int) =
            currentDirection.getNextDirection(360 - value)

        private fun rotateRight(value: Int) =
            currentDirection.getNextDirection(value)

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
                waypointNorthSouth = 0 - previousWaypointEastWest
            }

            180 -> {
                waypointEastWest = 0 - waypointEastWest
                waypointNorthSouth = 0 - waypointNorthSouth
            }

            270 -> {
                val previousWaypointEastWest = waypointEastWest
                waypointEastWest = 0 - waypointNorthSouth
                waypointNorthSouth = previousWaypointEastWest
            }

            else -> {
            }
        }

        private fun rotateWaypointRight(value: Int) = when (value) {
            90 -> {
                val previousWaypointEastWest = waypointEastWest
                waypointEastWest = 0 - waypointNorthSouth
                waypointNorthSouth = previousWaypointEastWest
            }

            180 -> {
                waypointEastWest = 0 - waypointEastWest
                waypointNorthSouth = 0 - waypointNorthSouth
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
            fun byLabel(label: Char) = values().first { direction -> label == direction.label }
        }

        fun getNextDirection(value: Int): Direction = values()[(ordinal + (value / 90)) % 4]
    }
}