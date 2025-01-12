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

    fun partOne(data: String): Int {
        val (ipRegister, instructions) = parseInput(data)
        val registers = IntArray(6)
        val seen = mutableSetOf<Int>()
        var last = 0

        while (registers[ipRegister] in instructions.indices) {
            val instruction = instructions[registers[ipRegister]]
            if (instruction.opcode == "eqrr" && instruction.a == 0) {
                if (registers[instruction.b] in seen) {
                    return last
                }
                seen.add(registers[instruction.b])
                last = registers[instruction.b]
            }
            executeInstruction(registers, instruction)
            registers[ipRegister]++
        }
        return last
    }

    fun partTwo(data: String): Int {
        return 42
    }

}


