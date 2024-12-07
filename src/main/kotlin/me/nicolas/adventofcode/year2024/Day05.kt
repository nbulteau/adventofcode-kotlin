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
        val parts = data.split("\r\n\r\n")
        val rules =
            parts[0].split("\n").groupBy({ it.split("|")[0].trim().toInt() }) { it.substringAfter("|").trim().toInt() }
        val updates = parts[1].split("\n").map { line -> line.split(",") }
            .map { update -> update.map { page -> page.trim().toInt() } }

        return updates.filter { isValid(rules, it) }.sumOf { it[it.size / 2] }
    }


    fun partTwo(data: String): Int {
        val parts = data.split("\r\n\r\n")
        val rules =
            parts[0].split("\n").groupBy({ it.split("|")[0].trim().toInt() }) { it.substringAfter("|").trim().toInt() }
        val updates = parts[1].split("\n").map { line -> line.split(",") }
            .map { update -> update.map { page -> page.trim().toInt() } }
        val incorrect = updates.filter { !isValid(rules, it) }

        val sorted = incorrect.map {
            val comparator = fun(a: Int, b: Int): Int {
                return when {
                    (rules[a]?.contains(b) == true) -> -1
                    (rules[b]?.contains(a) == true) -> 1
                    else -> 0
                }
            }
            it.sortedWith(comparator)
        }

        return sorted.sumOf { it[it.size / 2] }
    }
}

private fun isValid(rules: Map<Int, List<Int>>, update: List<Int>): Boolean {
    val updateList = update.toMutableList()

    while (updateList.isNotEmpty()) {
        val head = updateList.removeFirst()
        if (updateList.any { rules[it]?.contains(head) == true }) {
            return false
        }
    }

    return true
}

