package me.nicolas.adventofcode.year2019

/**
 * IntCode Computer - A virtual machine that executes IntCode programs.
 *
 * This is a complete implementation of the IntCode computer from Advent of Code 2019.
 * It supports all opcodes (1-9 and 99) and three parameter modes (position, immediate, relative).
 *
 * Features:
 * - Sparse memory using a map (supports arbitrarily large addresses with default 0 values)
 * - Relative base for relative addressing mode
 * - Two execution modes: execute() runs until halt, executeUntilOutput() pauses at each output
 *
 * Opcodes:
 * - 1: Addition (3 parameters)
 * - 2: Multiplication (3 parameters)
 * - 3: Input (1 parameter)
 * - 4: Output (1 parameter)
 * - 5: Jump-if-true (2 parameters)
 * - 6: Jump-if-false (2 parameters)
 * - 7: Less than (3 parameters)
 * - 8: Equals (3 parameters)
 * - 9: Adjust relative base (1 parameter)
 * - 99: Halt
 *
 * Parameter Modes:
 * - 0: Position mode - parameter is an address
 * - 1: Immediate mode - parameter is a value
 * - 2: Relative mode - parameter is an offset from relative base
 *
 * @param program The initial program to load into memory (list of Long integers)
 */
class IntCodeProgram(program: List<Long>) {

    // Use a map for sparse memory to support large memory with default 0 values
    private val memory = mutableMapOf<Long, Long>()

    // Relative base register for relative addressing mode
    private var relativeBase = 0L

    // Instruction pointer (current position in program)
    private var instructionPointer = 0L

    // Flag indicating if the program has halted (opcode 99)
    private var halted = false

    init {
        // Load program into memory starting at address 0
        program.forEachIndexed { index, value ->
            memory[index.toLong()] = value
        }
    }

    /**
     * Execute the program until it halts (opcode 99).
     *
     * This method runs the entire program from the current instruction pointer
     * until a halt instruction is encountered. All inputs must be provided upfront.
     *
     * @param inputs Mutable list of input values (consumed by opcode 3)
     * @return List of all output values produced by the program (from opcode 4)
     */
    fun execute(inputs: MutableList<Long>): List<Long> {
        val output = mutableListOf<Long>()

        var instruction = readMemory(instructionPointer)
        do {
            // Extract opcode from last 2 digits of instruction
            val operation = (instruction % 100).toInt()

            // Execute operation and update instruction pointer
            instructionPointer = when (operation) {
                1 -> opAdd(instructionPointer, instruction)           // Add
                2 -> opMultiply(instructionPointer, instruction)      // Multiply
                3 -> opInput(instructionPointer, instruction, inputs) // Input
                4 -> opOutput(instructionPointer, instruction, output)// Output
                5 -> jumpIfTrue(instructionPointer, instruction)    // Jump-if-true
                6 -> jumpIfFalse(instructionPointer, instruction)   // Jump-if-false
                7 -> opLessThan(instructionPointer, instruction)      // Less than
                8 -> opEquals(instructionPointer, instruction)        // Equals
                9 -> adjustRelativeBase(instructionPointer, instruction) // Adjust relative base
                99 -> {
                    halted = true
                    break
                }
                else -> throw IllegalArgumentException("Unknown operation: $operation")
            }
            instruction = readMemory(instructionPointer)
        } while (true)

        return output
    }

    /**
     * Execute the program until it produces an output or halts.
     *
     * This method is useful for interactive programs that need to process output
     * incrementally. The program pauses after each output instruction, allowing
     * the caller to examine the output and provide new inputs before continuing.
     *
     * The instruction pointer is preserved between calls, so calling this method
     * repeatedly will continue execution from where it left off.
     *
     * @param inputs Mutable list of input values (consumed as needed by opcode 3)
     * @return The output value if opcode 4 was executed, or null if program halted
     */
    fun executeUntilOutput(inputs: MutableList<Long>): Long? {
        var instruction = readMemory(instructionPointer)
        while (true) {
            // Extract opcode from last 2 digits of instruction
            val operation = (instruction % 100).toInt()
            when (operation) {
                1 -> instructionPointer = opAdd(instructionPointer, instruction)
                2 -> instructionPointer = opMultiply(instructionPointer, instruction)
                3 -> instructionPointer = opInput(instructionPointer, instruction, inputs)
                4 -> {
                    // Output operation: extract parameter mode and get output value
                    val parameterMode = (instruction / 100 % 10).toInt()
                    val out = getValue(parameterMode, instructionPointer + 1)
                    instructionPointer += 2 // Move past output instruction
                    return out // Pause execution and return output
                }
                5 -> instructionPointer = jumpIfTrue(instructionPointer, instruction)
                6 -> instructionPointer = jumpIfFalse(instructionPointer, instruction)
                7 -> instructionPointer = opLessThan(instructionPointer, instruction)
                8 -> instructionPointer = opEquals(instructionPointer, instruction)
                9 -> instructionPointer = adjustRelativeBase(instructionPointer, instruction)
                99 -> {
                    halted = true
                    return null // Program has halted
                }
                else -> throw IllegalArgumentException("Unknown operation: $operation")
            }
            instruction = readMemory(instructionPointer)
        }
    }

    /**
     * Check if the program has halted.
     *
     * @return true if the program has executed opcode 99, false otherwise
     */
    fun isHalted(): Boolean = halted

    /**
     * Opcode 9: Adjust the relative base by the parameter value.
     *
     * The relative base is used by parameter mode 2 (relative mode) to calculate
     * memory addresses relative to the current base.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @return New instruction pointer (current + 2)
     */
    private fun adjustRelativeBase(index: Long, instruction: Long): Long {
        val parameterMode = (instruction / 100 % 10).toInt()
        val parameter = getValue(parameterMode, index + 1)
        relativeBase += parameter

        return index + 2
    }

    /**
     * Opcode 8: Store 1 if parameter1 equals parameter2, otherwise store 0.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @return New instruction pointer (current + 4)
     */
    private fun opEquals(index: Long, instruction: Long): Long {
        val m1 = (instruction / 100 % 10).toInt()
        val m2 = (instruction / 1000 % 10).toInt()
        val m3 = (instruction / 10000 % 10).toInt()
        val p1 = getValue(m1, index + 1)
        val p2 = getValue(m2, index + 2)
        val addr = getAddress(m3, index + 3)
        writeMemory(addr, if (p1 == p2) 1 else 0)

        return index + 4
    }

    /**
     * Opcode 7: Store 1 if parameter1 < parameter2, otherwise store 0.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @return New instruction pointer (current + 4)
     */
    private fun opLessThan(index: Long, instruction: Long): Long {
        val m1 = (instruction / 100 % 10).toInt()
        val m2 = (instruction / 1000 % 10).toInt()
        val m3 = (instruction / 10000 % 10).toInt()
        val p1 = getValue(m1, index + 1)
        val p2 = getValue(m2, index + 2)
        val addr = getAddress(m3, index + 3)
        writeMemory(addr, if (p1 < p2) 1 else 0)

        return index + 4
    }

    /**
     * Opcode 6: Jump-if-false - If parameter is 0, set instruction pointer to address.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @return New instruction pointer (address if parameter is 0, else current + 3)
     */
    private fun jumpIfFalse(index: Long, instruction: Long): Long {
        val m1 = (instruction / 100 % 10).toInt()
        val m2 = (instruction / 1000 % 10).toInt()
        val param = getValue(m1, index + 1)
        val addr = getValue(m2, index + 2)

        return if (param == 0L) addr else index + 3
    }

    /**
     * Opcode 5: Jump-if-true - If parameter is non-zero, set instruction pointer to address.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @return New instruction pointer (address if parameter != 0, else current + 3)
     */
    private fun jumpIfTrue(index: Long, instruction: Long): Long {
        val m1 = (instruction / 100 % 10).toInt()
        val m2 = (instruction / 1000 % 10).toInt()
        val param = getValue(m1, index + 1)
        val addr = getValue(m2, index + 2)

        return if (param != 0L) addr else index + 3
    }

    /**
     * Opcode 4: Output - Add a value to the output list.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @param output Mutable list to append the output value
     * @return New instruction pointer (current + 2)
     */
    private fun opOutput(index: Long, instruction: Long, output: MutableList<Long>): Long {
        val parameterMode = (instruction / 100 % 10).toInt()
        output.add(getValue(parameterMode, index + 1))

        return index + 2
    }

    /**
     * Opcode 3: Input - Read a value from input and store at address.
     *
     * Removes and consumes the first value from the inputs list.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @param inputs Mutable list of input values
     * @return New instruction pointer (current + 2)
     */
    private fun opInput(index: Long, instruction: Long, inputs: MutableList<Long>): Long {
        val parameterMode = (instruction / 100 % 10).toInt()
        val addr = getAddress(parameterMode, index + 1)
        writeMemory(addr, inputs.removeAt(0))

        return index + 2
    }

    /**
     * Opcode 2: Multiply operand1 and operand2, store result at address.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @return New instruction pointer (current + 4)
     */
    private fun opMultiply(index: Long, instruction: Long): Long {
        val m1 = (instruction / 100 % 10).toInt()
        val m2 = (instruction / 1000 % 10).toInt()
        val m3 = (instruction / 10000 % 10).toInt()
        val o1 = getValue(m1, index + 1)
        val o2 = getValue(m2, index + 2)
        val addr = getAddress(m3, index + 3)
        writeMemory(addr, o1 * o2)

        return index + 4
    }

    /**
     * Opcode 1: Add operand1 and operand2, store result at address.
     *
     * @param index Current instruction pointer
     * @param instruction Full instruction including opcode and parameter modes
     * @return New instruction pointer (current + 4)
     */
    private fun opAdd(index: Long, instruction: Long): Long {
        val m1 = (instruction / 100 % 10).toInt()
        val m2 = (instruction / 1000 % 10).toInt()
        val m3 = (instruction / 10000 % 10).toInt()
        val o1 = getValue(m1, index + 1)
        val o2 = getValue(m2, index + 2)
        val addr = getAddress(m3, index + 3)
        writeMemory(addr, o1 + o2)

        return index + 4
    }

    /**
     * Get a value from memory using the specified parameter mode.
     *
     * Parameter modes determine how to interpret memory values:
     * - Mode 0 (position): Treat the value at index as an address, return value at that address
     * - Mode 1 (immediate): Return the value at index directly
     * - Mode 2 (relative): Treat value at index as offset from relativeBase, return value at that address
     *
     * @param mode Parameter mode (0=position, 1=immediate, 2=relative)
     * @param index Memory address to read the parameter from
     * @return The value retrieved according to the parameter mode
     */
    private fun getValue(mode: Int, index: Long): Long =
        when (mode) {
            0 -> readMemory(readMemory(index)) // position mode
            1 -> readMemory(index) // immediate mode
            2 -> readMemory(relativeBase + readMemory(index)) // relative mode
            else -> throw IllegalArgumentException("Unknown mode: $mode")
        }

    /**
     * Get a memory address using the specified parameter mode.
     *
     * Used for write operations to calculate where to store a value.
     * Note: Immediate mode (1) is not valid for addresses.
     *
     * @param mode Parameter mode (0=position, 2=relative)
     * @param index Memory address to read the parameter from
     * @return The calculated address for writing
     */
    private fun getAddress(mode: Int, index: Long): Long =
        when (mode) {
            0 -> readMemory(index) // position mode
            2 -> relativeBase + readMemory(index) // relative mode
            else -> throw IllegalArgumentException("Invalid address mode: $mode")
        }

    /**
     * Read a value from memory at the specified address.
     *
     * Uses sparse memory representation - uninitialized addresses default to 0.
     *
     * @param address Memory address to read from (must be non-negative)
     * @return Value at the address, or 0 if not initialized
     * @throws IllegalArgumentException if address is negative
     */
    private fun readMemory(address: Long): Long {
        if (address < 0) throw IllegalArgumentException("Invalid memory address: $address")
        return memory.getOrDefault(address, 0L)
    }

    /**
     * Write a value to memory at the specified address.
     *
     * @param address Memory address to write to (must be non-negative)
     * @param value Value to write
     * @throws IllegalArgumentException if address is negative
     */
    private fun writeMemory(address: Long, value: Long) {
        if (address < 0) throw IllegalArgumentException("Invalid memory address: $address")
        memory[address] = value
    }
}
