package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 24: Crossed Wires ---
// https://adventofcode.com/2024/day/24
fun main() {
    val data = readFileDirectlyAsText("/year2024/day24/data.txt")
    val day = Day24(2024, 24)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day24(year: Int, day: Int, title: String = "Crossed Wires") : AdventOfCodeDay(year, day, title) {

    data class GateInstruction(val operation: String, val inputs: List<String>, val output: String)

    fun partOne(data: String): Long {
        val (wireValues, gateInstructions) = parseInput(data)

        // Simulate the gates
        val finalWireValues = simulateGates(wireValues, gateInstructions)

        // Combine the bits from all wires starting with z
        val binaryString = finalWireValues
            .filterKeys { wire -> wire.startsWith("z") }
            .toSortedMap().reversed()
            .values
            .joinToString("") { digit -> digit.toString() }

        return binaryString.toLong(2)
    }

    fun partTwo(data: String): String {
        val (_, gateInstructions) = parseInput(data)

        val swapped = swapWiresForFullAdder(gateInstructions)

        return swapped.sorted().joinToString(",")
    }

    /**
     * Swaps the wires to simulate a full adder.
     *     Full Adder Logic:
     *     A full adder adds three one-bit numbers (X1, Y1, and carry-in C0) and outputs a sum bit (Z1) and a carry-out bit (C1).
     *     The logic for a full adder is as follows:
     *     - X1 XOR Y1 -> M1 (intermediate sum)
     *     - X1 AND Y1 -> N1 (intermediate carry)
     *     - C0 AND M1 -> R1 (carry for intermediate sum)
     *     - C0 XOR M1 -> Z1 (final sum)
     *     - R1 OR N1 -> C1 (final carry)
     *
     *     References:
     *     - https://www.geeksforgeeks.org/full-adder/
     *     - https://www.geeksforgeeks.org/carry-look-ahead-adder/
     *     - https://en.wikipedia.org/wiki/Adder_(electronics)#Full_adder
     */
    private fun swapWiresForFullAdder(gateInstructions: List<GateInstruction>): List<String> {

        val wires = gateInstructions.flatMap { instruction ->
            listOf(instruction.inputs.first(), instruction.inputs.last(), instruction.output)
        }.toSet()
        println("${wires.size} distinct wires")
        val bitCount = (0..64).first { i -> ("x" + i.toString().padStart(2, '0')) !in wires }
        val lastZ = "z"+ bitCount.toString().padStart(2, '0')

        val swapped = mutableListOf<String>()
        var c0: String? = null
        repeat(bitCount) { i ->
            val bit = String.format("%02d", i)

            // Half adder logic
            var m1 = gateInstructions.find("x$bit", "y$bit", "XOR")
            var n1 = gateInstructions.find("x$bit", "y$bit", "AND")

            var r1: String?
            var z1: String? = null
            var c1: String? = null
            if (c0 != null) {
                r1 = gateInstructions.find(c0!!, m1!!, "AND")
                if (r1 == null) {
                    val temp = m1
                    m1 = n1
                    n1 = temp
                    swapped.addAll(listOf(m1!!, n1))
                    r1 = gateInstructions.find(c0!!, m1, "AND")
                }

                z1 = gateInstructions.find(c0!!, m1, "XOR")
                if (m1.startsWith("z")) {
                    val temp = m1
                    m1 = z1
                    z1 = temp
                    swapped.addAll(listOf(m1!!, z1))
                }

                if (n1?.startsWith("z") == true) {
                    val temp = n1
                    n1 = z1
                    z1 = temp
                    swapped.addAll(listOf(n1!!, z1))
                }

                if (r1?.startsWith("z") == true) {
                    val temp = r1
                    r1 = z1
                    z1 = temp
                    swapped.addAll(listOf(r1!!, z1))
                }

                c1 = gateInstructions.find(r1!!, n1!!, "OR")
            }

            if (c1?.startsWith("z") == true && c1 != lastZ) {
                val temp = c1
                c1 = z1
                z1 = temp
                swapped.addAll(listOf(c1!!, z1))
            }

            c0 = if (c0 == null) {
                n1
            } else {
                c1
            }
        }

        return swapped
    }

    private fun parseInput(data: String): Pair<Map<String, Int>, List<GateInstruction>> {
        val initialWires = mutableMapOf<String, Int>()
        val gateInstructions = mutableListOf<GateInstruction>()

        data.lines().filter { line -> line.isNotBlank() }.forEach { line ->
            if (line.contains(":")) {
                // Initial wire values
                val parts = line.split(": ")
                initialWires[parts[0]] = parts[1].trim().toInt()
            } else {
                // Gate instructions
                val parts = line.split(" -> ")
                val output = parts[1].trim()
                val operationParts = parts[0].trim().split(" ")

                val operation: String = operationParts[1].trim()
                val inputs: List<String> = listOf(operationParts[0].trim(), operationParts[2].trim())

                gateInstructions.add(GateInstruction(operation, inputs, output))
            }
        }

        return Pair(initialWires, gateInstructions)
    }

    private fun List<GateInstruction>.find(a: String, b: String, operator: String) =
        this.find { gateInstruction ->
            gateInstruction.operation == operator && (gateInstruction.inputs.first() == a && gateInstruction.inputs.last() == b || gateInstruction.inputs.first() == b && gateInstruction.inputs.last() == a)
        }?.output

    private fun simulateGates(
        initialWires: Map<String, Int>,
        gateInstructions: List<GateInstruction>,
    ): Map<String, Int> {
        val wireValues = initialWires.toMutableMap()

        while (true) {
            var newValuesComputed = false
            for (instruction in gateInstructions) {
                if (wireValues.containsKey(instruction.output)){
                    continue
                }

                val inputValues = instruction.inputs.map { wire -> wireValues[wire] }
                if (inputValues.any { it == null }) {
                    continue
                }

                val outputValue = when (instruction.operation) {
                    "AND" -> inputValues[0]!! and inputValues[1]!!
                    "OR" -> inputValues[0]!! or inputValues[1]!!
                    "XOR" -> inputValues[0]!! xor inputValues[1]!!
                    else -> throw UnsupportedOperationException("Unsupported operation: ${instruction.operation}")
                }

                wireValues[instruction.output] = outputValue
                newValuesComputed = true
            }

            if (!newValuesComputed) {
                break
            }
        }

        return wireValues
    }
}
