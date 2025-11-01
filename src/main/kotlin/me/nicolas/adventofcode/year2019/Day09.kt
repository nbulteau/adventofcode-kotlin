package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2019/day/9
fun main() {

    val training = readFileDirectlyAsText("/year2019/day09/training.txt")
    val data = readFileDirectlyAsText("/year2019/day09/data.txt")

    val day = Day09(data)
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo() }
}

class Day09(private val input: String) {

    private fun parseProgram(): List<Long> {
        return input.trim().split(",").map { it.toLong() }
    }

    /**
     * Part 1: Run the BOOST program in test mode
     *
     * The BOOST program performs a series of checks on each opcode to verify
     * the IntCode computer is functioning correctly. When run in test mode (input = 1),
     * it should output any malfunctioning opcodes, or if everything is working correctly,
     * it should output a single value: the BOOST keycode.
     *
     * @return The BOOST keycode if all opcodes are functioning correctly
     */
    fun partOne(): Long {
        val program = parseProgram()
        val intCodeProgram = IntCodeProgram(program)
        val output = intCodeProgram.execute(mutableListOf(1L))
        return output.last()
    }

    /**
     * Part 2: Run the BOOST program in sensor boost mode
     *
     * Once the IntCode computer passes all tests in Part 1, we can run the BOOST
     * program in sensor boost mode (input = 2) to actually boost the sensors.
     * This outputs the coordinates of the distress signal from the Ceres monitoring station.
     *
     * @return The coordinates of the distress signal
     */
    fun partTwo(): Long {
        val program = parseProgram()
        val intCodeProgram = IntCodeProgram(program)
        val output = intCodeProgram.execute(mutableListOf(2L))
        return output.last()
    }
}