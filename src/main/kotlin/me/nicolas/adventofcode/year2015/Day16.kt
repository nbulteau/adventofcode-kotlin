package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 16: Aunt Sue ---
// https://adventofcode.com/2015/day/16
fun main() {
    val data = readFileDirectlyAsText("/year2015/day16/data.txt")
    val day = Day16(2015, 16)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day16(year: Int, day: Int, title: String = "Aunt Sue") : AdventOfCodeDay(year, day, title) {

    private data class Aunt(val number: Int, val attributes: Map<String, Int>)

    private val tickerTape = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1
    )

    fun partOne(data: String): Int {
        val aunts = data.lines().map { line -> line.parseAunt() }
        return aunts.first { matchesTickerTape(it) }.number
    }

    fun partTwo(data: String): Int {
        val aunts = data.lines().map { line -> line.parseAunt() }
        return aunts.first { matchesTickerTapeWithRanges(it) }.number
    }

    private fun String.parseAunt(): Aunt {
        val parts = this.split(": ", ", ", " ")
        val number = parts[1].toInt()
        val attributes = parts.drop(2).chunked(2).associate { strings -> strings[0] to strings[1].toInt() }

        return Aunt(number, attributes)
    }

    private fun matchesTickerTape(aunt: Aunt): Boolean {
        return aunt.attributes.all { (key, value) -> tickerTape[key] == value }
    }

    private fun matchesTickerTapeWithRanges(aunt: Aunt): Boolean {
        return aunt.attributes.all { (key, value) ->
            when (key) {
                "cats", "trees" -> value > tickerTape[key]!!
                "pomeranians", "goldfish" -> value < tickerTape[key]!!
                else -> tickerTape[key] == value
            }
        }
    }
}