package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


// https://adventofcode.com/2021/day/3
fun main() {

    val training = readFileDirectlyAsText("/year2021/day03/training.txt")
    val data = readFileDirectlyAsText("/year2021/day03/data.txt")

    val lines = data.split("\n")


    prettyPrint("Part one answer", measureTimedValue { Day03().partOne(lines) })

    prettyPrint("Part one answer", measureTimedValue { Day03().partTwo(lines) })
}

private class Day03 {

    fun partOne(lines: List<String>): Int {
        var gamma = ""
        var epsilon = ""

        for (index in lines[0].indices) {
            val str = lines.map { line -> line[index] }.joinToString()
            val zero = str.count { char -> char == '0' }
            val one = str.count { char -> char == '1' }
            gamma += if (zero > one) '0' else '1'
            epsilon += if (zero < one) '0' else '1'
        }

        return gamma.toInt(2) * epsilon.toInt(2)
    }

    fun partTwo(lines: List<String>): Int {

        val oxygenGeneratorRating = oxygenGeneratorRating(lines)
        val co2ScrubberRating = co2ScrubberRating(lines)

        return oxygenGeneratorRating.toInt(2) * co2ScrubberRating.toInt(2)
    }


    private fun oxygenGeneratorRating(lines: List<String>): String {
        var keep = lines.toMutableList()

        var index = 0
        do {
            val str = keep.map { line -> line[index] }.joinToString()
            val zero = str.count { char -> char == '0' }
            val one = str.count { char -> char == '1' }
            val toKeep = if (one >= zero) '1' else '0'

            keep = keep.filter { line -> line[index] == toKeep }.toMutableList()
            if (keep.size == 1) {
                return keep[0]
            }
            index++
        } while (true)
    }

    private fun co2ScrubberRating(lines: List<String>): String {
        var keep = lines.toMutableList()

        var index = 0
        do {
            val str = keep.map { line -> line[index] }.joinToString()
            val zero = str.count { char -> char == '0' }
            val one = str.count { char -> char == '1' }
            val linesToKeep = if (one < zero) '1' else '0'

            keep = keep.filter { line -> line[index] == linesToKeep }.toMutableList()
            if (keep.size == 1) {
                return keep[0]
            }
            index++
        } while (true)
    }

    private fun binaryToDecimal(number: String): Long {
        var result = 0L
        var bit = 0
        var numberOfBits: Int = number.length - 1
        // Execute given number in reverse order
        while (numberOfBits >= 0) {
            if (number[numberOfBits] == '1') {
                // When get binary 1
                result += (1 shl (bit))
            }
            numberOfBits -= 1
            // Count number of bits
            bit += 1
        }
        return result
    }
}
