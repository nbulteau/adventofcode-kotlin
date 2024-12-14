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

    /**
     * prizeX = buttonA.dx * aPresses + buttonB.dx * bPresses
     * prizeY = buttonA.dy * aPresses + buttonB.dy * bPresses
     */
    private fun findCheapestWayToWin(prize: Prize, buttonA: Button, buttonB: Button): Long {

        val aPresses =
            (prize.x * buttonB.dy - prize.y * buttonB.dx) / (buttonB.dy * buttonA.dx - buttonB.dx * buttonA.dy)
        val bPresses =
            (prize.x * buttonA.dy - prize.y * buttonA.dx) / (buttonA.dy * buttonB.dx - buttonB.dy * buttonA.dx)

        val isSolved =
            buttonA.dx * aPresses + buttonB.dx * bPresses == prize.x && buttonA.dy * aPresses + buttonB.dy * bPresses == prize.y

        return if (isSolved) {
            3 * aPresses + bPresses
        } else {
            0
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