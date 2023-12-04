package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


// https://adventofcode.com/2021/day/20
@ExperimentalTime
fun main() {

    val training = readFileDirectlyAsText("/year2021/day20/training.txt")
    val data = readFileDirectlyAsText("/year2021/day20/data.txt")

    val lines = data.split("\n\n")
    val algorithm = lines.first()
    val image = lines.last()

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day20().partOne(algorithm.toCharArray(), image) })


    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day20().partTwo(algorithm.toCharArray(), image) })

}

private class Day20 {

    fun partOne(algorithm: CharArray, inputImage: String): Int {

        return solve(inputImage, algorithm, 2)
    }

    fun partTwo(algorithm: CharArray, inputImage: String): Int {

        return solve(inputImage, algorithm, 50)
    }

    private fun solve(inputImage: String, algorithm: CharArray, steps: Int): Int {
        val image = inputImage.split("\n").map { it.toCharArray() }

        var enhanced = image.expand(steps)

        repeat(steps) {
            enhanced = enhance(enhanced, algorithm)
        }

        return enhanced.fold(0) { acc, chars -> acc + chars.count { it == '#' } }
    }

    private fun enhance(image: List<CharArray>, algorithm: CharArray): List<CharArray> {
        val enhanced = mutableListOf<CharArray>()
        for (row in image.indices) {
            var line = ""
            for (col in image.first().indices) {
                val value = image.getValue(row, col)
                line += algorithm[value]
            }
            enhanced.add(line.toCharArray())
        }
        return enhanced
    }

    private fun List<CharArray>.getValue(x: Int, y: Int): Int {
        var number = ""
        for (indexX in x - 1..x + 1) {
            for (indexY in y - 1..y + 1) {
                number += if ((indexY !in 0 until size) || (indexX !in 0 until first().size)) {
                    this[0][0] // the tricky part
                } else {
                    this[indexX][indexY]
                }
            }
        }
        // turning dark pixels (.) into 0 and light pixels (#) into 1
        return number.map { if (it == '#') 1 else 0 }.joinToString("").toInt(2)
    }

    private fun List<CharArray>.expand(padding: Int): List<CharArray> {
        val height = size
        val width = first().size

        val output = List(height + 2 * padding) {
            CharArray(width + 2 * padding) { '.' }
        }

        forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, c -> output[padding + rowIndex][padding + columnIndex] = c }
        }

        return output
    }

    private fun List<CharArray>.display() {
        println()
        this.forEach { string -> println(string) }
    }
}



