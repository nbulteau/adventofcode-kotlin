package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.*

// https://adventofcode.com/2021/day/8
fun main() {
    val data = readFileDirectlyAsText("/year2021/day08/data.txt")
    val day = Day08(2021, 8)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day08(year: Int, day: Int, title: String = "Seven Segment Search") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val lines = data.split("\n")

        return lines.sumOf { line ->
            val fourDigitOutputValue = line.split(" | ")[1]
            fourDigitOutputValue.split(" ")
                .filter { it.length in listOf(2, 3, 4, 7) }.size
        }
    }

    fun partTwo(data: String): Int {
        val lines = data.split("\n")

        return lines.sumOf { line ->
            val uniqueSignalPatterns = line.split(" | ")[0]
            val fourDigitOutputValue = line.split(" | ")[1]
            val signalPatterns = processMap(uniqueSignalPatterns.split(" "))

            fourDigitOutputValue.split(" ")
                .map { str -> signalPatterns[str.toSortedSet()] }
                .joinToString("")
                .toInt()
        }
    }

    private fun processMap(uniqueSignalPatterns: List<String>): Map<SortedSet<Char>, String> {
        val signalPatterns: MutableMap<SortedSet<Char>, String> = LinkedHashMap(10)
        var one: SortedSet<Char> = sortedSetOf()
        // guess 1, 4, 7, 8
        uniqueSignalPatterns.forEach { str ->
            val key = str.toSortedSet()
            when (str.length) {
                2 -> {
                    signalPatterns[key] = "1"
                    one = key
                }

                3 -> signalPatterns[key] = "7"
                4 -> signalPatterns[key] = "4"
                7 -> signalPatterns[key] = "8"
            }
        }
        // guess 3, 6
        var three: SortedSet<Char> = sortedSetOf()
        var six: SortedSet<Char> = sortedSetOf()
        uniqueSignalPatterns
            .filter { !signalPatterns.contains(it.toSortedSet()) }
            .forEach { str ->
                val key = str.toSortedSet()
                when {
                    (key.size == 5 && key.containsAll(one)) -> {
                        signalPatterns[key] = "3"
                        three = key
                    }

                    (key.size == 6 && !key.containsAll(one)) -> {
                        signalPatterns[key] = "6"
                        six = key
                    }
                }
            }
        // guess 0, 9, 5, 2
        uniqueSignalPatterns
            .filter { !signalPatterns.contains(it.toSortedSet()) }
            .forEach { str ->
                val key = str.toSortedSet()
                when (key.size) {
                    6 -> {
                        if (key.containsAll(three)) {
                            signalPatterns[key] = "9"
                        } else {
                            signalPatterns[key] = "0"
                        }
                    }

                    5 -> {
                        if (six.containsAll(key)) {
                            signalPatterns[key] = "5"
                        } else {
                            signalPatterns[key] = "2"
                        }
                    }
                }
            }
        return signalPatterns
    }
}