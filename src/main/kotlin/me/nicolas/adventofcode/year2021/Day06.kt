package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2021/day/6
@OptIn(ExperimentalTime::class)
fun main() {

    val training = readFileDirectlyAsText("/year2021/day06/training.txt")
    val data = readFileDirectlyAsText("/year2021/day06/data.txt")
    val input = data.split(",").map { it.toInt() }

    val lanternfishs = (0..8).associateWith { index ->
        input.count { it == index }.toLong()
    }

    prettyPrint("Part one answer", measureTimedValue { Day06().solve(lanternfishs, 80) })
    prettyPrint("Part two answer", measureTimedValue { Day06().solve(lanternfishs, 256) })
}

private class Day06 {

    fun solve(lanternfishs: Map<Int, Long>, nbDays: Int): Long {

        var lanternfishsStates = lanternfishs.toMutableMap()
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