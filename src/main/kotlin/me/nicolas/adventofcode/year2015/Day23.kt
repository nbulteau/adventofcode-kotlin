package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 23: Opening the Turing Lock ---
// https://adventofcode.com/2015/day/23
fun main() {
    val data = readFileDirectlyAsText("/year2015/day23/data.txt")
    val day = Day23(2015, 23)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day23(year: Int, day: Int, title: String = "Opening the Turing Lock") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val instructions = data.lines()
        val registers = mutableMapOf("a" to 0, "b" to 0)
        executeInstructions(instructions, registers)

        return registers["b"] ?: 0
    }

    fun partTwo(data: String): Int {
        val instructions = data.lines()
        val registers = mutableMapOf("a" to 1, "b" to 0)
        executeInstructions(instructions, registers)

        return registers["b"] ?: 0
    }

    // Execute the instructions and update the registers
    private fun executeInstructions(instructions: List<String>, registers: MutableMap<String, Int>) {
        var pointer = 0
        while (pointer in instructions.indices) {
            val parts = instructions[pointer].split(" ", ", ")
            when (parts[0]) {
                "hlf" -> {
                    registers[parts[1]] = (registers[parts[1]] ?: 0) / 2
                    pointer++
                }
                "tpl" -> {
                    registers[parts[1]] = (registers[parts[1]] ?: 0) * 3
                    pointer++
                }
                "inc" -> {
                    registers[parts[1]] = (registers[parts[1]] ?: 0) + 1
                    pointer++
                }
                "jmp" -> {
                    pointer += parts[1].toInt()
                }
                "jie" -> {
                    if ((registers[parts[1]] ?: 0) % 2 == 0) {
                        pointer += parts[2].toInt()
                    } else {
                        pointer++
                    }
                }
                "jio" -> {
                    if ((registers[parts[1]] ?: 0) == 1) {
                        pointer += parts[2].toInt()
                    } else {
                        pointer++
                    }
                }
            }
        }
    }
}