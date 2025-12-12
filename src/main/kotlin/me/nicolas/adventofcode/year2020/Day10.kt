package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 10: Adapter Array ---
// https://adventofcode.com/2020/day/10
fun main() {
    val data = readFileDirectlyAsText("/year2020/day10/data.txt")
    val day = Day10(2020, 10, "Adapter Array")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day10(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val adapters = data.split("\n").filter { it.isNotEmpty() }.map { it.toInt() }.sorted()
        val differences = (listOf(0) + adapters).zip(adapters + (adapters.last() + 3))
            .map { (a, b) -> b - a }
        return differences.count { it == 1 } * differences.count { it == 3 }
    }

    fun partTwo(data: String): Long {
        val adapters = data.split("\n").filter { it.isNotEmpty() }.map { it.toInt() }.sorted()
        val allAdapters = listOf(0) + adapters + (adapters.last() + 3)
        val paths = mutableMapOf(0 to 1L)
        for (adapter in allAdapters.drop(1)) {
            paths[adapter] = (1..3).sumOf { diff -> paths.getOrDefault(adapter - diff, 0) }
        }
        return paths[allAdapters.last()]!!
    }
}
