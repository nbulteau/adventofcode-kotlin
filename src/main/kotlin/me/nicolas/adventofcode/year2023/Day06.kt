package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    val data = readFileDirectlyAsText("/year2023/day06/data.txt")
    val day = Day06(2023, 6, "Wait For It")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
    prettyPrintPartTwo("(with maths)") { day.partTwo(data) }
}

class Day06(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    data class Race(val time: Long, val distance: Long) {

        // For each whole millisecond you spend at the beginning of the race holding down the button,
        // the boat's speed increases by one millimeter per millisecond.
        fun waysToWin(): Long {
            var result = 0L
            for (acceleration in (0..time)) {
                if (acceleration * (time - acceleration) > distance) {
                    result++
                }
            }
            return result
        }

        fun waysToWinWithMaths(): Long {
            val b = time / 2
            val d = sqrt((b * b - distance).toDouble())

            return (ceil(b + d) - floor(b - d) - 1).toLong()
        }
    }

    fun partOne(data: String): Long {
        val races = buildRaces(data)

        return races.map { race -> race.waysToWin() }.reduce { acc, i -> acc * i }
    }

    fun partTwo(data: String): Long {
        val race = buildRace(data)

        return race.waysToWin()
    }

    fun partTwoWithMaths(data: String): Long {
        val race = buildRace(data)

        return race.waysToWinWithMaths()
    }
    private fun buildRaces(data: String): List<Race> {
        val lines = data.split("\n")
        val times = lines[0].split(":")[1].trim().split(Regex(" +"))
        val distances = lines[1].split(":")[1].trim().split(Regex(" +"))

        return times.indices.map { Race(times[it].toLong(), distances[it].toLong()) }
    }

    private fun buildRace(input: String): Race {
        val time = input.lines()[0].split(":")[1].replace(Regex(" +"), "").toLong()
        val distance = input.lines()[1].split(":")[1].replace(Regex(" +"), "").toLong()

        return Race(time, distance)
    }
}

