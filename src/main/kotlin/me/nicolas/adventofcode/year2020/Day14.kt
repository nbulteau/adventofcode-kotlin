package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText
import java.math.BigDecimal
import kotlin.math.pow


// --- Day 14: Docking Data ---
// https://adventofcode.com/2020/day/14
fun main() {

    println("--- Day 14: Docking Data ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day14/training.txt")
    val data = readFileDirectlyAsText("/year2020/day14/data.txt")

    val program = data.split("\n")

    // Part One
    Day14().partOne(program, Day14().computeBlockPartOne)

    // Part Two
    Day14().partTwo(program, Day14().computeBlockPartTwo)

}

class Day14 {

    private val matchResult = Regex("""mem\[(\d+)\] = (\d+)""")

    fun partOne(program: List<String>, function: (String, List<String>, MutableMap<Long, String>) -> Unit) {

        val memory = process(program, function)

        println("Part one = ${memory.values.sumOf { value -> value.toDecimal() }}")
    }

    fun partTwo(program: List<String>, function: (String, List<String>, MutableMap<Long, String>) -> Unit) {

        val memory = process(program, function)

        println("Part two = ${memory.values.sumOf { value -> value.toDecimal() }}")
    }

    private fun process(
        program: List<String>,
        function: (String, List<String>, MutableMap<Long, String>) -> Unit
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

    val computeBlockPartOne = { mask: String, instructions: List<String>, memory: MutableMap<Long, String> ->

        val index = 0
        instructions.forEach {
            val (address, value) = it.getInstruction()
            memory[address] = value.applyMaskPartOne(mask)
        }
    }

    val computeBlockPartTwo = { mask: String, instructions: List<String>, memory: MutableMap<Long, String> ->

        instructions.forEach {
            val (address, value) = it.getInstruction()
            val maskedAddress = address.applyMaskPartTwo(mask)
            val addressList = maskedAddress.expandResult()
            addressList.forEach { address -> memory[address.toDecimal().toLong()] = value }
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
    private fun String.toDecimal(): BigDecimal {
        var binaryNumber = this.trimStart('0').toBigDecimal()
        var decimalNo = BigDecimal.ZERO
        var power = 0

        while (binaryNumber > BigDecimal.ZERO) {
            val r = binaryNumber % BigDecimal.valueOf(10)
            decimalNo = (decimalNo + r * BigDecimal.valueOf(2.0.pow(power)))
            binaryNumber /= BigDecimal.valueOf(10)
            power++
        }
        return decimalNo
    }

    private fun String.getInstruction(): Pair<Long, String> {
        val matchedResults = matchResult.findAll(this)
        val matchedText = matchedResults.first()

        return Pair(matchedText.groups[1]?.value?.toLong()!!, matchedText.groups[2]?.value?.toLong()?.toBinary()!!)
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

        val binaryaddress = this.toBinary()

        return mask.mapIndexed { index, char ->
            when (char) {
                '0' -> binaryaddress[index]
                '1' -> "1"
                else -> mask[index]
            }
        }.joinToString("")
    }
}