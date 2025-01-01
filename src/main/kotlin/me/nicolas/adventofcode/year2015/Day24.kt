package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 24: It Hangs in the Balance ---
// https://adventofcode.com/2015/day/24
fun main() {
    val data = readFileDirectlyAsText("/year2015/day24/data.txt")
    val day = Day24(2015, 24)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day24(year: Int, day: Int, title: String = "It Hangs in the Balance") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Long {
        val weights = data.lines().map { line -> line.toInt() }
        val targetWeight = weights.sum() / 3

        return solve(weights, targetWeight)
    }

    fun partTwo(data: String): Long {
        val weights = data.lines().map { line -> line.toInt() }
        val targetWeight = weights.sum() / 4

        return solve(weights, targetWeight)

    }

    private fun solve(weights: List<Int>, targetWeight: Int): Long {
        val validCombinations = findValidCombinations(weights, targetWeight)
        val minPackages = validCombinations.minOf { combinations -> combinations.size }
        val optimalCombinations = validCombinations.filter { combinations -> combinations.size == minPackages }

        return optimalCombinations.minOf { combinations -> combinations.fold(1L) { acc, weight -> acc * weight } }
    }

    private fun findValidCombinations(weights: List<Int>, targetWeight: Int): List<List<Int>> {
        val combinations = mutableListOf<List<Int>>()
        generateCombinations(weights, targetWeight, 0, mutableListOf(), combinations)

        return combinations
    }

    /**
     * Recursively generates combinations using backtracking.
     * Early Termination: Stops recursion if the current sum exceeds the target weight.
     * Combination Storage: Adds valid combinations to the result list.
     */
    private fun generateCombinations(
        weights: List<Int>,
        targetWeight: Int,
        start: Int,
        currentCombination: MutableList<Int>,
        combinations: MutableList<List<Int>>,
    ) {
        val currentSum = currentCombination.sum()
        if (currentSum == targetWeight) {
            combinations.add(ArrayList(currentCombination))

            return
        }
        if (currentSum > targetWeight) {
            return
        }
        for (i in start until weights.size) {
            currentCombination.add(weights[i])
            generateCombinations(weights, targetWeight, i + 1, currentCombination, combinations)
            currentCombination.removeAt(currentCombination.size - 1)
        }
    }
}