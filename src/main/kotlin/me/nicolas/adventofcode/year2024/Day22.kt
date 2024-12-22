package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2024/day22/data.txt")
    val day = Day22(2024, 22)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day22(year: Int, day: Int, title: String = "Monkey Market") : AdventOfCodeDay(year, day, title) {

    fun generateSecretNumbers(start: Long, nbPasswords: Int): List<Long> {
        val secretNumbers = mutableListOf<Long>()
        var current = start
        repeat(nbPasswords) {
            secretNumbers.add(current)
            current = nextSecretNumber(current)
        }
        return secretNumbers
    }

    private val memo = mutableMapOf<Long, Long>()

    private fun nextSecretNumber(secret: Long): Long {
        return memo.computeIfAbsent(secret) { current ->
            var nextSecret = current
            nextSecret = (nextSecret xor (nextSecret * 64)) % 16777216
            nextSecret = (nextSecret xor (nextSecret / 32)) % 16777216
            nextSecret = (nextSecret xor (nextSecret * 2048)) % 16777216
            nextSecret
        }
    }

    fun partOne(data: String, nbPasswords: Int = 2001): Long {
        val initialSecrets = data.lines().map { line -> line.toLong() }

        return initialSecrets.sumOf { seed ->
            generateSecretNumbers(seed, nbPasswords).last()
        }
    }

    fun partTwo(data: String, nbPasswords: Int = 2001): Int {

        // Generate the sequence of differences between each pair of consecutive numbers in the sequence.
        val sequenceForHighestPrice = data.lines()
            .flatMap { line ->
                generateSecretNumbers(line.toLong(), nbPasswords)
                    .map { it.toInt() % 10 }
                    .zipWithNext { a, b -> b to (b - a) }
                    .windowed(4) { list ->
                        list.map { it.second } to list.last().first
                    }
                    .distinctBy { (d, _) -> d }
            }


        // Group the sequenceForHighestPrice list by the first element of each pair (sequence, _).
        val groupedSequences = sequenceForHighestPrice.groupBy { (sequence, _) -> sequence }
            // Sum the counts for each group.
            .mapValues { (_, values) -> values.sumOf { (_, count) -> count } }

        // Get the maximum value from the map of grouped sequences
        val bestValue = groupedSequences.maxByOrNull { (_, value) -> value }?.value

        return bestValue ?: 0
    }
}