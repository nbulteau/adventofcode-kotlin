package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


// --- Day 5: Sunny with a Chance of Asteroids ---
// https://adventofcode.com/2019/day/5
@OptIn(ExperimentalTime::class)
fun main() {

    val training = readFileDirectlyAsText("/year2019/day05/training.txt")

    val data = readFileDirectlyAsText("/year2019/day05/data.txt")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day05().partOne(data) })
    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day05().partTwo(data) })
}

class Day05 {

    class IntCodeProgram(private val intCodeProgram: IntArray) {

        fun execute(input: Int): List<Int> {
            var index = 0
            val output = mutableListOf<Int>()

            var instruction = intCodeProgram[index]
            do {
                index = when (val operation = instruction % 100) {
                    1 -> addOperation(index, instruction)
                    2 -> multiplyOperation(index, instruction)
                    3 -> inputOperation(index, input)
                    4 -> outputOperation(index, instruction, output)
                    5 -> jumpIfTrueOperation(index, instruction)
                    6 -> jumpIfFalseOperation(index, instruction)
                    7 -> lessThanOperation(index, instruction)
                    8 -> equalsOperation(index, instruction)
                    99 -> break
                    else -> throw IllegalArgumentException("Unknown operation: $operation")
                }
                instruction = intCodeProgram[index]
            } while (true)

            return output
        }

        private fun equalsOperation(index: Int, instruction: Int): Int {
            val firstParameterMode = instruction / 100 % 10
            val secondParameterMode = instruction / 1000 % 10
            val parameter1 = getValue(firstParameterMode, index + 1)
            val parameter2 = getValue(secondParameterMode, index + 2)
            val address = intCodeProgram[index + 3]
            intCodeProgram[address] = if (parameter1 == parameter2) 1 else 0

            return index + 4
        }

        private fun lessThanOperation(index: Int, instruction: Int): Int {
            val firstParameterMode = instruction / 100 % 10
            val secondParameterMode = instruction / 1000 % 10
            val parameter1 = getValue(firstParameterMode, index + 1)
            val parameter2 = getValue(secondParameterMode, index + 2)
            val address = intCodeProgram[index + 3]
            intCodeProgram[address] = if (parameter1 < parameter2) 1 else 0

            return index + 4
        }

        private fun jumpIfFalseOperation(index: Int, instruction: Int): Int {
            val firstParameterMode = instruction / 100 % 10
            val secondParameterMode = instruction / 1000 % 10
            val parameter = getValue(firstParameterMode, index + 1)
            val address = getValue(secondParameterMode, index + 2)

            return if (parameter == 0) address else index + 3
        }

        private fun jumpIfTrueOperation(index: Int, instruction: Int): Int {
            val firstParameterMode = instruction / 100 % 10
            val secondParameterMode = instruction / 1000 % 10
            val parameter = getValue(firstParameterMode, index + 1)
            val address = getValue(secondParameterMode, index + 2)

            return if (parameter != 0) address else index + 3
        }

        private fun outputOperation(index: Int, instruction: Int, output: MutableList<Int>): Int {
            val parameterMode = instruction / 100 % 10
            output.add(getValue(parameterMode, index + 1))

            return index + 2
        }

        private fun inputOperation(index: Int, input: Int): Int {
            val address = intCodeProgram[index + 1]
            intCodeProgram[address] = input

            return index + 2
        }

        private fun multiplyOperation(index: Int, instruction: Int): Int {
            val firstParameterMode = instruction / 100 % 10
            val secondParameterMode = instruction / 1000 % 10
            val operand1 = getValue(firstParameterMode, index + 1)
            val operand2 = getValue(secondParameterMode, index + 2)
            val address = intCodeProgram[index + 3]
            intCodeProgram[address] = operand1 * operand2

            return index + 4
        }

        private fun addOperation(index: Int, instruction: Int): Int {
            val firstParameterMode = instruction / 100 % 10
            val secondParameterMode = instruction / 1000 % 10
            val operand1 = getValue(firstParameterMode, index + 1)
            val operand2 = getValue(secondParameterMode, index + 2)
            val address = intCodeProgram[index + 3]
            intCodeProgram[address] = operand1 + operand2

            return index + 4
        }

        private fun getValue(mode: Int, index: Int): Int =
            when (mode) {
                0 -> intCodeProgram[intCodeProgram[index]]
                1 -> intCodeProgram[index]
                else -> throw IllegalArgumentException("Unknown mode: $mode")
            }
    }

    fun partOne(inputs: String): Int {
        val intCodeProgram = IntCodeProgram(inputs.split(",").map { it.toInt() }.toIntArray())

        val output = intCodeProgram.execute(1)

        return output.last()
    }

    fun partTwo(inputs: String): Int {
        val intCodeProgram = IntCodeProgram(inputs.split(",").map { it.toInt() }.toIntArray())

        val output = intCodeProgram.execute(5)

        return output.last()
    }
}


