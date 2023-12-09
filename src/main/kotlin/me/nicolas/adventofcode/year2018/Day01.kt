package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.*

// https://adventofcode.com/2018/day/1
fun main() {

    val data = readFileDirectlyAsText("/year2018/day01/data.txt")
    val day = Day01(2018, 1, "Chronal Calibration")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day01(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val lines = data.split("\n")

        return lines.fold(0) { acc, line ->
            acc + line.trim().toInt()
        }
    }

    fun partTwo(data: String): Int {
        val lines = data.split("\n")
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

