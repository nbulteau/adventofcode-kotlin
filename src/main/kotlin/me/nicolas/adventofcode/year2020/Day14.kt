package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 14: Docking Data ---
// https://adventofcode.com/2020/day/14
fun main() {
    val data = readFileDirectlyAsText("/year2020/day14/data.txt")
    val day = Day14(2020, 14, "Docking Data")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day14(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private val matchResult = Regex("""mem\[(\d+)\] = (\d+)""")

    fun partOne(data: String): Long {
        val program = data.split("\n").filter { it.isNotEmpty() }
        val memory = process(program, ::computeBlockPartOne)
        return memory.values.sumOf { it.toDecimal() }
    }

    fun partTwo(data: String): Long {
        val program = data.split("\n").filter { it.isNotEmpty() }
        val memory = process(program, ::computeBlockPartTwo)
        return memory.values.sumOf { it.toDecimal() }
    }

    private fun process(
        program: List<String>,
        function: (String, List<String>, MutableMap<Long, String>) -> Unit,
    ): MutableMap<Long, String> {
        val memory = mutableMapOf<Long, String>()
        var index = 0
        do {
            val mask = program[index].substring("mask = ".length)
            var endBlock = index + 1
            while (endBlock < program.size && program[endBlock].startsWith("mem")) {
                endBlock++
            }
            val instructions = program.subList(index + 1, endBlock)
            function(mask, instructions, memory)
            index = endBlock
        } while (index < program.size)
        return memory
    }

    private fun computeBlockPartOne(mask: String, instructions: List<String>, memory: MutableMap<Long, String>) {
        instructions.forEach {
            val (address, value) = it.getInstruction()
            memory[address] = value.applyMaskPartOne(mask)
        }
    }

    private fun computeBlockPartTwo(mask: String, instructions: List<String>, memory: MutableMap<Long, String>) {
        instructions.forEach {
            val (address, value) = it.getInstruction()
            val maskedAddress = address.applyMaskPartTwo(mask)
            val addressList = maskedAddress.expandResult()
            addressList.forEach { addressToSet -> memory[addressToSet.toDecimal()] = value }
        }
    }

    private fun String.expandResult(): List<String> {
        val addressList = mutableListOf<String>()
        recursiveExpand(this, "", addressList)
        return addressList
    }

    private fun recursiveExpand(stringToExpand: String, current: String, list: MutableList<String>) {
        if (stringToExpand.isEmpty()) {
            list.add(current)
        } else {
            val char = stringToExpand[0]
            if (char == 'X') {
                recursiveExpand(stringToExpand.substring(1), current + '0', list)
                recursiveExpand(stringToExpand.substring(1), current + '1', list)
            } else {
                recursiveExpand(stringToExpand.substring(1), current + char, list)
            }
        }
    }

    /**
     *  function to convert given decimal number into Binary
     */
    private fun Long.toBinary(): String {
        var decimalNumber = this
        val binaryStr = StringBuilder()
        while (decimalNumber > 0) {
            val r = decimalNumber % 2
            decimalNumber /= 2
            binaryStr.append(r)
        }

        return binaryStr.reverse().toString().padStart(36, '0')
    }

    /**
     * to get Decimal number from input binary number
     */
    private fun String.toDecimal(): Long {
        // Parse binary string to Long. Keep it robust for strings that are all zeros.
        val s = this.trimStart('0')
        return if (s.isEmpty()) 0L else s.toLong(2)
    }

    private fun String.getInstruction(): Pair<Long, String> {
        val matchedResults = matchResult.findAll(this)
        val matchedText = matchedResults.first()
        return Pair(
            matchedText.groups[1]?.value?.toLong()!!,
            matchedText.groups[2]?.value?.toLong()?.toBinary()!!
        )
    }

    private fun String.applyMaskPartOne(mask: String): String {
        return mask.mapIndexed { index, char ->
            when (char) {
                '0', '1' -> char
                else -> this[index]
            }
        }.joinToString("")
    }

    private fun Long.applyMaskPartTwo(mask: String): String {
        val binaryAddress = this.toBinary()
        return mask.mapIndexed { index, char ->
            when (char) {
                '0' -> binaryAddress[index]
                '1' -> "1"
                else -> 'X'
            }
        }.joinToString("")
    }
}