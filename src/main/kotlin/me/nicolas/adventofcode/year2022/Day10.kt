package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day10/training.txt")
    val data = readFileDirectlyAsText("/year2022/day10/data.txt")

    val lines = data.split("\n")

    val day = Day10("--- Day 10: Cathode-Ray Tube ---", "https://adventofcode.com/2022/day/10")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
    prettyPrintPartTwo { day.partTwoBis(lines) }

}

private class Day10(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(lines: List<String>): Int {
        val interestingCycles = listOf(20, 60, 100, 140, 180, 220)
        var cycle = 1
        var result = 0
        var registerX = 1

        /**
         * For now, consider the signal strength (the cycle number multiplied by the value of the X register)
         * during the 20th cycle and every 40 cycles after that
         * (that is, during the 20th, 60th, 100th, 140th, 180th, and 220th cycles).
         */
        fun process() {
            if (cycle in interestingCycles) {
                result += cycle * registerX
            }
            cycle++
        }

        for (line in lines) {
            process()
            if (line.startsWith("addx")) {
                process()
                registerX += line.substringAfter(" ").toInt()
            }
        }
        println("Nb cycles : $cycle")
        return result
    }

    fun partTwo(lines: List<String>): Int {
        var registerX = 1
        var cycle = 1
        val screen = Array(6) { CharArray(40) { ' ' } }

        fun process() {
            val rowX = (cycle - 1) % 40
            if (rowX in (registerX - 1)..(registerX + 1)) {
                val rowY = (cycle - 1) / 40
                screen[rowY][rowX] = '#'
            }
            cycle++
        }

        for (line in lines) {
            process()
            if (line.startsWith("addx")) {
                process()
                registerX += line.substringAfter(" ").toInt()
            }
        }

        screen.display()

        return 0
    }

    fun partTwoBis(lines: List<String>): Int {
        var registerX = 1
        var cycle = 1
        val screen = mutableMapOf<Pair<Int, Int>, Char>()

        fun process() {
            val rowX = (cycle - 1) % 40
            if (rowX in (registerX - 1)..(registerX + 1)) {
                val rowY = (cycle - 1) / 40
                screen[Pair(rowX, rowY)] = '#'
            }
            cycle++
        }

        for (line in lines) {
            process()
            if (line.startsWith("addx")) {
                process()
                registerX += line.substringAfter(" ").toInt()
            }
        }

        screen.display()

        return 0
    }

    private fun Array<CharArray>.display() {
        for (y in this.indices) {
            for (x in this.first().indices) {
                print(this[y][x])
            }
            println()
        }
    }

    fun Map<Pair<Int, Int>, Char>.display() {
        for (y in 0..keys.maxOf { it.second }) {
            for (x in 0..keys.maxOf { it.first }) {
                print(getOrDefault(Pair(x, y), " "))
            }
            println()
        }
    }
}