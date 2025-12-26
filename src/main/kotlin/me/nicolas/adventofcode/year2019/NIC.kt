package me.nicolas.adventofcode.year2019

import kotlinx.coroutines.channels.Channel
import kotlin.math.pow

/**
 * NIC - Network Interface Controller / IntCode VM instance used by Day 23.
 *
 * This class implements an IntCode computer with:
 * - sparse memory (MutableMap<Long, Long>)
 * - an input Channel (input) and an output Channel (output)
 * - support for position (0), immediate (1) and relative (2) parameter modes
 *
 * The NIC executes instructions in a loop (start -> step) until it encounters opcode 99.
 * Input and output use Kotlin channels: the NIC reads from `input` on opcode 3
 * and sends values to `output` on opcode 4.
 *
 * Note: networking behavior (providing -1 when no packet is available, etc.) is
 * handled externally by the caller via the input channel usage pattern.
 */
class NIC(
    private val memory: MutableMap<Long, Long>,
    val input: Channel<Long> = Channel(Channel.UNLIMITED),
    val output: Channel<Long> = Channel(Channel.CONFLATED)
) {

    private var halted = false
    private var instructionPointer = 0L
    private var relativeBase = 0L

    /**
     * Start execution loop.
     *
     * Runs `step()` repeatedly until a halt opcode is executed.
     * When halted, the output channel is closed to signal completion.
     */
    suspend fun start() {
        while (!halted) {
            step()
        }
        output.close()
    }

    /**
     * Execute a single instruction (one step of the VM).
     *
     * This method decodes the opcode at the current instruction pointer and
     * performs the corresponding operation (add, mul, input, output, jumps,
     * comparisons, relative base adjust, halt). Parameter modes are resolved
     * using readParam/writeParam helpers.
     */
    private suspend fun step() =
        when (val opId = (memory.getValue(instructionPointer) % 100L).toInt()) {
            1 -> { // Add
                memory[writeParam(3)] = readParam(1) + readParam(2)
                instructionPointer += 4
            }

            2 -> { // Multiply
                memory[writeParam(3)] = readParam(1) * readParam(2)
                instructionPointer += 4
            }

            3 -> { // Input
                // Suspend until an input value is available on the channel.
                memory[writeParam(1)] = input.receive()
                instructionPointer += 2
            }

            4 -> { // Output
                // Send the resolved parameter value to the output channel.
                output.send(readParam(1))
                instructionPointer += 2
            }

            5 -> { // JumpIfTrue
                if (readParam(1) != 0L) {
                    instructionPointer = readParam(2)
                } else {
                    instructionPointer += 3
                }
            }

            6 -> { // JumpIfFalse
                if (readParam(1) == 0L) {
                    instructionPointer = readParam(2)
                } else {
                    instructionPointer += 3
                }
            }

            7 -> { // LessThan
                memory[writeParam(3)] = if (readParam(1) < readParam(2)) 1L else 0L
                instructionPointer += 4
            }

            8 -> { // Equals
                memory[writeParam(3)] = if (readParam(1) == readParam(2)) 1L else 0L
                instructionPointer += 4
            }

            9 -> { // AdjustRelativeBase
                // Modify relative base by the value of the parameter.
                relativeBase += readParam(1)
                instructionPointer += 2
            }

            99 -> { // Halt
                halted = true
            }

            else -> throw IllegalArgumentException("Unknown operation: $opId")
        }

    /**
     * Read the value of a parameter at position `paramNo` according to the
     * parameter mode embedded in the current instruction.
     *
     * Modes:
     * - 0: position mode -> treat parameter as address and read memory[address]
     * - 1: immediate mode -> treat parameter as literal value
     * - 2: relative mode -> treat parameter as address offset from relativeBase
     */
    private fun readParam(paramNo: Int): Long =
        memory.readRef(memory.getOrDefault(instructionPointer, 0L).toModeParam(paramNo), instructionPointer + paramNo)

    /**
     * Compute the target address for a write parameter at position `paramNo`.
     *
     * For write parameters, immediate mode (1) is invalid. Position (0) and
     * relative (2) modes produce an address where the value will be stored.
     */
    private fun writeParam(paramNo: Int): Long =
        memory.writeRef(memory.getOrDefault(instructionPointer, 0L).toModeParam(paramNo), instructionPointer + paramNo)

    /**
     * Extract the parameter mode digit for a given parameter position.
     *
     * The instruction's digits beyond the two least-significant digits encode
     * modes for parameters 1..n. This helper isolates the correct digit.
     */
    private fun Long.toModeParam(pos: Int): Int =
        (this / (10.0.pow(pos + 1).toInt()) % 10).toInt()

    /**
     * Resolve a read reference according to the specified mode and parameter location.
     *
     * - mode 0 (position): read the value at address readMemory(at)
     * - mode 1 (immediate): read the value at address `at` directly
     * - mode 2 (relative): read the value at address (readMemory(at) + relativeBase)
     */
    private fun MutableMap<Long, Long>.readRef(mode: Int, at: Long): Long =
        when (mode) {
            0 -> getOrDefault(getOrDefault(at, 0L), 0L)
            1 -> getOrDefault(at, 0L)
            2 -> getOrDefault(getOrDefault(at, 0L) + relativeBase, 0L)
            else -> throw IllegalArgumentException("Unknown read mode: $mode")
        }

    /**
     * Compute the address to use for a write operation based on mode and parameter location.
     *
     * - mode 0 (position): return the value stored at `at` (the target address)
     * - mode 2 (relative): return (value at `at`) + relativeBase
     *
     * Note: this returns the computed address (not the value stored there).
     */
    private fun MutableMap<Long, Long>.writeRef(mode: Int, at: Long): Long =
        when (mode) {
            0 -> getOrDefault(at, 0L)
            2 -> getOrDefault(at, 0L) + relativeBase
            else -> throw IllegalArgumentException("Unknown write mode: $mode")
        }
}

fun <T, R> Map<T, R>.copyOf(): Map<T, R> =
    mutableMapOf<T, R>().also { it.putAll(this) }