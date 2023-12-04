package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// https://adventofcode.com/2018/day/3
fun main() {
    val data = readFileDirectlyAsText("/year2018/day03/data.txt")
    val lines = data.split("\n")
    val day = Day03("--- Day 3: No Matter How You Slice It ---", "https://adventofcode.com/2018/day/3")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day03(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    private data class Claim(val id: Int, val left: Int, val top: Int, val width: Int, val height: Int) {
        val area: List<Pair<Int, Int>>
            get() = (left until width + left).flatMap { w ->
                (top until height + top).map { h ->
                    w to h
                }
            }
    }

    fun partOne(lines: List<String>): Int {
        val claims = buildClaims(lines)

        return claims
            // flatMap returns a single list of all elements yielded from results of claim.area
            .flatMap { claim -> claim.area }
            // groupingBy returns a Grouping source where each key is the result of the given function
            .groupingBy { it }
            // eachCount returns a Map where each key is an element from the Grouping source and its corresponding value is the number of elements in the group.
            .eachCount()
            // count the number of values that are greater than 1
            .count { it.value > 1 }
    }

    fun partTwo(lines: List<String>): Int {
        val claims = buildClaims(lines)

        val areaOfFabric = claims
            // flatMap returns a single list of all elements yielded from results of claim.area
            .flatMap { claim -> claim.area }
            // groupingBy returns a Grouping source where each key is the result of the given function
            .groupingBy { it }
            // eachCount returns a Map where each key is an element from the Grouping source and its corresponding value is the number of elements in the group.
            .eachCount()

        claims.forEach { claim ->
            // all returns true if all spots of claim's area are equal to 1
            if (claim.area.all { spot -> areaOfFabric[spot] == 1 }) {
                return claim.id
            }
        }

        return 0
    }

    private fun buildClaims(lines: List<String>): List<Claim> {
        return lines.map { line ->

            val (id, left, top, width, height) = line.trim()
                .replace("#", "")
                .replace(" @", "")
                .replace(",", " ")
                .replace(":", "")
                .replace("x", " ")
                .split(" ")
                .map { it.toInt() }

            Claim(id, left, top, width, height)
        }
    }

}