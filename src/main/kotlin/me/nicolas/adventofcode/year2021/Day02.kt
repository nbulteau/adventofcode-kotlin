package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2021/day/2
fun main() {

    val training = readFileDirectlyAsText("/year2021/day02/training.txt")
    val data = readFileDirectlyAsText("/year2021/day02/data.txt")

    val lines = data.split("\n")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day02().partOne(lines) })

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day02().partTwo(lines) })
}

class Day02 {

    fun partOne(lines: List<String>): Int {
        var horizontal = 0
        var depth = 0

        for (line in lines) {
            val instruction = line.split(" ")
            when (instruction[0]) {
                "forward" -> horizontal += instruction[1].toInt()
                "down" -> depth -= instruction[1].toInt()
                "up" -> depth += instruction[1].toInt()
            }
        }
        return abs(horizontal) * abs(depth)
    }

    fun partTwo(lines: List<String>): Int {

        var horizontal = 0
        var depth = 0
        var aim = 0

        for (line in lines) {
            val instruction = line.split(" ")
            when (instruction[0]) {
                "forward" -> {
                    horizontal += instruction[1].toInt()
                    depth += (aim * instruction[1].toInt())
                }

                "down" -> aim += instruction[1].toInt()
                "up" -> aim -= instruction[1].toInt()
            }
        }
        return abs(horizontal) * abs(depth)
    }
}


