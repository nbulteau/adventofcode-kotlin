package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText

// --- Day 6: Custom Customs ---
// https://adventofcode.com/2020/day/6
fun main() {

    val training = readFileDirectlyAsText("/year2020/day06/training.txt.txt")
    val data = readFileDirectlyAsText("/year2020/day06/data.txt")

    val answers = data.split("\n\n")

    // Part One
    partOne(answers)

    // Part Two
    partTwo(answers)
}

private fun partOne(answers: List<String>) {
    val count = answers.sumOf { str -> str.toSet().count { char -> char != '\n' } }
    println("Part one = $count")
}

private fun partTwo(answers: List<String>) {
    val count = answers.sumOf { answer ->
        val nbLines = answer.count { char -> char == '\n' } + 1
        answer.replace("\n", "")
            .groupBy { char -> char }
            .filterValues { chars -> chars.size == nbLines }
            .count()
    }
    println("Part two = $count")
}


