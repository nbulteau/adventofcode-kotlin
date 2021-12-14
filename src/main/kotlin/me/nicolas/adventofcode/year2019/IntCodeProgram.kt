package me.nicolas.adventofcode.year2019

class IntCodeProgram(private val intCodeProgram: IntArray) {

    fun execute(inputs: MutableList<Int>): List<Int> {
        var index = 0
        val output = mutableListOf<Int>()

        var instruction = intCodeProgram[index]
        do {
            index = when (val operation = instruction % 100) {
                1 -> addOperation(index, instruction)
                2 -> multiplyOperation(index, instruction)
                3 -> inputOperation(index, inputs)
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

    private fun inputOperation(index: Int, inputs: MutableList<Int>): Int {
        val address = intCodeProgram[index + 1]
        intCodeProgram[address] = inputs.removeAt(0)

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
