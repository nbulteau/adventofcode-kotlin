package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.stream.LongStream

fun main() {
    val data = readFileDirectlyAsText("/year2024/day17/data.txt")
    val day = Day17(2024, 17)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day17(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    private data class Computer(
        var registerA: Long,
        var registerB: Long,
        var registerC: Long,
    )

    fun partOne(data: String): String {
        val (computer, program) = parseInput(data)
        return computer.run(program)
    }

    fun partTwo(data: String): Long {
        val (_, program) = parseInput(data)
        val programText = program.joinToString(",")




        return LongStream.range(0L, Long.MAX_VALUE).parallel().filter { registerValue ->
            val output = Computer(registerValue, 0, 0).run(program)
            if (output == programText) {
                println("Solution Register A: $registerValue")
            }
            output == programText
        }.findFirst().orElseThrow()
    }

    private fun parseInput(data: String): Pair<Computer, List<Int>> {
        val lines = data.lines()
        return Computer(
            registerA = lines[0].substringAfterLast(" ").toLong(),
            registerB = lines[1].substringAfterLast(" ").toLong(),
            registerC = lines[2].substringAfterLast(" ").toLong(),
        ) to lines[4].substringAfterLast(" ").split(",").map { instruction -> instruction.toInt() }
    }

    private fun Computer.run(program: List<Int>): String {
        var ip = 0
        val outputs = mutableListOf<Int>()

        while (ip < program.size) {
            val instruction = program[ip]
            val operand = program[ip + 1]
            when (instruction) {
                0 -> registerA = registerA shr combo(operand.toLong(), registerA, registerB, registerC).toInt()
                1 -> registerB = registerB xor operand.toLong()
                2 -> registerB = combo(operand.toLong(), registerA, registerB, registerC) % 8
                3 -> if (registerA != 0L) ip = operand - 2
                4 -> registerB = registerB xor registerC
                5 -> outputs.add((combo(operand.toLong(), registerA, registerB, registerC) % 8).toInt())
                6 -> registerB = registerA shr combo(operand.toLong(), registerA, registerB, registerC).toInt()
                7 -> registerC = registerA shr combo(operand.toLong(), registerA, registerB, registerC).toInt()
                else -> throw IllegalArgumentException("Invalid instruction: $instruction")
            }
            ip += 2
        }
        return outputs.joinToString(",")
    }

    fun combo(operand: Long, regA: Long, regB: Long, regC: Long) = when (operand) {
        in 0..3 -> operand
        4L -> regA
        5L -> regB
        6L -> regC
        else -> throw IllegalArgumentException("Invalid operand: $operand")
    }

}