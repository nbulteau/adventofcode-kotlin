package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


// https://adventofcode.com/2021/day/25
@ExperimentalTime
fun main() {

    val training = readFileDirectlyAsText("/year2021/day25/training.txt")
    val smallTraining = readFileDirectlyAsText("/year2021/day25/small-training.txt")
    val data = readFileDirectlyAsText("/year2021/day25/data.txt")

    val inputs = data.split("\n")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day25().partOne(inputs) })
}


private class Day25 {

    fun partOne(inputs: List<String>): Int {
        val region = Region(parseInputs(inputs))

        var step = 0
        do {
            //region.display()
            val east = region.nextStepEast()
            val south = region.nextStepSouth()
            step++
        } while (east || south)

        return step
    }

    private class Region(private var map: Array<CharArray>) {

        private val height = map.size

        private val width = map.first().size

        fun nextStepEast(): Boolean {
            val movedMap = Array(height) { BooleanArray(width) }
            var moved = false
            // have to move
            for (y in 0 until height) {
                for (x in 0 until width) {
                    movedMap[y][x] = map[y][x] == '>' && map[y][(x + 1) % width] == '.'
                    if (movedMap[y][x]) {
                        moved = true
                    }
                }
            }
            // move
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (movedMap[y][x]) {
                        map[y][x] = '.'
                        map[y][(x + 1) % width] = '>'
                    }
                }
            }

            return moved
        }

        fun nextStepSouth(): Boolean {
            val movedMap = Array(height) { BooleanArray(width) }

            var moved = false
            for (y in 0 until height) {
                for (x in 0 until width) {
                    movedMap[y][x] = map[y][x] == 'v' && map[(y + 1) % height][x] == '.'
                    if (movedMap[y][x]) {
                        moved = true
                    }
                }
            }
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (movedMap[y][x]) {
                        map[y][x] = '.'
                        map[(y + 1) % height][x] = 'v'
                    }
                }
            }

            return moved
        }

        fun display() {
            println()
            map.forEach { string -> println(string) }
        }

    }

    private fun parseInputs(inputs: List<String>): Array<CharArray> {
        return inputs.map { line ->
            line.toCharArray()
        }.toTypedArray()
    }
}