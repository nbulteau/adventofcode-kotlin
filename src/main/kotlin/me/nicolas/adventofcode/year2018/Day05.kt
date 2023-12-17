package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2018/day/5
fun main() {
    val data = readFileDirectlyAsText("/year2018/day05/data.txt")
    val day = Day05(2018, 5, "Alchemical Reduction")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day05(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val polymer = data.trimIndent()

        return reducePolymer(polymer).length
    }

    fun partTwo(data: String): Int {
        val polymer = data.trimIndent()

        // all lowercase letters
        return ('a'..'z').map { lowercase ->
            polymer.replace(lowercase.toString(), "", true)
        }.minOf { reducePolymer(it).length }
    }

    private fun Char.doReact(otherChar: Char) = this.lowercaseChar() == otherChar.lowercaseChar() && this != otherChar

    private fun reducePolymer(polymer: String): String {
        var reducedPolymer = polymer
        var index = 0
        while (index < reducedPolymer.length - 1) {
            if (reducedPolymer[index].doReact(reducedPolymer[index + 1])) {
                reducedPolymer = reducedPolymer.removeRange(index, index + 2)
                index = if (index > 0) index - 1 else 0
            } else {
                index++
            }
        }

        return reducedPolymer
    }
}