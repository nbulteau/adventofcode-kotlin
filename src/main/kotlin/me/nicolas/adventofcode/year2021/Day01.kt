package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2021/day/1
@OptIn(ExperimentalTime::class)
fun main() {

    val training = readFileDirectlyAsText("/year2021/day01/training.txt")
    val data = readFileDirectlyAsText("/year2021/day01/data.txt")

    val numbers = data.split("\n").map { string -> string.toInt() }

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day01().partOne(numbers) })

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day01().partTwo(numbers) })
}

private class Day01 {

    fun partOne(numbers: List<Int>): Int {
        var increase = 0
        for (index in 0..numbers.size - 2) {
            if (numbers[index + 1] - numbers[index] > 0) {
                increase++
            }
        }

        return increase
    }

    fun partTwo(numbers: List<Int>): Int {
        var increase = 0
        for (index in 0..numbers.size - 4) {
            val number1 = numbers[index] + numbers[index + 1] + numbers[index + 2]
            val number2 = numbers[index + 1] + numbers[index + 2] + numbers[index + 3]

            if (number2 > number1) {
                increase++
            }
        }

        return increase
    }
}


