package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 16: Ticket Translation ---
// https://adventofcode.com/2020/day/16
fun main() {
    val data = readFileDirectlyAsText("/year2020/day16/data.txt")
    val day = Day16(2020, 16, "Ticket Translation")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day16(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val (rules, _, nearbyTickets) = parseData(data)
        return nearbyTickets
            .flatten()
            .filter { value ->
                rules.values.flatten().none { range -> value in range }
            }
            .sum()
    }

    fun partTwo(data: String): Long {
        val (rules, myTicket, nearbyTickets) = parseData(data)
        val allRanges = rules.values.flatten()
        val validTickets = nearbyTickets.filter { ticket ->
            ticket.all { value -> allRanges.any { range -> value in range } }
        }

        val possibleFields = rules.mapValues { (_, ranges) ->
            (0 until myTicket.size).filter { index ->
                validTickets.all { ticket ->
                    ranges.any { range -> ticket[index] in range }
                }
            }.toMutableSet()
        }

        val fieldMapping = mutableMapOf<String, Int>()
        while (possibleFields.values.any { it.isNotEmpty() }) {
            val (field, index) = possibleFields.entries.first { it.value.size == 1 }
            val determinedIndex = index.first()
            fieldMapping[field] = determinedIndex
            possibleFields.values.forEach { it.remove(determinedIndex) }
        }

        return fieldMapping
            .filterKeys { it.startsWith("departure") }
            .values
            .map { myTicket[it].toLong() }
            .reduce { acc, i -> acc * i }
    }

    private fun parseData(data: String): Triple<Map<String, List<IntRange>>, List<Int>, List<List<Int>>> {
        val sections = data.split("\n\n")
        val rules = extractRules(sections)
        val myTicket = extractTicket(sections)
        val nearbyTickets = extractNearbyTickets(sections)
        return Triple(rules, myTicket, nearbyTickets)
    }

    private fun extractRules(sections: List<String>): Map<String, List<IntRange>> {
        return sections[0].split("\n").filter { it.isNotEmpty() }.associate { line ->
            val name = line.substringBefore(":")
            val ranges = line.substringAfter(": ").split(" or ").map {
                val (start, end) = it.split("-").map { str -> str.toInt() }
                start..end
            }
            name to ranges
        }
    }

    private fun extractTicket(sections: List<String>): List<Int> {
        return sections[1].substringAfter("your ticket:\n").split(",").map { it.toInt() }
    }

    private fun extractNearbyTickets(sections: List<String>): List<List<Int>> {
        return sections[2].substringAfter("nearby tickets:\n").split("\n").filter { it.isNotEmpty() }.map { line ->
            line.split(",").map { it.toInt() }
        }
    }
}
