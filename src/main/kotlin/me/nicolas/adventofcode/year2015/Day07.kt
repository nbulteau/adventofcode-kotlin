package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 7: Some Assembly Required ---
// https://adventofcode.com/2015/day/7
fun main() {
    val data = readFileDirectlyAsText("/year2015/day07/data.txt")
    val day = Day07(2015, 7)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07(year: Int, day: Int, title: String = "Some Assembly Required") : AdventOfCodeDay(year, day, title) {


    fun evaluateCircuit(instructions: List<String>): Map<String, Int> {
        val wireValues = mutableMapOf<String, Int>()
        val operations = instructions.toMutableList()

        while (operations.isNotEmpty()) {
            val currentInstructions = operations.toMutableList()
            for (instruction in currentInstructions) {
                val parts = instruction.split(" ")

                when (parts.size) {
                    3 -> { // Direct assignment or value
                        if (parts[0].toIntOrNull() != null) {
                            wireValues[parts[2]] = parts[0].toInt()
                            operations.remove(instruction)
                        } else if (wireValues.containsKey(parts[0])) {
                            wireValues[parts[2]] = wireValues[parts[0]]!!
                            operations.remove(instruction)
                        }
                    }

                    4 -> { // NOT gate
                        val target = parts[3]
                        if (parts[1].toIntOrNull() != null) {
                            wireValues[target] = parts[1].toInt().inv() and 0xFFFF
                            operations.remove(instruction)
                        } else if (wireValues.containsKey(parts[1])) {
                            wireValues[target] = wireValues[parts[1]]!!.inv() and 0xFFFF
                            operations.remove(instruction)
                        }
                    }

                    5 -> { // AND, OR, LSHIFT, RSHIFT gates
                        val target = parts[4]
                        val op = parts[1]

                        if (op == "AND") {
                            val left = if (parts[0].toIntOrNull() != null) parts[0].toInt() else wireValues[parts[0]]
                            val right = if (parts[2].toIntOrNull() != null) parts[2].toInt() else wireValues[parts[2]]

                            if (left != null && right != null) {
                                wireValues[target] = left and right
                                operations.remove(instruction)
                            }
                        } else if (op == "OR") {
                            val left = if (parts[0].toIntOrNull() != null) parts[0].toInt() else wireValues[parts[0]]
                            val right = if (parts[2].toIntOrNull() != null) parts[2].toInt() else wireValues[parts[2]]

                            if (left != null && right != null) {
                                wireValues[target] = left or right
                                operations.remove(instruction)
                            }
                        } else if (op == "LSHIFT") {
                            val left = if (parts[0].toIntOrNull() != null) parts[0].toInt() else wireValues[parts[0]]

                            if (left != null && parts[2].toIntOrNull() != null) {
                                wireValues[target] = left shl parts[2].toInt()
                                operations.remove(instruction)
                            }
                        } else if (op == "RSHIFT") {
                            val left = if (parts[0].toIntOrNull() != null) parts[0].toInt() else wireValues[parts[0]]

                            if (left != null && parts[2].toIntOrNull() != null) {
                                wireValues[target] = left shr parts[2].toInt()
                                operations.remove(instruction)
                            }
                        }
                    }
                }
            }
        }

        return wireValues
    }


    fun partOne(data: String): Long {
        val instructions = data.split("\n").map { line -> line.trim() }
        val wires = evaluateCircuit(instructions)

        return wires["a"]!!.toLong()
    }


    fun partTwo(data: String): Long {
        val instructions = data.split("\n").map { line -> line.trim() }
        var wires = evaluateCircuit(instructions)

        // Override wire b with the value of a
        val aValue = wires["a"]!!
        wires = evaluateCircuit(instructions + "$aValue -> b")

        return wires["a"]!!.toLong()
    }
}