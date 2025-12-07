package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 2: 1202 Program Alarm ---
// https://adventofcode.com/2019/day/2
fun main() {
    val data = readFileDirectlyAsText("/year2019/day02/data.txt")
    val day = Day02(2019, 1)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day02(year: Int, day: Int, title: String = "1202 Program Alarm") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val program: IntArray = data.split(",").map { it.toInt() }.toIntArray()

        program[1] = 12
        program[2] = 2
        program.execute()

        return program[0]
    }

    fun partTwo(data: String): Int {
        val program: IntArray = data.split(",").map { it.toInt() }.toIntArray()

        val expected = 19690720

        for (noun in 0..99) {
            for (verb in 0..99) {
                val programToTest = program.clone()
                programToTest[1] = noun
                programToTest[2] = verb
                programToTest.execute()

                if (programToTest[0] == expected) {
                    return 100 * noun + verb
                }
            }
        }
        return 0
    }

    private fun IntArray.execute() {
        var index = 0

        while (this[index] != 99) {
            when (this[index]) {
                1 -> {
                    this[this[index + 3]] = this[this[index + 1]] + this[this[index + 2]]
                }

                2 -> {
                    this[this[index + 3]] = this[this[index + 1]] * this[this[index + 2]]
                }
            }
            index += 4
        }
    }
}

