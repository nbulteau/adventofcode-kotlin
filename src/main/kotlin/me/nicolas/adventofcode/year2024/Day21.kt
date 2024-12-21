package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// --- Day 21: Keypad Conundrum ---
// https://adventofcode.com/2024/day/21
fun main() {
    val data = readFileDirectlyAsText("/year2024/day21/data.txt")
    val day = Day21(2024, 21)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}


/**
The solution to this problem involves simulating the process of controlling multiple robots to type codes on a numeric keypad using a series of directional keypads.
The goal is to find the shortest sequence of button presses required to type each code and then calculate the complexity based on the length of these sequences and the numeric part of the code.

Part One
Keypad Setup:
- Numeric Keypad: A 4x3 grid with numbers and an 'A' button.
- Directional Keypad: A 2x3 grid with directional buttons and an 'A' button.
Initial Position:
The robot starts at the 'A' button on the numeric keypad.
Movement Simulation:
- The robot moves using the directional keypad to reach the desired button on the numeric keypad.
- The sequence of movements is calculated to ensure the robot does not move to an invalid position.
Sequence Calculation:
- For each code, calculate the shortest sequence of button presses required to type the code on the numeric keypad.
- Multiply the length of this sequence by the numeric part of the code to get the complexity.
Sum of Complexities:
- Sum the complexities of all codes to get the final result.

Part Two
Increased Complexity:
- The number of directional keypads increases to 25, forming a chain of robots controlling each other.
Recursive Calculation:
- The process is similar to Part One but involves more layers of robots.
- Each robot controls the next robot in the chain, and the sequence of movements is calculated recursively.
Sequence Calculation:
- For each code, calculate the shortest sequence of button presses required to type the code on the numeric keypad, considering the increased number of robots.
- Multiply the length of this sequence by the numeric part of the code to get the complexity.
Sum of Complexities:
- Sum the complexities of all codes to get the final result.
 */


class Day21(year: Int, day: Int, title: String = "Keypad Conundrum") : AdventOfCodeDay(year, day, title) {

    private val numericKeypad = arrayOf(
        "789",
        "456",
        "123",
        " 0A"
    ).map { it.toCharArray() }

    private val directionalKeypad = arrayOf(
        " ^A",
        "<v>"
    ).map { it.toCharArray() }

    private operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>) = first + pair.first to second + pair.second

    // Generate all permutations of a list of characters
    private fun List<Char>.permutations(): Set<String> {
        if (this.size <= 1) {
            return setOf(this.joinToString(""))
        }

        val result = mutableSetOf<String>()
        for (index in this.indices) {
            val remaining = this.toMutableList().apply { removeAt(index) }
            for (perm in remaining.permutations()) {
                result.add(this[index] + perm)
            }
        }
        return result
    }

    private fun getAllValidSequences(
        keypad: List<CharArray>,
        start: Pair<Int, Int>,
        target: Pair<Int, Int>
    ): Set<String> {
        val horizontal = (if (start.first > target.first) {
            "<"
        } else {
            ">"
        }).repeat(abs(start.first - target.first))

        val vertical = (if (start.second > target.second) {
            "^"
        } else {
            "v"
        }).repeat(abs(start.second - target.second))

        val charSequenceToReachTarget = (horizontal + vertical).toList()

        return charSequenceToReachTarget.permutations()
            // Filter out invalid sequences
            .filter { permutation ->
                var position = start
                permutation.all { movement ->
                    position += when (movement) {
                        '>' -> 1 to 0
                        '<' -> -1 to 0
                        'v' -> 0 to 1
                        '^' -> 0 to -1
                        else -> error("Unexpected movement: $movement")
                    }
                    // Check if the position is valid
                    keypad[position.second][position.first] != ' '
                }
            }
            .map { permutation -> permutation + 'A' }
            .toSet()
    }

    private fun List<CharArray>.findKey(char: Char): Pair<Int, Int> {
        val row = indexOfFirst { charArray -> charArray.contains(char) }
        val col = this[row].indexOf(char)

        return col to row
    }

    private fun calculateKeyStrokes(
        keypadList: List<List<CharArray>>,
        keysSequence: String,
        cache: MutableMap<Pair<Int, String>, Long> = mutableMapOf()
    ): Long {
        // Base case when there are no more pads to visit
        if (keypadList.isEmpty()) {
            return keysSequence.length.toLong()
        }

        val currentPad = keypadList.first()

        // mapKey is a pair of the current pad size and the keys sequence (one cache per pad size)
        val mapKey = keypadList.size to keysSequence
        cache[mapKey]?.let { sequenceLength ->
            return sequenceLength
        }

        var position = currentPad.findKey('A')
        var sequenceLength = 0L
        keysSequence.forEach { key ->
            val nextPosition = currentPad.findKey(key)
            val minStrokes = getAllValidSequences(currentPad, position, nextPosition).minOf {
                calculateKeyStrokes(keypadList.drop(1), it, cache)
            }
            sequenceLength += minStrokes
            position = nextPosition
        }

        cache[mapKey] = sequenceLength

        return sequenceLength
    }

    fun partOne(data: String): Long {
        val keypadList = listOf(numericKeypad, directionalKeypad, directionalKeypad)

        return data.lines().sumOf { line ->
            val numericPart = line.substring(0, line.length - 1).toLong()
            val keyStrokes = calculateKeyStrokes(keypadList, line)

            numericPart * keyStrokes
        }
    }

    fun partTwo(data: String): Long {
        val keypadList = listOf(numericKeypad) + List(25) { directionalKeypad }

        return data.lines().sumOf { line ->
            val numericPart = line.substring(0, line.length - 1).toLong()
            val keyStrokes = calculateKeyStrokes(keypadList, line, mutableMapOf())

            numericPart * keyStrokes
        }
    }
}
