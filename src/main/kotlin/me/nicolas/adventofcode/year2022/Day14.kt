package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.max
import kotlin.math.min

fun main() {
    val training = readFileDirectlyAsText("/year2022/day14/training.txt")
    val data = readFileDirectlyAsText("/year2022/day14/data.txt")

    val inputs = data.split("\n")

    val day = Day14("--- Day 14: Regolith Reservoir ---", "https://adventofcode.com/2022/day/14")
    prettyPrintPartOne { day.partOne(inputs) }
    prettyPrintPartTwo { day.partTwo(inputs) }
}

private class Day14(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(inputs: List<String>): Int {
        val (cave, sourceOfTheSand) = buildCave(inputs)

        return simulateTheFallingSandPartOne(cave, sourceOfTheSand)
    }

    fun partTwo(inputs: List<String>): Int {
        val (cave, sourceOfTheSand) = buildCave(inputs)

        // The floor is an infinite horizontal line with a y coordinate equal to two plus the highest y coordinate of any point in your scan.
        val xMin = cave.keys.minBy { it.first }.first - 135 // Magic number
        val xMax = cave.keys.maxBy { it.first }.first + 135 // Magic number
        val yMax = cave.keys.maxBy { it.second }.second + 2
        (xMin..xMax).forEach { cave[Pair(it, yMax)] = '#' }

        return simulateTheFallingSandPartTwo(cave, sourceOfTheSand)
    }

    private fun simulateTheFallingSandPartOne(
        cave: MutableMap<Pair<Int, Int>, Char>,
        sourceOfTheSand: Pair<Int, Int>,
    ): Int {
        val yMax = cave.keys.maxBy { it.second }.second
        var fallingSand = sourceOfTheSand

        var nbUnitsOfSand = 0
        // sand flows out the bottom, falling into the endless void ?
        while (fallingSand.second < yMax) {
            val south = fallingSand.south()
            if (!cave.containsKey(south)) {
                fallingSand = south
                continue
            }
            val southwest = fallingSand.southwest()
            if (!cave.containsKey(southwest)) {
                fallingSand = southwest
                continue
            }
            val southeast = fallingSand.southeast()
            if (!cave.containsKey(southeast)) {
                fallingSand = southeast
                continue
            }
            cave[fallingSand] = 'o'
            fallingSand = sourceOfTheSand
            nbUnitsOfSand++
        }

        //cave.display()
        return nbUnitsOfSand
    }

    private fun simulateTheFallingSandPartTwo(
        cave: MutableMap<Pair<Int, Int>, Char>,
        sourceOfTheSand: Pair<Int, Int>,
    ): Int {
        var fallingSand = sourceOfTheSand

        var nbUnitsOfSand = 0
        // the source of the sand becomes blocked ?
        while (cave[sourceOfTheSand] != 'o') {
            val south = fallingSand.south()
            if (!cave.containsKey(south)) {
                fallingSand = south
                continue
            }
            val southwest = fallingSand.southwest()
            if (!cave.containsKey(southwest)) {
                fallingSand = southwest
                continue
            }
            val southeast = fallingSand.southeast()
            if (!cave.containsKey(southeast)) {
                fallingSand = southeast
                continue
            }
            cave[fallingSand] = 'o'
            fallingSand = sourceOfTheSand
            nbUnitsOfSand++
        }

        //cave.display()
        return nbUnitsOfSand
    }

    private fun Pair<Int, Int>.south() = Pair(this.first, this.second + 1)
    private fun Pair<Int, Int>.southwest() = Pair(this.first - 1, this.second + 1)
    private fun Pair<Int, Int>.southeast() = Pair(this.first + 1, this.second + 1)

    private fun buildCave(inputs: List<String>): Pair<MutableMap<Pair<Int, Int>, Char>, Pair<Int, Int>> {
        val cave = mutableMapOf<Pair<Int, Int>, Char>()
        val sourceOfTheSand = Pair(500, 0)
        cave[sourceOfTheSand] = '+'

        inputs.forEach { line ->
            line.split(" -> ").windowed(2, 1)
                .forEach { (a, b) ->
                    val p1 = Pair(a.substringBefore(',').toInt(), a.substringAfter(',').toInt())
                    val p2 = Pair(b.substringBefore(',').toInt(), b.substringAfter(',').toInt())
                    if (p1.first == p2.first) {
                        (min(p1.second, p2.second)..max(p1.second, p2.second)).forEach {
                            cave[Pair(p1.first, it)] = '#'
                        }
                    } else {
                        (min(p1.first, p2.first)..max(p1.first, p2.first)).forEach { cave[Pair(it, p1.second)] = '#' }
                    }
                }
        }

        return Pair(cave, sourceOfTheSand)
    }

    private fun Map<Pair<Int, Int>, Char>.display() {
        val xMin = keys.minByOrNull { it.first }!!.first
        val xMax = keys.maxByOrNull { it.first }!!.first
        val yMin = keys.minByOrNull { it.second }!!.second
        val yMax = keys.maxByOrNull { it.second }!!.second

        for (y in yMin..yMax) {
            for (x in xMin..xMax) {
                print(this.getOrDefault(Pair(x, y), '.'))
            }
            println()
        }
        println()
    }
}


