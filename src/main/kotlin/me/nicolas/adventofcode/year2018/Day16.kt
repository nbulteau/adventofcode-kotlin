package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// https://adventofcode.com/2018/day/16
fun main() {
    val data = readFileDirectlyAsText("/year2018/day16/data.txt")
    val day = Day16(2018, 16, "Chronal Classification")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day16(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val parts = data.split("\n\n\n\n")
        val part1Input: List<Input> = parsePart1Input(parts.first().lines())

        return part1Input.count { input -> countMatchingOperations(input) >= 3 }
    }

    fun partTwo(data: String): Int {
        val parts = data.split("\n\n\n\n")
        val part1Input: List<Input> = parsePart1Input(parts.first().lines())
        val part2Input: List<IntArray> = parsePart2Input(parts.last().lines())

        // Map each function to a set of possible opcodes that could be it based on the input
        val functionToOpCodes: MutableMap<Operation, MutableSet<Int>> = part1Input.flatMap { input ->
            getOperations().mapNotNull { operation ->
                if (operation.execute(input.registersBefore, input.instruction)
                        .contentEquals(input.expectedRegisters)
                ) {
                    input.id to operation
                } else {
                    null
                }
            }
        }
            // Group by function and map to a set of opcodes
            .groupBy({ it.second }, { it.first })
            .mapValues { (_, list) -> list.toMutableSet() }
            .toMutableMap()

        // Debug
        //functionToOpCodes.forEach { (op, codes) ->
        //    println("$op -> $codes")
        //}

        val operations = mutableMapOf<Int, Operation>()
        // While there are still functions that could be mapped to multiple opcodes keep looping through
        while (functionToOpCodes.isNotEmpty()) {
            // Find all functions that can only be mapped to one opcode
            functionToOpCodes
                .filter { (_, codes) -> codes.size == 1 }
                .map { Pair(it.key, it.value.first()) }
                // Map them to the opcode and remove them from the other functions
                .forEach { (op, code) ->
                    operations[code] = op
                    functionToOpCodes.remove(op)
                    functionToOpCodes.forEach { (_, thoseFuncs) -> thoseFuncs.remove(code) }
                }
            // Remove all functions that are already mapped to an opcode
            functionToOpCodes.entries.removeIf { (_, value) -> value.isEmpty() }
        }
        // Debug
        //operations.forEach { (code, operation) ->
        //    println("$code -> ${operation.name}")
        //}

        var registers = intArrayOf(0, 0, 0, 0)
        part2Input.forEach { instruction ->
            val operation = operations[instruction[0]]!!
            registers = operation.execute(registers, instruction)
        }

        return registers.first()
    }

    private fun parsePart1Input(data: List<String>): List<Input> =
        data.chunked(4) { chunk ->
            val registersBefore =
                chunk[0].substringAfter("Before: [").substringBefore("]").split(", ").map { it.toInt() }
            val instruction = chunk[1].split(" ").map { it.toInt() }
            val registersAfter =
                chunk[2].substringAfter("After:  [").substringBefore("]").split(", ").map { it.toInt() }

            Input(registersBefore.toIntArray(), instruction.toIntArray(), registersAfter.toIntArray())
        }

    private fun parsePart2Input(data: List<String>): List<IntArray> =
        data.map { line -> line.split(" ").map { it.toInt() }.toIntArray() }

    private fun countMatchingOperations(input: Input): Int =
        getOperations().count { operation ->
            val result = operation.execute(input.registersBefore, input.instruction)
            result.contentEquals(input.expectedRegisters)
        }

    private fun getOperations(): List<Operation> = listOf(
        Addr(0, "addr"),
        Addi(1, "addi"),
        Mulr(2, "mulr"),
        Muli(3, "muli"),
        Banr(4, "banr"),
        Bani(5, "bani"),
        Borr(6, "borr"),
        Bori(7, "bori"),
        Setr(8, "setr"),
        Seti(9, "seti"),
        Gtir(10, "gtir"),
        Gtri(11, "gtri"),
        Gtrr(12, "gtrr"),
        Eqir(13, "eqir"),
        Eqri(14, "eqri"),
        Eqrr(15, "eqrr")
    )

    // Not a data class because we don't want to override equals and hashcode for this class
    class Input(val registersBefore: IntArray, val instruction: IntArray, val expectedRegisters: IntArray) {
        val id: Int
            get() = instruction[0]
    }

    sealed class Operation(open val code: Int, open val name: String) {
        abstract fun execute(registers: IntArray, instruction: IntArray): IntArray
    }

    data class Addr(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] + registers[instruction[2]] }
    }

    data class Addi(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] + instruction[2] }
    }

    data class Mulr(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] * registers[instruction[2]] }

    }

    data class Muli(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] * instruction[2] }
    }

    data class Banr(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] and registers[instruction[2]] }
    }

    data class Bani(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] and instruction[2] }
    }

    data class Borr(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] or registers[instruction[2]] }
    }

    data class Bori(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] or instruction[2] }
    }

    data class Setr(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = registers[instruction[1]] }
    }

    data class Seti(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = instruction[1] }
    }

    data class Gtir(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = if (instruction[1] > registers[instruction[2]]) 1 else 0 }
    }

    data class Gtri(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf().apply { this[instruction[3]] = if (registers[instruction[1]] > instruction[2]) 1 else 0 }
    }

    data class Gtrr(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf()
                .apply { this[instruction[3]] = if (registers[instruction[1]] > registers[instruction[2]]) 1 else 0 }
    }

    data class Eqir(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf()
                .apply { this[instruction[3]] = if (instruction[1] == registers[instruction[2]]) 1 else 0 }
    }

    data class Eqri(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf()
                .apply { this[instruction[3]] = if (registers[instruction[1]] == instruction[2]) 1 else 0 }
    }

    data class Eqrr(override val code: Int, override val name: String) : Operation(code, name) {
        override fun execute(registers: IntArray, instruction: IntArray) =
            registers.copyOf()
                .apply { this[instruction[3]] = if (registers[instruction[1]] == registers[instruction[2]]) 1 else 0 }
    }
}


