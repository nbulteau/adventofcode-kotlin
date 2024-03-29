package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day02/training.txt")
    val data = readFileDirectlyAsText("/year2022/day02/data.txt")

    val lines = data.split("\n")

    val day = Day02(2022, 2, "Rock Paper Scissors")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day02(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private val shapeYouSelected = mapOf('X' to 1, 'Y' to 2, 'Z' to 3)

    // A rock, B Paper, C Scissors
    // X Rock, Y Paper, Z Scissors
    private val score = mapOf(
        "A X" to 3,
        "A Y" to 6,
        "A Z" to 0,
        "B X" to 0,
        "B Y" to 3,
        "B Z" to 6,
        "C X" to 6,
        "C Y" to 0,
        "C Z" to 3,
    )

    // 1 for Rock, 2 for Paper, and 3 for Scissors
    // 0 if you lost, 3 if the round was a draw, and 6 if you won
    fun partOne(lines: List<String>): Int {
        return lines.sumOf { line ->
            shapeYouSelected.getValue(line[2]) + score.getValue(line)
        }
    }

    private val valueScore = mapOf('X' to 0, 'Y' to 3, 'Z' to 6)

    // A rock, B Paper, C Scissors
    // X lose, Y draw, Z win
    private val youChoose = mapOf(
        "A X" to 3,
        "A Y" to 1,
        "A Z" to 2,
        "B X" to 1,
        "B Y" to 2,
        "B Z" to 3,
        "C X" to 2,
        "C Y" to 3,
        "C Z" to 1, // scissors win with rock = 1
    )

    fun partTwo(lines: List<String>): Int {
        return lines.sumOf { line ->
            youChoose.getValue(line) + valueScore.getValue(line[2])
        }
    }
}


