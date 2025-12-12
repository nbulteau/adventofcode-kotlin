package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 06: Custom Customs ---
// https://adventofcode.com/2020/day/6
fun main() {
    val data = readFileDirectlyAsText("/year2020/day06/data.txt")
    val day = Day06(2020, 6, "Custom Customs")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day06(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val answers = data.split("\n\n")
        return answers.sumOf { group ->
            group.replace("\n", "").toSet().size
        }
    }

    fun partTwo(data: String): Int {
        val answers = data.split("\n\n")
        return answers.sumOf { group ->
            val persons = group.split("\n").filter { it.isNotEmpty() }
            persons.first().count { char ->
                persons.all { person -> char in person }
            }
        }
    }
}
