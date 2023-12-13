package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.lang.String.join


fun main() {
    val data = readFileDirectlyAsText("/year2023/day12/data.txt")
    val day = Day12(2023, 12, "Hot Springs")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day12(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Long {
        val rows = parseData(data)
       return rows.sumOf { (springs, groupsOfDamagedSprings) ->
            count(springs, groupsOfDamagedSprings)
        }
    }

    fun partTwo(data: String): Long {
        val rows = parseData(data)
        return rows.sumOf { (springs, groupsOfDamagedSprings) ->
            val unfoldedSprings =   join("?", springs, springs, springs, springs, springs)
            val unfoldedGroupsOfDamagedSprings = groupsOfDamagedSprings + groupsOfDamagedSprings + groupsOfDamagedSprings + groupsOfDamagedSprings + groupsOfDamagedSprings

            count(unfoldedSprings, unfoldedGroupsOfDamagedSprings)
        }
    }

    // Cache for memoization
    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()

    // Recursive count function with memoization
    private fun count(springs: String, groupsOfDamagedSprings: List<Int>): Long {

        // stops conditions
        if (groupsOfDamagedSprings.isEmpty()) {
            return if ("#" in springs) 0 else 1
        }
        if (springs.isEmpty()) {
            return 0
        }

        // dynamic programming with memoization
        return cache.getOrPut(springs to groupsOfDamagedSprings) {
            var result = 0L

            // springs starts with . or ?
            // then we can remove the first char and recurse
            if (springs.first() in ".?") {
                result += count(springs.drop(1), groupsOfDamagedSprings)
            }
            // springs starts with # or ?
            // and the first group of damaged springs is not longer than the length of springs
            // and there is no . before the first group of damaged springs
            // and the first group of damaged springs is the last group of damaged springs or the next char is not #
            // then we can remove the first group of damaged springs and recurse
            if (springs.first() in "#?" && groupsOfDamagedSprings.first() <= springs.length && "." !in springs.take(groupsOfDamagedSprings.first()) && (groupsOfDamagedSprings.first() == springs.length || springs[groupsOfDamagedSprings.first()] != '#')) {
                result += count(springs.drop(groupsOfDamagedSprings.first() + 1), groupsOfDamagedSprings.drop(1))
            }

            result
        }
    }

    private fun parseData(data: String): List<Pair<String, List<Int>>> {
        return data.split('\n').map { row ->
            val springs = row.split(' ').first()
            val groupsOfDamagedSprings = row.split(' ').last().split(',').map { it.toInt() }
            springs to groupsOfDamagedSprings
        }
    }
}

