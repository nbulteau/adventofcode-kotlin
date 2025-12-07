package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2021/day/6
fun main() {
    val data = readFileDirectlyAsText("/year2021/day06/data.txt")
    val day = Day06(2021, 6)
    val input = data.split(",").map { it.toInt() }
    val lanternfishs = (0..8).associateWith { index ->
        input.count { it == index }.toLong()
    }
    prettyPrintPartOne { day.partOne(lanternfishs) }
    prettyPrintPartTwo { day.partTwo(lanternfishs) }
}

class Day06(year: Int, day: Int, title: String = "Lanternfish") : AdventOfCodeDay(year, day, title) {

    fun partOne(lanternfishs: Map<Int, Long>): Long = solve(lanternfishs, 80)

    fun partTwo(lanternfishs: Map<Int, Long>): Long = solve(lanternfishs, 256)

    fun solve(lanternfishs: Map<Int, Long>, nbDays: Int): Long {

        val lanternfishsStates = lanternfishs.toMutableMap()
        //println("Initial state: $lanternfishsStates")
        for (day in 1..nbDays) {
            updateLanterfishStates(lanternfishsStates)
            //println("Day $day: $lanternfishsStates")
        }

        return lanternfishsStates.entries.sumOf { it.value }
    }

    private fun updateLanterfishStates(lanternfishs: MutableMap<Int, Long>) {

        val nbNewLanternfish = lanternfishs[0] ?: 0L

        for (index in 0..7) {
            lanternfishs[index] = lanternfishs[index + 1] ?: 0L
        }
        lanternfishs[6] = (lanternfishs[6] ?: 0L) + nbNewLanternfish
        lanternfishs[8] = nbNewLanternfish
    }
}