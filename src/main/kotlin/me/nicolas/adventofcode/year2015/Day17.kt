package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 17: No Such Thing as Too Much ---
// https://adventofcode.com/2015/day/17
fun main() {
    val data = readFileDirectlyAsText("/year2015/day17/data.txt")
    val day = Day17(2015, 17)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day17(year: Int, day: Int, title: String = "No Such Thing as Too Much") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String, target: Int = 150): Int {
        val containers = data.lines().map { line -> line.toInt() }

        return findAllCombinations(containers, target).size
    }

    fun partTwo(data: String, target: Int = 150): Int {
        val allContainers = data.lines().map { line -> line.toInt() }
        val allCombinations = findAllCombinations(allContainers, target)
        val minContainers = allCombinations.minOf { containers -> containers.size }

        return allCombinations.count { containers -> containers.size == minContainers }
    }

    private fun findAllCombinations(containers: List<Int>, target: Int): List<List<Int>> {
        fun findCombinations(index: Int, remaining: Int): List<List<Int>> {
            if (remaining == 0) {
                return listOf(emptyList())
            }
            if (remaining < 0 || index == containers.size) {
                return emptyList()
            }

            val withCurrent = findCombinations(index + 1, remaining - containers[index]).map { it + containers[index] }
            val withoutCurrent = findCombinations(index + 1, remaining)

            return withCurrent + withoutCurrent
        }

        return findCombinations(0, target)
    }
}