package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day03/data.txt")
    val lines = data.split("\n")
    val day = Day03("--- Day 3: Gear Ratios ---", "https://adventofcode.com/2023/day/3")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day03(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    private fun Char.isSymbol(): Boolean = this != '.' && !isWhitespace() && !isDigit()

    private val numberRegex = """\d+""".toRegex()

    fun partOne(lines: List<String>): Int {
        val map: Map<Triple<Int, Int, Char>, List<Int>> = buildMap(lines)

        return map.values.flatten().sum()
    }

    fun partTwo(lines: List<String>): Int {
        val map: Map<Triple<Int, Int, Char>, List<Int>> = buildMap(lines)

        // A gear is any * symbol that is adjacent to exactly two part numbers.
        return map.filter { (key, _) -> key.third == '*' }
            .values.filter { list -> list.size == 2 }
            .sumOf { list -> list.first() * list.last() }
    }

    private fun buildMap(lines: List<String>): Map<Triple<Int, Int, Char>, List<Int>> {
        // map<key, values>
        // key: x, y, symbol
        // value: list of numbers sticking to the symbol
        val map = mutableMapOf<Triple<Int, Int, Char>, MutableList<Int>>()

        // for each line find numbers
        for ((y, line) in lines.withIndex()) {
            // find numbers in line
            numberRegex.findAll(line).forEach { matchResult ->
                // get number value
                val number = matchResult.value.toInt()
                // look line before and after for symbol
                for (index in (y - 1).coerceAtLeast(0)..(y + 1).coerceAtMost(lines.lastIndex)) {
                    val currentLine = lines[index]
                    val xStart = matchResult.range.first - 1
                    val xEnd = matchResult.range.last + 1
                    // check if symbol is present
                    for (x in xStart.coerceAtLeast(0)..xEnd.coerceAtMost(currentLine.lastIndex)) {
                        if (currentLine[x].isSymbol()) {
                            map.getOrPut(key = Triple(x, index, currentLine[x])) { mutableListOf() } += number
                        }
                    }
                }
            }
        }

        return map
    }
}

