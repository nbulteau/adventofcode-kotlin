package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.*

// https://adventofcode.com/2018/day/1
fun main() {

    val data = readFileDirectlyAsText("/year2018/day01/data.txt")
    val lines = data.split("\n")
    val day = Day01("--- Day 1: Chronal Calibration ---", "https://adventofcode.com/2018/day/1")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day01(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(lines: List<String>): Int {

        return lines.fold(0) { acc, line ->
            acc + line.trim().toInt()
        }
    }

    fun partTwo(lines: List<String>): Int {
        val set = mutableSetOf<Int>()

        val circularList: CircularList<Int> = CircularList(lines.map { it.trim().toInt() })
        var index = 0
        var acc = 0
        do {
            acc += circularList[index]
            if (set.contains(acc)) {
                return acc
            } else {
                set.add(acc)
            }
            index++
        } while (true)
    }
}

