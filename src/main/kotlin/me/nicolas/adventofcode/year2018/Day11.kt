package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// https://adventofcode.com/2018/day/11
fun main() {
    //val data = readFileDirectlyAsText("/year2018/day11/data.txt")
    val day = Day11(2018, 11, "Chronal Charge")
    prettyPrintPartOne { day.partOne(4172) }
    prettyPrintPartTwo { day.partTwo(4172) }
}

class Day11(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(serial: Int): String {
        val powerMap = buildPowerLevelMap(serial)
        // Calculate the sum of square for each point in the grid for a square of size 3
        val sumOfSquareMap = (1..298).flatMap { x ->
            (1..298).map { y ->
                val point = Pair(x, y)
                point to powerMap.sumOfSquare(Pair(x, y), 3)
            }
        }.toMap()
        // Find the point with the highest sum of square
        val key = sumOfSquareMap.maxBy { it.value }.key

        return "${key.first},${key.second}"
    }

    // This is a very slow solution, but it works and I'm too lazy to optimize it
    fun partTwo(serial: Int): String {
        val powerMap = buildPowerLevelMap(serial)
        // Calculate the sum of square for each point in the grid for a square of size 1 to 300
        val sumOfSquareMap = (1..298).flatMap { x ->
            (1..298).flatMap { y ->
                val point = Pair(x, y)
                (1..300).map { n ->
                    Triple(point, n, recurse(point, n, powerMap))
                }
            }
        }.maxBy { it.third }

        return "${sumOfSquareMap.first.first},${sumOfSquareMap.first.second},${sumOfSquareMap.second}"
    }

    // Build a map of the power level of each point in the grid using the serial number of the grid
    private fun buildPowerLevelMap(serial: Int): Map<Pair<Int, Int>, Int> {
        val powerMap = (1..298).flatMap { x ->
            (1..298).map { y ->
                val point = Pair(x, y)
                point to point.powerLevel(serial)
            }
        }.toMap()
        return powerMap
    }

    // Calculate the power level of a point in the grid using the serial number of the grid
    private fun Pair<Int, Int>.powerLevel(serial: Int): Int {
        val rackId = this.first + 10
        var powerLevel = rackId * this.second
        powerLevel += serial
        powerLevel *= rackId
        powerLevel = (powerLevel / 100) % 10
        powerLevel -= 5

        return powerLevel
    }

    // Calculate the sum of a square of size n at point
    private fun Map<Pair<Int, Int>, Int>.sumOfSquare(point: Pair<Int, Int>, n: Int): Int {
        var sum = 0
        repeat(n) { x ->
            repeat(n) { y ->
                sum += this[Pair(point.first + x, point.second + y)] ?: 0
            }
        }
        return sum
    }

    // cache for the recursive function below to avoid recalculating the same square multiple times
    private val sizeCache = mutableMapOf<Pair<Pair<Int, Int>, Int>, Int>()

    // Calculate the sum of a square of size n at point recursively using the powerMap
    private fun recurse(start: Pair<Int, Int>, side: Int, powerMap: Map<Pair<Int, Int>, Int>): Int {

        return sizeCache.getOrPut(start to side) {
            if (side == 1) {
                powerMap[start]?: 0
            } else {
                var sum = 0
                val endX = start.first + side - 1
                val endY = start.second + side - 1

                for (y in start.second..endY) {
                    sum += powerMap[Pair(endX, y)]?: 0
                }

                for (x in start.first..<endX) {
                    sum += powerMap[Pair(x, endY)]?: 0
                }

                recurse(start, side - 1, powerMap) + sum
            }
        }
    }
}
