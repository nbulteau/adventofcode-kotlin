package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.readFileDirectlyAsText

// https://adventofcode.com/2021/day/6
fun main() {

    val training = readFileDirectlyAsText("/year2021/day06/training.txt")
    val data = readFileDirectlyAsText("/year2021/day06/data.txt")
    val input = data.split(",").map { it.toInt() }

    val lanternfishs = (0..8).associateWith { index ->
        input.count { it == index }.toLong()
    }

    println("Part one answer = ${Day06().solve(lanternfishs, 80)}")

    println("Part two answer = ${Day06().solve(lanternfishs, 256)}")
}

private class Day06 {

    fun solve(lanternfishs: Map<Int, Long>, nbDays: Int): Long {

        var lanternfishsStates = lanternfishs
        println("Initial state: $lanternfishsStates")
        for (day in 1..nbDays) {
            lanternfishsStates = nextDay(lanternfishsStates.toMutableMap())
            println("Day $day: $lanternfishsStates")
        }

        return lanternfishsStates.entries.sumOf { it.value }
    }

    private fun nextDay(lanternfishs: MutableMap<Int, Long>): MutableMap<Int, Long> {

        val nbNewLanternfish = lanternfishs[0] ?: 0L

        for (index in 0..7) {
            lanternfishs[index] = lanternfishs[index + 1] ?: 0L
        }
        lanternfishs[6] = (lanternfishs[6] ?: 0L) + nbNewLanternfish
        lanternfishs[8] = nbNewLanternfish

        return lanternfishs
    }
}