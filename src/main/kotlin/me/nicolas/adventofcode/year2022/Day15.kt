package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val training = readFileDirectlyAsText("/year2022/day15/training.txt")
    val data = readFileDirectlyAsText("/year2022/day15/data.txt")

    val inputs = data.split("\n")

    val day = Day15("---- Day 15: Beacon Exclusion Zone ---", "https://adventofcode.com/2022/day/15")
    prettyPrintPartOne { day.partOne(inputs) }
    prettyPrintPartTwo { day.partTwo(inputs) }
}

private class Day15(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(inputs: List<String>): Int {
        val row = mutableMapOf<Int, Char>()
        val sensors = build(inputs)

        val y = 2000000
        sensors.forEach { (sensor, beacon) ->
            val distance = sensor.manhattanDistance(beacon).toInt()
            for (x in sensor.first - distance..sensor.first + distance) {
                if (sensor.manhattanDistance(Pair(x, y)) <= distance) {
                    row[x] = '#'
                }
            }
        }
        sensors.filter { (sensor, _) -> sensor.second == y }.forEach { (sensor, _) -> row[sensor.first] = 'S' }
        sensors.filter { (_, beacon) -> beacon.second == y }.forEach { (_, beacon) -> row[beacon.first] = 'B' }

        return row.count { it.value == '#' }
    }

    /**
     * sensor at 8,7
     * beacon at 2,10
     * distance = 9
     * x = 8 -> diff = 9 -> Range = ]-2, 16]
     *
     *        0123456789012345678
     *   07 .#########S#########........
     *   08 ..#################.........
     *   09 ...###############..........
     *   10 ....B############...........
     *
     */
    fun partTwo(inputs: List<String>): Long {
        val sensors = build(inputs)

        for (x in 0..4000000) {
            val ranges = mutableListOf<Pair<Long, Long>>()
            sensors.forEach { (sensor, beacon) ->
                val distance = sensor.manhattanDistance(beacon)
                val diff = distance - abs(x - sensor.first)
                if (diff >= 0) {
                    ranges.add(Pair(sensor.second - diff, sensor.second + diff))
                }
            }

            ranges.sortBy { it.first }

            var rightBound = 0L
            for (range in ranges) {
                if (range.first > rightBound) {
                    return x * 4000000L + (range.first - 1) // Don't forget minus 1 !
                }
                rightBound = max(rightBound, range.second)
            }
        }

        return 0L
    }

    private fun build(inputs: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val regex = Regex("Sensor at x=([-\\d]+), y=([-\\d]+): closest beacon is at x=([-\\d]+), y=([-\\d]+)")
        return inputs.map { line ->
            val (sx, sy, bx, by) = regex.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
            Pair(Pair(sx, sy), Pair(bx, by))
        }
    }

    private fun Pair<Int, Int>.manhattanDistance(other: Pair<Int, Int>): Long =
        abs(this.first - other.first).toLong() + abs(this.second - other.second).toLong()
}


