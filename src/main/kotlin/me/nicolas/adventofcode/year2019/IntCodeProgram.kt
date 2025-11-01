package me.nicolas.adventofcode.year2019

class IntCodeProgram(program: List<Long>) {

    // Use a map for sparse memory to support large memory with default 0 values
    private val memory = mutableMapOf<Long, Long>()
    private var relativeBase = 0L

    init {
        program.forEachIndexed { index, value ->
            memory[index.toLong()] = value
        }
    }

    fun execute(inputs: MutableList<Long>): List<Long> {
        var index = 0L
        val output = mutableListOf<Long>()

        var instruction = getMemory(index)
        do {
            val operation = (instruction % 100).toInt()
            index = when (operation) {
                1 -> addOperation(index, instruction)
                2 -> multiplyOperation(index, instruction)
                3 -> inputOperation(index, instruction, inputs)
                4 -> outputOperation(index, instruction, output)
                5 -> jumpIfTrueOperation(index, instruction)
                6 -> jumpIfFalseOperation(index, instruction)
                7 -> lessThanOperation(index, instruction)
                8 -> equalsOperation(index, instruction)
                9 -> adjustRelativeBaseOperation(index, instruction)
                99 -> break
                else -> throw IllegalArgumentException("Unknown operation: $operation")
            }
            instruction = getMemory(index)
        } while (true)

        return output
    }

    private fun adjustRelativeBaseOperation(index: Long, instruction: Long): Long {
        val parameterMode = (instruction / 100 % 10).toInt()
        val parameter = getValue(parameterMode, index + 1)
        relativeBase += parameter

        return index + 2
    }

    private fun equalsOperation(index: Long, instruction: Long): Long {
        val firstParameterMode = (instruction / 100 % 10).toInt()
        val secondParameterMode = (instruction / 1000 % 10).toInt()
        val thirdParameterMode = (instruction / 10000 % 10).toInt()
        val parameter1 = getValue(firstParameterMode, index + 1)
        val parameter2 = getValue(secondParameterMode, index + 2)
        val address = getAddress(thirdParameterMode, index + 3)
        setMemory(address, if (parameter1 == parameter2) 1 else 0)

        return index + 4
    }

    private fun lessThanOperation(index: Long, instruction: Long): Long {
        val firstParameterMode = (instruction / 100 % 10).toInt()
        val secondParameterMode = (instruction / 1000 % 10).toInt()
        val thirdParameterMode = (instruction / 10000 % 10).toInt()
        val parameter1 = getValue(firstParameterMode, index + 1)
        val parameter2 = getValue(secondParameterMode, index + 2)
        val address = getAddress(thirdParameterMode, index + 3)
        setMemory(address, if (parameter1 < parameter2) 1 else 0)

        return index + 4
    }

    private fun jumpIfFalseOperation(index: Long, instruction: Long): Long {
        val firstParameterMode = (instruction / 100 % 10).toInt()
        val secondParameterMode = (instruction / 1000 % 10).toInt()
        val parameter = getValue(firstParameterMode, index + 1)
        val address = getValue(secondParameterMode, index + 2)

        return if (parameter == 0L) address else index + 3
    }

    private fun jumpIfTrueOperation(index: Long, instruction: Long): Long {
        val firstParameterMode = (instruction / 100 % 10).toInt()
        val secondParameterMode = (instruction / 1000 % 10).toInt()
        val parameter = getValue(firstParameterMode, index + 1)
        val address = getValue(secondParameterMode, index + 2)

        return if (parameter != 0L) address else index + 3
    }

    private fun outputOperation(index: Long, instruction: Long, output: MutableList<Long>): Long {
        val parameterMode = (instruction / 100 % 10).toInt()
        output.add(getValue(parameterMode, index + 1))

        return index + 2
    }

    private fun inputOperation(index: Long, instruction: Long, inputs: MutableList<Long>): Long {
        val parameterMode = (instruction / 100 % 10).toInt()
        val address = getAddress(parameterMode, index + 1)
        setMemory(address, inputs.removeAt(0))

        return index + 2
    }

    private fun multiplyOperation(index: Long, instruction: Long): Long {
        val firstParameterMode = (instruction / 100 % 10).toInt()
        val secondParameterMode = (instruction / 1000 % 10).toInt()
        val thirdParameterMode = (instruction / 10000 % 10).toInt()
        val operand1 = getValue(firstParameterMode, index + 1)
        val operand2 = getValue(secondParameterMode, index + 2)
        val address = getAddress(thirdParameterMode, index + 3)
        setMemory(address, operand1 * operand2)

        return index + 4
    }

    private fun addOperation(index: Long, instruction: Long): Long {
        val firstParameterMode = (instruction / 100 % 10).toInt()
        val secondParameterMode = (instruction / 1000 % 10).toInt()
        val thirdParameterMode = (instruction / 10000 % 10).toInt()
        val operand1 = getValue(firstParameterMode, index + 1)
        val operand2 = getValue(secondParameterMode, index + 2)
        val address = getAddress(thirdParameterMode, index + 3)
        setMemory(address, operand1 + operand2)

        return index + 4
    }

    private fun getValue(mode: Int, index: Long): Long =
        when (mode) {
            0 -> getMemory(getMemory(index)) // position mode
            1 -> getMemory(index) // immediate mode
            2 -> getMemory(relativeBase + getMemory(index)) // relative mode
            else -> throw IllegalArgumentException("Unknown mode: $mode")
        }

    private fun getAddress(mode: Int, index: Long): Long =
        when (mode) {
            0 -> getMemory(index) // position mode
            2 -> relativeBase + getMemory(index) // relative mode
            else -> throw IllegalArgumentException("Invalid address mode: $mode")
        }

    private fun getMemory(address: Long): Long {
        if (address < 0) throw IllegalArgumentException("Invalid memory address: $address")
        return memory.getOrDefault(address, 0L)
    }

    private fun setMemory(address: Long, value: Long) {
        if (address < 0) throw IllegalArgumentException("Invalid memory address: $address")
        memory[address] = value
    }
}
