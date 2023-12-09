package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day01/training.txt")
    val data = readFileDirectlyAsText("/year2022/day01/data.txt")

    val lines = data.split("\n")

    val day = Day01(2022, 1, "Calorie Counting")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day01(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
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

