package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 25: Code Chronicle ---
// https://adventofcode.com/2024/day/25
fun main() {
    val data = readFileDirectlyAsText("/year2024/day25/data.txt")
    val day = Day25(2024, 25)
    prettyPrintPartOne { day.partOne(data) }
}


class Day25(year: Int, day: Int, title: String = "Code Chronicle") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val (locks, keys) = parseSchematics(data)

        var fittingPairsCount = 0

        for (lock in locks) {
            for (key in keys) {
                if (fitsTogether(lock, key)) {
                    fittingPairsCount++
                }
            }
        }

        return fittingPairsCount
    }

    private fun parseSchematics(data: String): Pair<List<List<Int>>, List<List<Int>>> {
        val schematics = data.replace("\r\n", "\n").replace("\r", "\n").trim().split("\n\n")
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()

        for (schematic in schematics) {
            val lines = schematic.lines()
            if (lines.first()[0] == '#') {
                // This is a lock
                locks.add(convertToHeights(lines, true))
            } else {
                // This is a key
                keys.add(convertToHeights(lines, false))
            }
        }

        return locks to keys
    }

    private fun convertToHeights(lines: List<String>, isLock: Boolean): List<Int> {
        val heights = MutableList(lines[0].length) { 0 }

        if (isLock) {
            // For locks, count the number of '#' from the top down
            for (line in lines) {
                for (i in line.indices) {
                    if (line[i] == '#') {
                        heights[i]++
                    }
                }
            }
        } else {
            // For keys, count the number of '#' from the bottom up
            for (i in lines[0].indices) {
                for (index in lines.size - 1 downTo 0) {
                    if (lines[index][i] == '#') {
                        heights[i]++
                    }
                }
            }
        }

        return heights
    }

    private fun fitsTogether(lock: List<Int>, key: List<Int>): Boolean {
        for ((lockHeight, keyHeight) in lock.zip(key)) {
            if (lockHeight + keyHeight > 7) { // 7 is the maximum height
                return false
            }
        }
        return true
    }
}