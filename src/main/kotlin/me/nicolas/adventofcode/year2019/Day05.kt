package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


// --- Day 5: Sunny with a Chance of Asteroids ---
// https://adventofcode.com/2019/day/5
fun main() {

    val training = readFileDirectlyAsText("/year2019/day05/training.txt")

    val data = readFileDirectlyAsText("/year2019/day05/data.txt")

    val inputs = data.split(",").map { it.toInt() }.toIntArray()

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day05().partOne(inputs.clone()) })
    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day05().partTwo(inputs.clone()) })
}

class Day05 {

    fun partOne(inputs: IntArray): Int {
        val intCodeProgram = IntCodeProgram(inputs)

        val output = intCodeProgram.execute(mutableListOf(1))

        return output.last()
    }

    fun partTwo(inputs: IntArray): Int {
        val intCodeProgram = IntCodeProgram(inputs)

        val output = intCodeProgram.execute(mutableListOf(5))

        return output.last()
    }
}


