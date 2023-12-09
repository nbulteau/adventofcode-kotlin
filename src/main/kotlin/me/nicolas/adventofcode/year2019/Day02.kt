package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// --- Day 2: 1202 Program Alarm ---
// https://adventofcode.com/2019/day/2
fun main() {

    println("--- Day 2: 1202 Program Alarm ---")
    println()

    val training = readFileDirectlyAsText("/year2019/day02/training.txt")
    val data = readFileDirectlyAsText("/year2019/day02/data.txt")

    val intCodeProgram: IntArray = data.split(",").map { it.toInt() }.toIntArray()

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day02().partOne(intCodeProgram.clone()) })

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day02().partTwo(intCodeProgram.clone()) })
}

private class Day02 {
    fun partOne(program: IntArray): Int {
        program[1] = 12
        program[2] = 2
        program.execute()

        return program[0]
    }

    fun partTwo(program: IntArray): Int {
        val expected = 19690720

        for (noun in 0..99) {
            for (verb in 0..99) {
                val programToTest = program.clone()
                programToTest[1] = noun
                programToTest[2] = verb
                programToTest.execute()

                if (programToTest[0] == expected) {
                    return 100 * noun + verb
                }
            }
        }
        return 0
    }

    private fun IntArray.execute() {
        var index = 0

        while (this[index] != 99) {
            when (this[index]) {
                1 -> {
                    this[this[index + 3]] = this[this[index + 1]] + this[this[index + 2]]
                }

                2 -> {
                    this[this[index + 3]] = this[this[index + 1]] * this[this[index + 2]]
                }
            }
            index += 4
        }
    }
}

