package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// --- Day 5: Sunny with a Chance of Asteroids ---
// https://adventofcode.com/2019/day/5
fun main() {

    val training = readFileDirectlyAsText("/year2019/day05/training.txt")

    val data = readFileDirectlyAsText("/year2019/day05/data.txt")

    val inputs = data.split(",").map { it.toLong() }
    val day = Day05(2024, 25)

    prettyPrintPartOne { day.partOne(inputs) }
    prettyPrintPartTwo { day.partTwo(inputs) }
}

class Day05(year: Int, day: Int, title: String = "Sunny with a Chance of Asteroids") : AdventOfCodeDay(year, day, title)  {

    /**
     * Part One: TEST Diagnostic Program
     *
     * Run the Thermal Environment Supervision Terminal (TEST) diagnostic program
     * with input value 1 to test the air conditioner unit.
     * The program will output several diagnostic codes (should all be 0) followed
     * by the final diagnostic code which is the answer.
     *
     * @param inputs The IntCode program to execute
     * @return The final diagnostic code (last output value)
     */
    fun partOne(inputs: List<Long>): Long {
        val intCodeProgram = IntCodeProgram(inputs)

        // Run the diagnostic with input ID 1 (air conditioner unit)
        val output = intCodeProgram.execute(mutableListOf(1L))

        // Return the last output value (the diagnostic code)
        return output.last()
    }

    /**
     * Part Two: Thermal Radiator Controller Diagnostic
     *
     * Run the diagnostic program with input value 5 to test the thermal radiator
     * controller. This tests the new jump and comparison instructions (opcodes 5-8).
     * The program should output a single diagnostic code.
     *
     * @param inputs The IntCode program to execute
     * @return The diagnostic code for the thermal radiator controller
     */
    fun partTwo(inputs: List<Long>): Long {
        val intCodeProgram = IntCodeProgram(inputs)

        // Run the diagnostic with input ID 5 (thermal radiator controller)
        val output = intCodeProgram.execute(mutableListOf(5L))

        // Return the last output value (the diagnostic code)
        return output.last()
    }
}


