package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 13: Shuttle Search ---
// https://adventofcode.com/2020/day/13
fun main() {
    val data = readFileDirectlyAsText("/year2020/day13/data.txt")
    val day = Day13(2020, 13, "Shuttle Search")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day13(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val (timestampStr, bus) = data.split("\n")
        val initialTimestamp = timestampStr.toInt()
        val busIdsList = bus.split(",").filter { it != "x" }.map { it.toInt() }

        var timestamp = initialTimestamp
        while (true) {
            val busId = busIdsList.find { timestamp % it == 0 }
            if (busId != null) {
                return (timestamp - initialTimestamp) * busId
            }
            timestamp++
        }
    }

    fun partTwo(data: String): Long {
        val busIds = data.split("\n")[1].split(",")
        val buses = mutableMapOf<Int, Int>()
        for (index in busIds.indices) {
            if (busIds[index] != "x") {
                val value = busIds[index].toInt()
                buses[value] = index
            }
        }

        var result: Long = 0
        var incrementer: Long = 1

        for (busId in buses.keys) {
            while ((result + buses[busId]!!) % busId != 0L) {
                result += incrementer
            }
            incrementer *= busId.toLong()
        }
        return result
    }
}