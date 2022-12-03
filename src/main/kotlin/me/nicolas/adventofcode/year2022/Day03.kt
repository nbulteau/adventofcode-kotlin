package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


// https://adventofcode.com/2022/day/3
@OptIn(ExperimentalTime::class)
fun main() {

    val training = readFileDirectlyAsText("/year2022/day03/training.txt")
    val data = readFileDirectlyAsText("/year2022/day03/data.txt")

    val lines = data.split("\n")


    prettyPrint("Part one answer", measureTimedValue { Day03().partOne(lines) })

    prettyPrint("Part two answer", measureTimedValue { Day03().partTwo(lines) })
}

private class Day03 {

    fun partOne(lines: List<String>): Int {
        return lines.sumOf { rucksack ->
            val (firstCompartment, secondCompartment) = rucksack.chunked(rucksack.length / 2)
            val item = firstCompartment.first { typeOfItem ->
                typeOfItem in secondCompartment
            }
            item.value()
        }
    }

    fun partTwo(lines: List<String>): Int {
        return lines.chunked(3) { groupOfRucksack ->
            val item = groupOfRucksack.joinToString("")
                .first { typeOfItem ->
                    groupOfRucksack.all { typeOfItem in it }
                }
            item.value()
        }.sum()
    }

    private fun Char.value() =
        if (this.isUpperCase()) this.code - 'A'.code + 27 else this.code - 'a'.code + 1
}
