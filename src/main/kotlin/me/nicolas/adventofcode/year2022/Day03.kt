package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day03/training.txt")
    val data = readFileDirectlyAsText("/year2022/day03/data.txt")

    val lines = data.split("\n")

    val day = Day03("--- Day 3: Rucksack Reorganization ---", "https://adventofcode.com/2022/day/3")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

private class Day03(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

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
