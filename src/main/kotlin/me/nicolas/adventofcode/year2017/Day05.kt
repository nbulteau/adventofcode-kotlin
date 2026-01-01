package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 5: A Maze of Twisty Trampolines, All Alike ---
// https://adventofcode.com/2017/day/5
fun main() {
    val data = readFileDirectlyAsText("/year2017/day05/data.txt")
    val day = Day05()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day05(year: Int = 2017, day: Int = 5, title: String = "A Maze of Twisty Trampolines, All Alike") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val instructions = data.lines().map { it.toInt() }.toMutableList()
        var steps = 0
        var position = 0
        while (position in instructions.indices) {
            val jump = instructions[position]
            instructions[position]++
            position += jump
            steps++
        }
        return steps
    }

    fun partTwo(data: String): Int {
        val instructions = data.lines().map { it.toInt() }.toMutableList()
        var steps = 0
        var position = 0
        while (position in instructions.indices) {
            val jump = instructions[position]
            if (jump >= 3) {
                instructions[position]--
            } else {
                instructions[position]++
            }
            position += jump
            steps++
        }
        return steps
    }
}