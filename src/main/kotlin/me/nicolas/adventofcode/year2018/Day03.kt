package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// https://adventofcode.com/2018/day/3
fun main() {
    val data = readFileDirectlyAsText("/year2018/day03/data.txt")
    val day = Day03(2018, 3, "No Matter How You Slice It")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day03(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private data class Claim(val id: Int, val left: Int, val top: Int, val width: Int, val height: Int) {
        val area: List<Pair<Int, Int>>
            get() = (left until width + left).flatMap { w ->
                (top until height + top).map { h ->
                    w to h
                }
            }
    }

    fun partOne(data: String): Int {
        val claims = buildClaims(data)

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

    fun partTwo(data: String): Int {
        val claims = buildClaims(data)

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

    private fun buildClaims(data: String): List<Claim> {
        val lines = data.split("\n")

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