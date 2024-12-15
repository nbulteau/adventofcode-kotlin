package me.nicolas.adventofcode.year2015

import kotlinx.serialization.json.*
import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 12: ---
// https://adventofcode.com/2015/day/12
fun main() {
    val data = readFileDirectlyAsText("/year2015/day12/data.txt")
    val day = Day12(2015, 12)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day12(year: Int, day: Int, title: String = "JSAbacusFramework.io") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String) = Regex("-?\\d+").findAll(data).map { it.value.toInt() }.sum()

    fun partTwo(data: String) = Json.parseToJsonElement(data).sumNumbersExcludingRed()

    private fun JsonElement.sumNumbersExcludingRed(): Int {
        return when (this) {
            is JsonPrimitive -> if (this.isString) 0 else this.int
            is JsonObject -> {
                if (this.values.any { it is JsonPrimitive && it.content == "red" }) {
                    return 0
                }
                return this.values.sumOf { jsonElement -> jsonElement.sumNumbersExcludingRed() }
            }

            is JsonArray -> {
                return this.sumOf { jsonElement -> jsonElement.sumNumbersExcludingRed() }
            }
        }
    }
}