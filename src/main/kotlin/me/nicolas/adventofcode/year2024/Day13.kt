package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 13: ---
// https://adventofcode.com/2024/day/13
fun main() {
    val data = readFileDirectlyAsText("/year2024/day13/data.txt")
    val day = Day13(2024, 13)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day13(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    private data class Button(val dx: Long, val dy: Long)

    private data class Prize(val x: Long, val y: Long)

    private fun findCheapestWayToWinBruteForce(prize: Prize, buttonA: Button, buttonB: Button): Int {
        var minTokens = Int.MAX_VALUE

        for (aPresses in 0..100) {
            for (bPresses in 0..100) {
                val finalX = aPresses * buttonA.dx + bPresses * buttonB.dx
                val finalY = aPresses * buttonA.dy + bPresses * buttonB.dy
                if (finalX.toLong() == prize.x && finalY.toLong() == prize.y) {
                    // Calculate the total number of tokens required
                    val tokens = aPresses * 3 + bPresses
                    minTokens = minOf(minTokens, tokens)
                }
            }
        }

        return if (minTokens == Int.MAX_VALUE) {
            0
        } else {
            minTokens
        }
    }

    private fun findCheapestWayToWin(prize: Prize, buttonA: Button, buttonB: Button): Long {
        // matrix: [
        //   [ Ax, Bx ]
        //   [ Ay, By ]
        // ]
        // Calculate the determinant of the matrix formed by buttonA and buttonB
        val determinant = buttonA.dx * buttonB.dy - buttonB.dx * buttonA.dy

        // Calculate the inverse of the matrix
        val inverse = listOf(
            listOf(buttonB.dy, -buttonB.dx),
            listOf(-buttonA.dy, buttonA.dx)
        )

        val aPresses = (prize.x * inverse[0][0] + prize.y * inverse[0][1])
        val bPresses = (prize.x * inverse[1][0] + prize.y * inverse[1][1])

        // Check if the solution is valid (both aPresses and bPresses should be divisible by the determinant)
        return if (aPresses % determinant != 0L || bPresses % determinant != 0L) {
            0L
        } else {
            (3 * aPresses + bPresses) / determinant
        }
    }

    private fun parseInput(input: List<String>): List<Triple<Button, Button, Prize>> {
        val machines = mutableListOf<Triple<Button, Button, Prize>>()

        for (i in 0 until input.size step 4) {
            val buttonAConfig = input[i].split("Button A: X+", ", Y+").drop(1).map { it.toLong() }
            val buttonBConfig = input[i + 1].split("Button B: X+", ", Y+").drop(1).map { it.toLong() }
            val prizeLocation = input[i + 2].split("Prize: X=", ", Y=").drop(1).map { it.toLong() }

            val buttonA = Button(buttonAConfig[0], buttonAConfig[1])
            val buttonB = Button(buttonBConfig[0], buttonBConfig[1])
            val prize = Prize(prizeLocation[0], prizeLocation[1])

            machines.add(Triple(buttonA, buttonB, prize))
        }

        return machines
    }

    fun partOne(data: String): Int {
        val machines = parseInput(data.lines())

        return machines.sumOf { (buttonA, buttonB, prize) ->
            findCheapestWayToWinBruteForce(prize, buttonA, buttonB)
        }
    }

    fun partTwo(data: String): Long {
        val machines = parseInput(data.lines())
            .map { machine ->
                // Add 10000000000000 to each prize's X and Y coordinates
                machine.copy(third = Prize(machine.third.x + 10000000000000, machine.third.y + 10000000000000))
            }

        return machines.sumOf { (buttonA, buttonB, prize) ->
            findCheapestWayToWin(prize, buttonA, buttonB)
        }
    }
}