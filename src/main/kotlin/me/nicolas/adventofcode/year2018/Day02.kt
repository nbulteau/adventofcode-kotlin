package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2018/day/2
fun main() {

    val data = readFileDirectlyAsText("/year2018/day02/data.txt")
    val day = Day02(2018, 2, "Inventory Management System")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day02(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val lines = data.split("\n")

        // counts is  a map where each key is a key from the source and its corresponding value is the number of elements in the group.
        val counts = lines.map { line ->
            // Groups elements from the Grouping source by key and counts elements in each group.
            line.groupingBy { char -> char }
                .eachCount().values
        }

        return counts.count { it.contains(2) } * counts.count { it.contains(3) }
    }

    fun partTwo(data: String): String {
        val lines = data.split("\n")

        // -1 because last doesn't need to be compared to anything else
        for (i in 0 until lines.size - 1) {
            for (j in i + 1 until lines.size) {
                val line1 = lines[i]
                val line2 = lines[j]
                // zip returns a list of pairs built from elements of both collections with same indexes.
                val diff = line1.zip(line2).count { pair -> pair.first != pair.second }
                // If the two lines differ by exactly one character
                if (diff == 1) {
                    return line1.zip(line2)
                        // filter returns a list of pairs built from elements of both collections with same indexes.
                        .filter { pair -> pair.first == pair.second }
                        // map returns a list of the first element of each pair.
                        .map { pair -> pair.first }
                        .joinToString("")
                }
            }
        }

        return ""
    }
}


