package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// https://adventofcode.com/2018/day/9
fun main() {
    val data = readFileDirectlyAsText("/year2018/day09/data.txt")
    val day = Day09(2018, 9, "Marble Mania")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day09(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        val (players, totalMarbles) = extractInts(data)

        return solve(players, totalMarbles)
    }
    fun partTwo(data: String): Long {
        val (players, totalMarbles) = extractInts(data)

        return solve(players, totalMarbles * 100)
    }

    private fun extractInts(data: String): List<Int> {
        return "-?\\d+".toRegex().findAll(data).map { it.value.toInt() }.toList()
    }

    private fun solve(players: Int, totalMarbles: Int): Long {
        val circle = ArrayDeque<Int>().also { it.add(0) }
        val scores = MutableList(players) { 0L }
        for (marble in 1..totalMarbles) {
            // First, the current player keeps the marble they would have placed, adding it to their score.
            // In addition, the marble 7 marbles counter-clockwise from the current marble is removed from the circle
            // and also added to the current player's score.
            if (marble % 23 == 0) {
                scores[marble % players] += marble + with(circle) {
                    circle.shift(-7)
                    removeFirst().toLong()
                }
                circle.shift(1)
            }
            // Elf takes a turn placing the lowest-numbered remaining marble into the circle between the marbles that are 1 and 2 marbles clockwise of the current marble.
            // The marble that was just placed then becomes the current marble.
            else {
                circle.shift(1)
                circle.addFirst(marble)
            }
        }

        return scores.max()
    }

    // shift function rotates the circle of marbles by a given number of steps
    private fun ArrayDeque<Int>.shift(steps: Int) {
        if (steps < 0) {
            // If the number of steps is negative, rotate the circle counter-clockwise
            repeat(abs(steps)) {
                addLast(removeFirst())
            }
        } else {
            // Otherwise, rotate the circle clockwise
            repeat(steps) {
                addFirst(removeLast())
            }
        }
    }
}

