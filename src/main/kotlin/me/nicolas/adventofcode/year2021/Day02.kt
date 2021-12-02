package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.math.abs

// https://adventofcode.com/2021/day/2
fun main() {

    val training = readFileDirectlyAsText("/year2021/day02/training.txt")
    val data = readFileDirectlyAsText("/year2021/day02/data.txt")

    val lines = data.split("\n")

    // Part One
    Day02().partOne(lines)

    // Part Two
    Day02().partTwo(lines)
}

private class Day02 {

    fun partOne(lines: List<String>) {
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
        val result = abs(horizontal) * abs(depth)

        println("Part one answer = $result")
    }

    fun partTwo(lines: List<String>) {

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
        val result = abs(horizontal) * abs(depth)

        println("Part two answer = $result")
    }
}


