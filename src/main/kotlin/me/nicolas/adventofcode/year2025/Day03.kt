package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 3: Lobby ---
// https://adventofcode.com/2025/day/3
fun main() {
    val data = readFileDirectlyAsText("/year2025/day03/data.txt")
    val day = Day03(2025, 3)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartOne { day.partOneBis(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day03(year: Int, day: Int, title: String = "Lobby") : AdventOfCodeDay(year, day, title) {

    /**
     * Find the max joltage from each battery bank by selecting exactly 2 batteries.
     *
     * We try all possible pairs and concatenate their digits to form a number.
     * For example, in "987654321111111", picking batteries at positions 0 and 1 gives us 98.
     * We sum up the maximum joltage from each bank.
     */
    fun partOne(data: String): Long {
        return data.lines()
            .filter { bank -> bank.isNotBlank() }
            .sumOf { bank -> findMaxJoltage(bank) }
    }

    /**
     * Alternative implementation of partOne using the generic n-batteries approach.
     * Finds the max joltage from each battery bank by selecting exactly 2 batteries.
     *
     * This version uses the stack-based algorithm (findMaxJoltageWithNBatteries)
     * instead of brute force enumeration, which is more efficient.
     */
    fun partOneBis(data: String): Long {
        return data.lines()
            .filter { bank -> bank.isNotBlank() }
            .sumOf { bank -> findMaxJoltageWithNBatteries(bank, 2) }
    }

    /**
     * Same goal but now we need to select exactly 12 batteries from each bank.
     *
     * Since each bank has 15 batteries, we need to skip 3 of them.
     * The trick is to remove the smallest digits that hurt our number the least.
     * We use a stack to greedily keep the largest digits in the leftmost positions,
     * which gives us the biggest possible number.
     *
     * For "987654321111111", we remove three 1s from the end to get "987654321111".
     */
    fun partTwo(data: String): Long {
        return data.lines()
            .filter { bank -> bank.isNotBlank() }
            .sumOf { bank -> findMaxJoltageWithNBatteries(bank, 12) }
    }


    private fun findMaxJoltage(bank: String): Long {
        var maxJoltage = 0L

        for (i in bank.indices) {
            for (j in i + 1 until bank.length) {
                val joltage = "${bank[i]}${bank[j]}".toLong()
                maxJoltage = maxOf(maxJoltage, joltage)
            }
        }

        return maxJoltage
    }

    /**
     * Finds the maximum joltage by selecting n batteries from the bank.
     * Uses a greedy stack-based approach to maximize the resulting number
     * by keeping the largest digits in the leftmost positions.
     */
    private fun findMaxJoltageWithNBatteries(bank: String, n: Int): Long {
        val toRemove = bank.length - n
        val stack = mutableListOf<Char>()
        var removedCount = 0

        for (digit in bank) {
            while (stack.isNotEmpty() && removedCount < toRemove && stack.last() < digit) {
                stack.removeLast()
                removedCount++
            }
            stack.add(digit)
        }

        return stack.dropLast(toRemove - removedCount)
            .joinToString("")
            .toLong()
    }
}