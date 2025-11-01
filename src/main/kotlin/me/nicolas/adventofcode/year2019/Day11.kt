package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 11: Space Police ---
// https://adventofcode.com/2019/day/11
fun main() {
    val data = readFileDirectlyAsText("/year2019/day11/data.txt")
    val day = Day11(2019, 11)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day11(year: Int, day: Int, title: String = "Space Police") : AdventOfCodeDay(year, day, title) {

    data class Position(val x: Int, val y: Int)

    enum class Direction(val dx: Int, val dy: Int) {
        UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

        fun turnLeft(): Direction = when (this) {
            UP -> LEFT
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
        }

        fun turnRight(): Direction = when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    private fun parseProgram(data: String) = data.trim().split(",").map { it.toLong() }

    private fun runRobot(program: List<Long>, startOnWhite: Boolean = false): Map<Position, Int> {
        val intCodeProgram = IntCodeProgram(program)
        val panels = mutableMapOf<Position, Int>()
        var position = Position(0, 0)
        var direction = Direction.UP

        if (startOnWhite) {
            panels[position] = 1
        }

        while (!intCodeProgram.isHalted()) {
            // Provide current panel color as input (0 for black, 1 for white)
            val currentColor = panels.getOrDefault(position, 0)
            val input = mutableListOf(currentColor.toLong())

            // Get paint color output
            val paintColor = intCodeProgram.executeUntilOutput(input) ?: break
            panels[position] = paintColor.toInt()

            // Get turn direction output
            val turnDirection = intCodeProgram.executeUntilOutput(mutableListOf()) ?: break

            // Turn the robot
            direction = if (turnDirection == 0L) direction.turnLeft() else direction.turnRight()

            // Move forward
            position = Position(position.x + direction.dx, position.y + direction.dy)
        }

        return panels
    }

    /**
     * Part One: Count the number of panels painted at least once.
     *
     * The emergency hull painting robot starts on a black panel and follows
     * instructions from the IntCode program to paint panels and navigate.
     * Each panel is either black (0) or white (1), and the robot tracks
     * all panels it has painted.
     *
     * The robot's behavior:
     * - Reads the current panel color
     * - Sends it to the IntCode program
     * - Receives paint instruction and paints the panel
     * - Receives turn instruction (left or right)
     * - Turns and moves forward one panel
     * - Repeats until the program halts
     *
     * @param data The IntCode program as comma-separated long integers
     * @return The number of unique panels painted at least once
     */
    fun partOne(data: String): Int {
        val program = parseProgram(data)
        val panels = runRobot(program)
        return panels.size
    }

    /**
     * Part Two: Paint the hull starting on a white panel and render the result.
     *
     * This time the robot starts on a white panel instead of black. After
     * the robot completes its painting, the painted panels form a registration
     * identifier that must be rendered as ASCII art.
     *
     * The identifier is rendered as:
     * - White panels (1) are shown as '#'
     * - Black panels (0) are shown as ' ' (space)
     *
     * The output is a visual representation of the painted hull that reveals
     * the registration identifier required by Space Law.
     *
     * @param data The IntCode program as comma-separated long integers
     * @return ASCII art representation of the painted panels
     */
    fun partTwo(data: String): String {
        val program = parseProgram(data)
        val panels = runRobot(program, startOnWhite = true)

        // Find bounds of the painted area
        val minX = panels.keys.minOf { it.x }
        val maxX = panels.keys.maxOf { it.x }
        val minY = panels.keys.minOf { it.y }
        val maxY = panels.keys.maxOf { it.y }

        // Build the image by iterating through all positions in bounds
        val result = StringBuilder("\n")
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val color = panels.getOrDefault(Position(x, y), 0)
                result.append(if (color == 1) '#' else ' ')
            }
            result.append("\n")
        }

        return result.toString()
    }
}