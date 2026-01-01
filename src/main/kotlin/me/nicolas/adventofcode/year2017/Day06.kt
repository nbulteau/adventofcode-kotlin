package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 6: Memory Reallocation ---
// https://adventofcode.com/2017/day/6
fun main() {
    val data = readFileDirectlyAsText("/year2017/day06/data.txt")
    val day = Day06()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day06(year: Int = 2017, day: Int = 6, title: String = "Memory Reallocation") : AdventOfCodeDay(year, day, title) {

    /**
     * partOne finds the number of redistribution cycles before a configuration is repeated.
     */
    fun partOne(data: String): Int {
        val banks = data.split("\t", " ").map { it.trim().toInt() }.toMutableList()
        var cycle = 0

        // Count cycles until a configuration is repeated
        val seenConfigurations = mutableSetOf<List<Int>>()

        while (banks.toList() !in seenConfigurations) {
            seenConfigurations.add(banks.toList())
            cycle += 1
            // Find the bank with the most blocks
            val maxBlocks = banks.maxOrNull() ?: 0
            val index = banks.indexOfFirst { it == maxBlocks }
            banks[index] = 0
            var blocksToDistribute = maxBlocks
            var currentIndex = (index + 1) % banks.size
            // Distribute one block at a time
            while (blocksToDistribute > 0) {
                banks[currentIndex] += 1
                blocksToDistribute -= 1
                currentIndex = (currentIndex + 1) % banks.size
            }
        }

        return cycle
    }

    /**
     * partTwo computes the size of the loop in the reallocation process.
     */
    fun partTwo(data: String): Int {
        val banks = data.split("\t", " ").map { it.trim().toInt() }.toMutableList()

        // Map each seen configuration to the cycle number when it was first seen.
        // We start counting cycles from 0: the initial configuration is recorded as cycle 0.
        // For each redistribution we increment cycle by 1. When we encounter a configuration
        // that was seen before, the size of the loop is currentCycle - firstSeenCycle.
        val seenAtCycle = mutableMapOf<List<Int>, Int>()
        var cycle = 0

        while (banks.toList() !in seenAtCycle) {
            seenAtCycle[banks.toList()] = cycle
            // Perform redistribution (same logic as in partOne)
            val maxBlocks = banks.maxOrNull() ?: 0
            val index = banks.indexOfFirst { it == maxBlocks }
            banks[index] = 0
            var blocksToDistribute = maxBlocks
            var currentIndex = (index + 1) % banks.size
            while (blocksToDistribute > 0) {
                banks[currentIndex] += 1
                blocksToDistribute -= 1
                currentIndex = (currentIndex + 1) % banks.size
            }
            cycle += 1
        }

        val firstSeen = seenAtCycle[banks.toList()] ?: 0

        return cycle - firstSeen
    }
}