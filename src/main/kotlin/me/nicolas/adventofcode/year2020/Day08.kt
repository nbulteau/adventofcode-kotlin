package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 08: Handheld Halting ---
// https://adventofcode.com/2020/day/8
fun main() {
    val data = readFileDirectlyAsText("/year2020/day08/data.txt")
    val day = Day08(2020, 8, "Handheld Halting")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day08(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val instructions = data.split("\n").filter { it.isNotEmpty() }
        val (accumulator, _) = runProgram(instructions)
        return accumulator
    }

    fun partTwo(data: String): Int {
        val instructions = data.split("\n").filter { it.isNotEmpty() }
        for (i in instructions.indices) {
            val modifiedInstructions = instructions.toMutableList()
            val instruction = instructions[i]
            when {
                instruction.startsWith("nop") -> modifiedInstructions[i] = instruction.replace("nop", "jmp")
                instruction.startsWith("jmp") -> modifiedInstructions[i] = instruction.replace("jmp", "nop")
                else -> continue
            }
            val (accumulator, terminated) = runProgram(modifiedInstructions)
            if (terminated) {
                return accumulator
            }
        }
        throw IllegalStateException("No solution found")
    }

    private fun runProgram(instructions: List<String>): Pair<Int, Boolean> {
        var accumulator = 0
        var pointer = 0
        val visited = mutableSetOf<Int>()

        while (pointer < instructions.size && pointer !in visited) {
            visited.add(pointer)
            val instruction = instructions[pointer]
            val (operation, argument) = instruction.split(" ")
            when (operation) {
                "acc" -> {
                    accumulator += argument.toInt()
                    pointer++
                }
                "jmp" -> pointer += argument.toInt()
                "nop" -> pointer++
            }
        }
        return Pair(accumulator, pointer == instructions.size)
    }
}
