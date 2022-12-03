package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2022/day/1
@OptIn(ExperimentalTime::class)
fun main() {

    val training = readFileDirectlyAsText("/year2022/day01/training.txt")
    val data = readFileDirectlyAsText("/year2022/day01/data.txt")

    val numbers = data.split("\n")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day01().partOne(numbers) })

    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day01().partTwo(numbers) })
}

class Day01 {
    fun partOne(numbers: List<String>): Int {
        return processCalories(numbers)
            .maxOf { it }
    }

    fun partTwo(numbers: List<String>): Int {
        return processCalories(numbers)
            .sortedDescending()
            .take(3)
            .sum()
    }

    private fun processCalories(numbers: List<String>): List<Int> {
        val calories = mutableListOf<Int>()
        var sum = 0
        for (number in numbers) {
            if (number.isNotBlank()) {
                sum += number.toInt()
            } else {
                calories.add(sum)
                sum = 0
            }
        }
        calories.add(sum)

        return calories
    }
}

