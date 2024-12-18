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
    //prettyPrintPartTwo { day.partTwoBruteForce(data) }
}

class Day17(year: Int, day: Int, title: String = "Chronospatial Computer") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): String {
        val (computer, program) = parseInput(data)

        return computer.run(program).joinToString(",")
    }

    fun partTwo(data: String): Long {
        val (_, program) = parseInput(data)

        return findAMatchingOutput(program, program)
    }

    // Brute force solution: not a good idea to run this
    fun partTwoBruteForce(data: String): Long {
        val (_, program) = parseInput(data)

        return LongStream.range(0L, Long.MAX_VALUE).filter { registerValue ->
            println("Trying Register A: $registerValue")
            val computer = Computer(registerA = registerValue, registerB = 0L, registerC = 0L)
            val output = computer.run(program)
            // println("Register A: $registerValue, Output: $output, binary: ${output.joinToString("") { it.toString(2) }}")
            if (output == program) {
                println("Solution Register A: $registerValue")
            }
            output == program
        }.findFirst().asLong
    }

    private data class Computer(
        var registerA: Long,
        var registerB: Long,
        var registerC: Long,
    ) {
        fun run(program: List<Int>): MutableList<Int> {
            var instructionPointer = 0
            val outputs = mutableListOf<Int>()

            while (instructionPointer < program.size) {
                val instruction = program[instructionPointer]
                val operand = program[instructionPointer + 1]
                when (instruction) {
                    0 -> registerA = registerA shr combo(operand.toLong()).toInt()
                    1 -> registerB = registerB xor operand.toLong()
                    2 -> registerB = combo(operand.toLong()) % 8
                    3 -> if (registerA != 0L) {
                        instructionPointer = operand - 2
                    }

                    4 -> registerB = registerB xor registerC
                    5 -> outputs.add((combo(operand.toLong()) % 8).toInt())
                    6 -> registerB = registerA shr combo(operand.toLong()).toInt()
                    7 -> registerC = registerA shr combo(operand.toLong()).toInt()
                    else -> throw IllegalArgumentException("Invalid instruction: $instruction")
                }
                instructionPointer += 2
            }
            return outputs
        }

        private fun combo(operand: Long) = when (operand) {
            in 0..3 -> operand
            4L -> registerA
            5L -> registerB
            6L -> registerC
            else -> throw IllegalArgumentException("Invalid operand: $operand")
        }
    }

    // This program loops while a > 0, dividing a by 8 every iteration.
    // Each iteration outputs one value, solely derived from the value of a % 8 at the beginning of the iteration.
    // Thus each iteration is completely independent and can be solved for in isolation.
    // The solution below starts by finding the value of a that outputs solely the last value of the program by starting with a=0 and incrementing by one each attempt.
    // The value of a that finds the last instruction is multiplied by 8, then used as the starting point for a search to find the starting value of a which will output the last two instructions.
    // This process is repeated until all instructions are output.

    // Program: 2,4,1,1,7,5,1,4,0,3,4,5,5,5,3,0
    // while a != 0 {
    //    b = a % 8
    //    b = b ^ 5
    //    c = a / (1 << b)
    //    b = b ^ c
    //    b = b ^ 6
    //    a = a / (1 << 3)
    //    out.add(b % 8)
    // }
    private fun findAMatchingOutput(program: List<Int>, target: List<Int>): Long {
        var aStart = if (target.size == 1) {
            0
        } else {
            8 * findAMatchingOutput(program, target.subList(1, target.size))
        }

        while (Computer(registerA = aStart, registerB = 0, registerC = 0).run(program) != target) {
            aStart++
        }

        return aStart
    }

    private fun parseInput(data: String): Pair<Computer, List<Int>> {
        val lines = data.lines()
        return Computer(
            registerA = lines[0].substringAfterLast(" ").toLong(),
            registerB = lines[1].substringAfterLast(" ").toLong(),
            registerC = lines[2].substringAfterLast(" ").toLong(),
        ) to lines[4].substringAfterLast(" ").split(",").map { instruction -> instruction.toInt() }
    }
}