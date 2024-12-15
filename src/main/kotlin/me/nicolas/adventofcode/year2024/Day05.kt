package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 5: Print Queue ---
// https://adventofcode.com/2024/day/5
fun main() {
    val data = readFileDirectlyAsText("/year2024/day05/data.txt")
    val day = Day05(2024, 5, "Print Queue")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day05(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val (rules, updates) = parseData(data)

        return updates
            .filter { update -> update.isValid(rules) }
            .sumOf { update -> update[update.size / 2] }
    }


    fun partTwo(data: String): Int {
        val (rules, updates) = parseData(data)

        val comparator = fun(a: Int, b: Int): Int {
            return when {
                (rules[a]?.contains(b) == true) -> -1
                (rules[b]?.contains(a) == true) -> 1
                else -> 0
            }
        }

        return updates
            .filter { update -> !update.isValid(rules) }
            .map { update -> update.sortedWith(comparator) }
            .sumOf { update -> update[update.size / 2] }
    }

    private fun parseData(data: String): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        val input = data.lines()
        val split = input.indexOf("") // Find the first empty line

        val rules = input.subList(0, split)
            .groupBy({ line -> line.split("|")[0].trim().toInt() }) { line ->
                line.substringAfter("|").trim().toInt()
            }
        val updates = input.drop(split + 1)
            .map { line -> line.split(",") }
            .map { update -> update.map { page -> page.trim().toInt() } }

        return Pair(rules, updates)
    }


    private fun List<Int>.isValid(rules: Map<Int, List<Int>>): Boolean {
        val updateList = this.toMutableList()

        while (updateList.isNotEmpty()) {
            val head = updateList.removeFirst()
            if (updateList.any { rules[it]?.contains(head) == true }) {
                return false
            }
        }

        return true
    }
}


