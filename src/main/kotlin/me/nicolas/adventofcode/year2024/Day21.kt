package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// --- Day 21: ---
// https://adventofcode.com/2024/day/21
fun main() {
    val data = readFileDirectlyAsText("/year2024/day21/data.txt")
    val day = Day21(2024, 21)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day21(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

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
