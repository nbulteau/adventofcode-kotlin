package me.nicolas.adventofcode.year2019


// --- Day 4: Secure Container ---
// https://adventofcode.com/2019/day/4
fun main() {

    println("--- Day 4: Secure Container ---")
    println()

    val data = "264360-746325".split("-")

    val from = data[0].toInt()
    val to = data[1].toInt()

    // Part One
    Day04().partOne(from..to)

    // Part Two
    Day04().partTwo(from..to)
}

class Day04 {

    fun partOne(range: IntRange) {

        val result = range.map { index -> index.toString() }
            .filter { isIncreaseOrStayTheSame(it) }
            .filter { hasTwoAdjacentDigitsAreTheSame(it) }
            .count()

        println("Part one $result")
    }

    fun partTwo(range: IntRange) {

        val result = range.map { index -> index.toString() }
            .filter { isIncreaseOrStayTheSame(it) }
            .filter { hasAOnlyTwoAdjacentDigitsAreTheSame(it) }
            .count()

        println("Part two $result")
    }

    fun isIncreaseOrStayTheSame(input: String): Boolean =
        input.zipWithNext().all { it.first <= it.second }

    fun hasTwoAdjacentDigitsAreTheSame(input: String): Boolean =
        input.zipWithNext().any { it.first == it.second }

    fun hasAOnlyTwoAdjacentDigitsAreTheSame(input: String): Boolean =
        input.groupBy { char -> char }.any { it.value.size == 2 }

}


