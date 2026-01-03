package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.*

// https://adventofcode.com/2019/day/8
fun main() {
    val data = readFileDirectlyAsText("/year2019/day08/data.txt")
    val day = Day08(2019, 8, "Space Image Format")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day08(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    val width = 25
    val height = 6

    /**
     * partOne : Find the layer that contains the fewest 0 digits. On that layer, what is the number of 1 digits multiplied by the number of 2 digits?
     */
    fun partOne(data: String): Int {
        val layers = data.map { it.toString().toInt() }.windowed(width, width).windowed(height, height)
        val flattenedLayers = layers.map { it.flatten() }
        val p1Layer = flattenedLayers.minBy { ints -> ints.count { it == 0 } }

        return p1Layer.count { it == 1 } * p1Layer.count { it == 2 }
    }

    /**
     * partTwo : Decode the image.
     * Each pixel of the image is 0 (black), 1 (white), or 2 (transparent).
     * The layers are rendered with the first layer in front and the last layer in back.
     */
    fun partTwo(data: String): Int {
        val layers = data.map { it.toString().toInt() }.windowed(width, width).windowed(height, height)
        val grids = layers.map { layer ->
            val map = layer.flatMapIndexed { x, row ->
                row.mapIndexed { y, pixel ->
                    Pair(x, y) to pixel
                }
            }.toMap().toMutableMap()
            Grid(map)
        }.toList()
        val pixels = grids.first().indices.map { point ->
            grids.map { it[point] }.first { it != 2 } == 1
        }
        println(pixels.chunked(width).joinToString("\n") { booleans -> booleans.joinToString("") { if (it) "X" else " " } })

        return 42
    }
}