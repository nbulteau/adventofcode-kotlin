package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.readFileDirectlyAsText

// https://adventofcode.com/2021/day/1
fun main(args: Array<String>) {

    val training = readFileDirectlyAsText("/year2021/day01/training.txt")
    val data = readFileDirectlyAsText("/year2021/day01/data.txt")

    val numbers = data.split("\n").map { string -> string.toInt() }

    // Part One
    Day01().partOne(numbers)

    // Part Two
    Day01().partTwo(numbers)
}

private class Day01 {

    fun partOne(numbers: List<Int>) {
        var increase = 0
        for (index in 0 ..numbers.size - 2) {
            if (numbers[index + 1] - numbers[index] > 0) {
                increase++
            }
        }

        println("Part one answer = $increase")
    }

    fun partTwo(numbers: List<Int>) {
        var increase = 0
        for (index in 0 ..numbers.size - 4) {
            val number1 = numbers[index] + numbers[index+1] + numbers[index+2]
            val number2 = numbers[index+1] + numbers[index+2] + numbers[index+3]

            if (number2 > number1) {
                increase++
            }
        }

        println("Part two answer = $increase")
    }
}


