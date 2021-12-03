package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.readFileDirectlyAsText


// https://adventofcode.com/2021/day/3
fun main() {

    val training = readFileDirectlyAsText("/year2021/day03/training.txt")
    val data = readFileDirectlyAsText("/year2021/day03/data.txt")

    val lines = data.split("\n")

    // Part One
    Day03().partOne(lines)

    // Part Two
    Day03().partTwo(lines)
}

private class Day03 {
    fun partOne(lines: List<String>) {
        var gamma = ""
        var epsilon = ""

        for (index in lines[0].indices) {
            val str = lines.map { line -> line[index] }.joinToString()
            val zero = str.count { char -> char == '0' }
            val one = str.count { char -> char == '1' }
            gamma += if (zero > one) '0' else '1'
            epsilon += if (zero < one) '0' else '1'
        }

        val result = gamma.toInt(2) * epsilon.toInt(2)

        println("Part one answer = $result")
    }

    fun partTwo(lines: List<String>) {

        val oxygenGeneratorRating = oxygenGeneratorRating(lines)
        val co2ScrubberRating = co2ScrubberRating(lines)
        val result = oxygenGeneratorRating.toInt(2) * co2ScrubberRating.toInt(2)

        println("Part two answer = $result")
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
