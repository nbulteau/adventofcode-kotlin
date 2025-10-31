package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// https://adventofcode.com/2018/day/21
fun main() {
    val data = readFileDirectlyAsText("/year2018/day21/data.txt")
    val day = Day21()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day21(year: Int = 2018, day: Int = 21, title: String = "Chronal Conversion") : AdventOfCodeDay(year, day, title) {

    /**
     * Solution approach:
     * - The program contains an 'eqrr' instruction that compares register 0 with another register
     * - The program halts when this comparison is true (equal)
     * - Part One: Find the first value that would cause immediate halt (fewest instructions)
     * - Part Two: Find the last unique value before the cycle repeats (most instructions)
     */

    data class Instruction(val opcode: String, val a: Int, val b: Int, val c: Int)

    private fun parseInput(data: String): Pair<Int, List<Instruction>> {
        val lines = data.lines()
        val ipRegister = lines.first().split(" ")[1].toInt()
        val instructions = lines.drop(1).map { line ->
            val parts = line.split(" ")
            Instruction(parts[0], parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
        }
        return ipRegister to instructions
    }

    private fun executeInstruction(registers: IntArray, instruction: Instruction) {
        val (opcode, a, b, c) = instruction
        when (opcode) {
            "addr" -> registers[c] = registers[a] + registers[b]
            "addi" -> registers[c] = registers[a] + b
            "mulr" -> registers[c] = registers[a] * registers[b]
            "muli" -> registers[c] = registers[a] * b
            "banr" -> registers[c] = registers[a] and registers[b]
            "bani" -> registers[c] = registers[a] and b
            "borr" -> registers[c] = registers[a] or registers[b]
            "bori" -> registers[c] = registers[a] or b
            "setr" -> registers[c] = registers[a]
            "seti" -> registers[c] = a
            "gtir" -> registers[c] = if (a > registers[b]) 1 else 0
            "gtri" -> registers[c] = if (registers[a] > b) 1 else 0
            "gtrr" -> registers[c] = if (registers[a] > registers[b]) 1 else 0
            "eqir" -> registers[c] = if (a == registers[b]) 1 else 0
            "eqri" -> registers[c] = if (registers[a] == b) 1 else 0
            "eqrr" -> registers[c] = if (registers[a] == registers[b]) 1 else 0
        }
    }

    /**
     * Part One: Find the value for register 0 that halts the program with the fewest instructions.
     *
     * The program halts when an 'eqrr' instruction comparing register 0 returns true.
     * The first time we reach this comparison, we return the value in the other register.
     * This is the value register 0 needs to be for immediate halt.
     */
    fun partOne(data: String): Int {
        val (ipRegister, instructions) = parseInput(data)
        // Initialize 6 registers, all starting at 0
        val registers = IntArray(6)

        // Execute instructions until we go out of bounds
        while (registers[ipRegister] in instructions.indices) {
            val instruction = instructions[registers[ipRegister]]

            // Look for the 'eqrr' instruction that compares with register 0
            // This is the halt condition - the program stops when this comparison is true
            if (instruction.opcode == "eqrr") {
                // Determine which register is being compared with register 0
                // If instruction.a is 0, we want the value in instruction.b, otherwise instruction.a
                val registerA = if (instruction.a == 0) instruction.b else instruction.a

                // Return the current value in that register
                // This is the answer: if we set register 0 to this value from the start,
                // the comparison will be true immediately and the program halts with minimum instructions
                return registers[registerA]
            }

            // Execute the current instruction
            executeInstruction(registers, instruction)
            // Increment the instruction pointer
            registers[ipRegister]++
        }
        return 0
    }

    /**
     * Part Two: Find the value for register 0 that halts the program with the most instructions.
     *
     * The comparison register cycles through values. We track all unique values seen.
     * When we encounter a repeated value, we know the cycle is complete.
     * The last unique value before the cycle repeats is the answer - it causes maximum execution time.
     */
    fun partTwo(data: String): Int {
        val (ipRegister, instructions) = parseInput(data)
        // Initialize 6 registers, all starting at 0
        val registers = IntArray(6)
        // Track all unique values we've seen at the comparison point
        val seen = mutableSetOf<Int>()
        // Keep track of the last value we saw
        var last = 0

        // Execute instructions until we go out of bounds
        while (registers[ipRegister] in instructions.indices) {
            val instruction = instructions[registers[ipRegister]]

            // Look for the 'eqrr' instruction that compares with register 0
            if (instruction.opcode == "eqrr") {
                // Determine which register is being compared with register 0
                val registerA = if (instruction.a == 0) instruction.b else instruction.a
                val value = registers[registerA]

                // Check if we've seen this value before
                // If yes, the program has started cycling and will repeat forever
                if (value in seen) {
                    // Return the last unique value before the cycle
                    // Setting register 0 to this value will cause the program to execute
                    // through all unique comparison values before finally halting
                    // This maximizes the number of instructions executed
                    return last
                }

                // This is a new value, add it to our seen set
                seen.add(value)
                // Update the last value we've seen
                last = value
            }

            // Execute the current instruction
            executeInstruction(registers, instruction)
            // Increment the instruction pointer
            registers[ipRegister]++
        }
        // Return the last value seen (edge case if program exits without cycling)
        return last
    }


}


