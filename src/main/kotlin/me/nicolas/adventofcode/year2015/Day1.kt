package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// --- Day 1: Not Quite Lisp ---
// https://adventofcode.com/2015/day/1
fun main() {
    val data = readFileDirectlyAsText("/year2015/day01/data.txt")
    val day = Day01(2015, 1, "Not Quite Lisp")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day01(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(directions: String): Int {
        return directions.count { char -> char == '(' } - directions.count { char -> char == ')' }
    }

    fun partTwo(directions: String): Int {
        var floor = 0
        var index = 0
        do {
            when (directions[index]) {
                '(' -> floor += 1
                ')' -> floor -= 1
            }
            index++
        } while (floor != -1 && index < directions.length)

       return index
    }
}
