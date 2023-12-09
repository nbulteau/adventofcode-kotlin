package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2021/day/7
fun main() {

    val training = readFileDirectlyAsText("/year2021/day07/training.txt")
    val data = readFileDirectlyAsText("/year2021/day07/data.txt")
    val positions = data.split(",").map { it.toInt() }

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day07().solve(positions) { position -> position } })

    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day07().solve(positions) { position -> (1..position).sumOf { it } } })

    prettyPrint(
        message = "Part two bis answer",
        measureTimedValue { Day07().solve(positions) { position -> (position * (position + 1) / 2) } })
}

private class Day07 {

    fun solve(positions: List<Int>, computeFuelConsumption: (position: Int) -> Int): Int {

        val max = positions.maxOf { it }
        val fuelConsumptions = (0..max).associateWith { destination ->
            positions.sumOf { position -> computeFuelConsumption(abs(position - destination)) }
        }
        return fuelConsumptions.values.minOf { it }
    }
}


