package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// --- Day 3: Perfectly Spherical Houses in a Vacuum ---
// https://adventofcode.com/2015/day/3
fun main() {
    val data = readFileDirectlyAsText("/year2015/day03/data.txt")
    val day = Day03(2015, 3, "Perfectly Spherical Houses in a Vacuum")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day03(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {


    fun partOne(directions : String): Int {
        val houses = mutableSetOf<Pair<Int, Int>>()

        var x = 0
        var y = 0
        houses.add(Pair(x, y))

        directions.forEach { direction ->
            when (direction) {
                '^' -> y++
                'v' -> y--
                '>' -> x++
                '<' -> x--
            }
            houses.add(Pair(x, y))
        }

        return houses.size
    }

    fun partTwo(directions : String): Int {
        val houses = mutableSetOf<Pair<Int, Int>>()

        var xSanta = 0
        var ySanta = 0
        houses.add(Pair(xSanta, ySanta))

        var xRoboSanta = 0
        var yRoboSanta = 0
        houses.add(Pair(xRoboSanta, yRoboSanta))

        directions.forEachIndexed { index, direction ->
            if (index % 2 == 0) {
                when (direction) {
                    '^' -> ySanta++
                    'v' -> ySanta--
                    '>' -> xSanta++
                    '<' -> xSanta--
                }
                houses.add(Pair(xSanta, ySanta))
            } else {
                when (direction) {
                    '^' -> yRoboSanta++
                    'v' -> yRoboSanta--
                    '>' -> xRoboSanta++
                    '<' -> xRoboSanta--
                }
                houses.add(Pair(xRoboSanta, yRoboSanta))
            }
        }

        return houses.size
    }
}
