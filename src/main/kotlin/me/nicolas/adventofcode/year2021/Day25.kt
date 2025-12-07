package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2021/day/25
fun main() {
    val data = readFileDirectlyAsText("/year2021/day25/data.txt")
    val day = Day25(2021, 25)
    prettyPrintPartOne { day.partOne(data) }
}

class Day25(year: Int, day: Int, title: String = "Sea Cucumber") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val inputs = data.split("\n")

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