package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2018/day/10
fun main() {
    val data = readFileDirectlyAsText("/year2018/day10/data.txt")
    val day = Day10(2018, 10, "The Stars Align")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day10(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        // Parse the input data into a list of Light objects
        val lights = parseData(data)
        // Expand the lights and get the new state of lights and the number of seconds passed
        val (newLights, seconds) = expand(lights)
        newLights.display(seconds)

        return 42
    }

    fun partTwo(data: String): Int {
        val lights = parseData(data)
        val (_, seconds) = expand(lights)

        // Return the number of seconds passed
        return seconds
    }

    // Expand the lights until they start to move apart
    private fun expand(lights: List<Light>): Pair<List<Light>, Int> {
        var newLights = lights
        var rangeXBefore = IntRange(newLights.minBy { light -> light.x }.x, newLights.maxBy { light -> light.x }.x)
        var seconds = 0
        do {
            val temp = newLights.map { light -> light.move() }
            val rangeXAfter = IntRange(temp.minBy { light -> light.x }.x, temp.maxBy { light -> light.x }.x)

            if (rangeXBefore.count() < rangeXAfter.count()) {
                return Pair(newLights, seconds)
            } else {
                newLights = temp
                rangeXBefore = rangeXAfter
            }
            seconds++
        } while (true)
    }

    // Display the current state of lights
    private fun List<Light>.display(nbSteps: Int) {

        val lightSet = this.map { Pair(it.x, it.y) }.toSet()
        val rangeX = IntRange(this.minBy { it.x }.x, this.maxBy { it.x }.x)
        val rangeY = IntRange(this.minBy { it.y }.y, this.maxBy { it.y }.y)

        val string = rangeY.joinToString(separator = "\n") { y ->
            rangeX.map { x ->
                if (Pair(x, y) in lightSet) '#' else '.'
            }.joinToString(separator = "")
        }
        println("Step $nbSteps")
        println(string)
    }

    private data class Velocity(val x: Int, val y: Int) {
        override fun toString(): String {
            return "#"
        }
    }

    private data class Light(val x: Int, val y: Int, val velocity: Velocity) {
        fun move(): Light {
            return Light(x + velocity.x, y + velocity.y, velocity)
        }
    }

    // Parse the input data into a list of Light objects
    private fun parseData(data: String): List<Light> {
        val inputLines = data.split("\n")
        return inputLines.map {
            val (x, y, vx, vy) = extractInts(it)
            Light(x, y, Velocity(vx, vy))
        }
    }

    // Extract integers from a string
    private fun extractInts(data: String): List<Int> {
        return "-?\\d+".toRegex().findAll(data).map { it.value.toInt() }.toList()
    }
}


