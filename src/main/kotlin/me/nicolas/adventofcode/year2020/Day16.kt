package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 16: Ticket Translation ---
// https://adventofcode.com/2020/day/16
@ExperimentalTime
fun main() {

    println("--- Day 16: Ticket Translation ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day16/training.txt")
    val data = readFileDirectlyAsText("/year2020/day16/data.txt")

    val sections = data.split("\n\n")
    val rules = extractRules(sections)
    val ticket = extractTicket(sections)
    val nearbyTickets = extractNearbyTickets(sections)

    // Part One
    val invalidNumbers = Day16().partOne(rules, nearbyTickets)

    // Part Two
    // remove invalid tickets
    val remainingValidTickets = nearbyTickets
        .filter { numbers -> numbers.map { it in invalidNumbers }.all { !it } }

    val duration = measureTime { Day16().partTwo(ticket, rules, remainingValidTickets) }
    println("Part two duration : $duration")
}

private fun extractNearbyTickets(sections: List<String>): List<List<Int>> {
    return sections[2]
        .substringAfter("nearby tickets:\n")
        .split("\n")
        .map { line -> line.split(",").map { it.toInt() } }
}

private fun extractTicket(sections: List<String>): List<Int> {
    return sections[1]
        .substringAfter("your ticket:\n")
        .split(",")
        .map { it.toInt() }
}

private fun extractRules(sections: List<String>): Map<String, List<IntRange>> {
    return sections[0]
        .split("\n")
        .map { line ->
            line.substringBefore(":") to line.substringAfter(":").split(" or ").map { str ->
                IntRange(
                    str.substringBefore("-").trim().toInt(),
                    str.substringAfter("-").trim().toInt()
                )
            }
        }.toMap()
}

class Day16 {

    fun partOne(rules: Map<String, List<IntRange>>, tickets: List<List<Int>>): List<Int> {

        val invalidNumbers = tickets
            .flatMap { ticket ->
                ticket
                    .filter { value ->
                        rules.values
                            .map { ranges -> value in ranges[0] || value in ranges[1] }
                            .all { !it }
                    }
            }

        println("Part one = ${invalidNumbers.sum()}")

        return invalidNumbers
    }

    fun partTwo(ticket: List<Int>, rules: Map<String, List<IntRange>>, tickets: List<List<Int>>) {

        val validRows = rules
            .map { rule -> rule.key to getAllPossibleIndexForARule(rule, tickets) }
            .toMap()

        val dictionary = mutableMapOf<String, Int>()

        recursiveSolve(validRows, dictionary)

        // display dictionary
        // dictionary.forEach { (key, values) -> println("$key -> $values") }

        // look for the six fields on your ticket that start with the word departure
        val result: Long = dictionary
            .filter { item -> item.key.startsWith("departure") }.values
            .map { index -> ticket[index].toLong() }
            .reduce { acc: Long, i -> acc * i }

        println("Part two = $result")
    }

    private fun recursiveSolve(validRows: Map<String, List<Boolean>>, dictionary: MutableMap<String, Int>) {

        val unique = validRows
            .filterValues { list -> list.count { it } == 1 }
            .map { it.key to it.value.indexOf(true) }
            .toMap()
            .toMutableMap()

        unique.forEach { (key, value) ->
            dictionary[key] = value

            val newValidRows: Map<String, MutableList<Boolean>> = validRows
                .minus(key)
                .map { it.key to it.value.toMutableList().apply { set(value, false) } }
                .toMap()

            recursiveSolve(newValidRows, dictionary)
        }
    }

    private fun getAllPossibleIndexForARule(
        rule: Map.Entry<String, List<IntRange>>,
        tickets: List<List<Int>>
    ): List<Boolean> {

        // row, column -> column, row
        val entriesByIndex: List<MutableList<Int>> = tickets[0].map { mutableListOf() }
        tickets[0].forEachIndexed { index, _ ->
            tickets.forEach { ticket ->
                entriesByIndex[index].add(ticket[index])
            }
        }

        return entriesByIndex.map { list ->
            list.map { value -> value in rule.value[0] || value in rule.value[1] }
                .all { it }
        }
    }
}


