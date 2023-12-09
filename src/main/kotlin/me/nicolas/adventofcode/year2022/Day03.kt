package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day03/training.txt")
    val data = readFileDirectlyAsText("/year2022/day03/data.txt")

    val lines = data.split("\n")

    val day = Day03(2022, 3, "Rucksack Reorganization")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

private class Day03(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

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
