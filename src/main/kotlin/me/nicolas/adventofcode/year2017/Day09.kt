package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 9: Stream Processing ---
// https://adventofcode.com/2017/day/9
fun main() {
    val data = readFileDirectlyAsText("/year2017/day09/data.txt")
    val day = Day09()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day09(year: Int = 2017, day: Int = 9, title: String = "Stream Processing") : AdventOfCodeDay(year, day, title) {

    /**
     * partOne computes the total score of all groups in the input data.
     * Groups are defined by curly braces {}, and their score is determined
     * by their depth in the nested structure. Garbage sections, defined
     * by angle brackets <>, are ignored for scoring purposes.
     * The function also handles escape characters '!' which negate the next character.
     */
    fun partOne(data: String): Int {
        var totalScore = 0
        var depth = 0
        var inGarbage = false
        var ignoreNext = false

        for (ch in data) {
            if (ignoreNext) {
                ignoreNext = false
                continue
            }

            if (inGarbage) {
                when (ch) {
                    '!' -> ignoreNext = true
                    '>' -> inGarbage = false
                    else -> {
                        // inside garbage, other characters are ignored for part one
                    }
                }
            } else {
                when (ch) {
                    '<' -> inGarbage = true
                    '{' -> depth++
                    '}' -> {
                        if (depth > 0) {
                            totalScore += depth
                            depth--
                        }
                    }
                    else -> {
                        // commas and other chars outside garbage are irrelevant
                    }
                }
            }
        }

        return totalScore
    }

    /**
     * partTwo counts the total number of non-canceled characters within
     * garbage sections in the input data. Garbage sections are defined
     * by angle brackets <>, and characters can be escaped using '!' which
     * negates the next character.
     */
    fun partTwo(data: String): Int {
        var garbageCount = 0
        var inGarbage = false
        var ignoreNext = false

        for (ch in data) {
            if (ignoreNext) {
                ignoreNext = false
                continue
            }

            if (inGarbage) {
                when (ch) {
                    '!' -> ignoreNext = true
                    '>' -> inGarbage = false
                    else -> garbageCount++
                }
            } else {
                if (ch == '<') inGarbage = true
            }
        }

        return garbageCount
    }
}