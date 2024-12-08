package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 8: ---
// https://adventofcode.com/2015/day/8
fun main() {
    val data = readFileDirectlyAsText("/year2015/day08/data.txt")
    val day = Day08(2015, 8)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day08(year: Int, day: Int, title: String = "Matchsticks ") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {

        var totalCodeChars = 0
        var totalMemoryChars = 0

        data.split("\n").forEach { line ->
            // Remove quotes around the string literal
            val codeString = line.trim()
            totalCodeChars += codeString.length

            // Decode the string to find the number of characters in memory
            val memoryString = StringBuilder()
            var index = 1 // Start from the first character inside the quotes
            while (index < codeString.length - 1) {
                when (codeString[index]) {
                    '\\' -> {
                        index++
                        when (codeString[index]) {
                            'n' -> memoryString.append('\n')
                            't' -> memoryString.append('\t')
                            '\"' -> memoryString.append('\"')
                            '\\' -> memoryString.append('\\')
                            'x' -> {
                                // Handle \x followed by two hex digits
                                val hexValue = codeString.substring(index + 1, index + 3).toInt(16)
                                memoryString.append(hexValue.toChar())
                                index += 2
                            }
                        }
                    }
                    else -> {
                        memoryString.append(codeString[index])
                    }
                }
                index++
            }

            totalMemoryChars += memoryString.length
        }

        return totalCodeChars - totalMemoryChars
    }

    fun partTwo(data: String): Int {
        var totalCodeChars = 0
        var totalEncodedChars = 0

        data.split("\n").forEach { line ->
            // Remove quotes around the string literal
            val codeString = line.trim()
            totalCodeChars += codeString.length

            // Encode the string to find the number of characters in the encoded representation
            val encodedStringBuilder = StringBuilder()
            encodedStringBuilder.append("\"") // Add the opening quote
            for (char in codeString) {
                when (char) {
                    '\\' -> encodedStringBuilder.append("\\\\")
                    '\"' -> encodedStringBuilder.append("\\\"")
                    else -> encodedStringBuilder.append(char)
                }
            }
            encodedStringBuilder.append("\"") // Add the closing quote

            val encodedString = encodedStringBuilder.toString()
            totalEncodedChars += encodedString.length
        }

        return totalEncodedChars - totalCodeChars
    }
}