package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day18/training.txt")
    val data = readFileDirectlyAsText("/year2022/day18/data.txt")

    val inputs = data.split("\n")

    val day = Day18(
        "--- Day 18: Boiling Boulders ---", "https://adventofcode.com/2022/day/18", inputs
    )
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo() }
}

private class Day18(title: String, adventOfCodeLink: String, inputs: List<String>) :
    AdventOfCodeDay(title, adventOfCodeLink) {

    private val cubes = inputs.map { line ->
        val (x, y, z) = line.split(",")
        Cube(x.toInt(), y.toInt(), z.toInt())
    }

    fun partOne(): Int {
        return cubes.sumOf { cube -> cube.neighbors().count { !cubes.contains(it) } }
    }

    fun partTwo(): Int {
        // we need to know the full range of all three axes (x, y, and z) plus one in each direction.
        val xRange = IntRange(cubes.minOf { it.x } - 1, cubes.maxOf { it.x } + 1)
        val yRange = IntRange(cubes.minOf { it.y } - 1, cubes.maxOf { it.y } + 1)
        val zRange = IntRange(cubes.minOf { it.z } - 1, cubes.maxOf { it.z } + 1)

        // breadth-first search
        val queue = ArrayDeque<Cube>().apply {
            add(Cube(xRange.first, yRange.first, zRange.first))
        }
        val seen = mutableSetOf<Cube>()

        while (queue.isNotEmpty()) {
            val cube = queue.removeFirst()
            seen.add(cube)

            val neighbors = cube.neighbors()
                .filter { neighbor -> !cubes.contains(neighbor) }
                .filter { neighbor -> !seen.contains(neighbor) }
                .filter { neighbor -> neighbor.x in xRange && neighbor.y in yRange && neighbor.z in zRange }
            neighbors.forEach { neighbor ->
                seen.add(neighbor)
                queue.add(neighbor)
            }
        }

        return seen.sumOf { cube -> cube.neighbors().count { cubes.contains(it) } }
    }

    private data class Cube(val x: Int, val y: Int, val z: Int) {
        fun neighbors() = listOf(
            Cube(x - 1, y, z),
            Cube(x + 1, y, z),
            Cube(x, y - 1, z),
            Cube(x, y + 1, z),
            Cube(x, y, z - 1),
            Cube(x, y, z + 1),
        )
    }
}


