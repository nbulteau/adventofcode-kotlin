package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 22: Monkey Market ---
fun main() {
    val data = readFileDirectlyAsText("/year2024/day22/data.txt")
    val day = Day22(2024, 22)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

/**
 * To solve this problem, we need to simulate the generation of secret numbers and their corresponding prices for each buyer.
 * Then, we need to find the sequence of four consecutive price changes that maximizes the number of bananas we can get by selling at the right time.
 *
 * Part One
 * - Generate Secret Numbers: For each buyer, generate 2000 secret numbers starting from their initial secret number using the given transformation rules.
 * - Sum the 2000th Secret Numbers: Calculate the sum of the 2000th secret number for each buyer.
 *
 * Part Two
 * - Generate Prices: For each buyer, convert the secret numbers to prices by taking the last digit of each secret number.
 * - Calculate Price Changes: Calculate the changes in prices for each buyer.
 * - Find the Best Sequence: Determine the sequence of four consecutive price changes that maximizes the total number of bananas we can get by selling at the right time.
 */

class Day22(year: Int, day: Int, title: String = "Monkey Market") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        val initialSecrets = data.lines().map { line -> line.toLong() }
        val secretNumbers = initialSecrets.map { seed -> generateSecretNumbers(seed) }

        return secretNumbers.sumOf { list -> list.last() }
    }

    fun partTwo(data: String): Int {
        val initialSecrets = data.lines().map { line -> line.toLong() }
        val secretNumbers = initialSecrets.map { seed -> generateSecretNumbers(seed) }

        val sequenceForHighestPrice = secretNumbers
            .flatMap { list -> list.transformSecretNumbers() }
            .groupBy { (sequence, _) -> sequence }
            .mapValues { (_, values) -> values.sumOf { (_, count) -> count } }

        return sequenceForHighestPrice
            .maxBy { (_, value) -> value }.value
    }

    private fun generateSecretNumbers(start: Long): List<Long> {
        val secretNumbers = mutableListOf(start)
        var current = start
        repeat(times = 2000) {
            current = nextSecretNumber(current)
            secretNumbers.add(current)
        }
        return secretNumbers
    }

    private val cache = mutableMapOf<Long, Long>()

    private fun nextSecretNumber(secret: Long): Long {
        return cache.computeIfAbsent(secret) { current ->
            var nextSecret = current
            nextSecret = (nextSecret xor (nextSecret * 64)) % 16777216
            nextSecret = (nextSecret xor (nextSecret / 32)) % 16777216
            nextSecret = (nextSecret xor (nextSecret * 2048)) % 16777216
            nextSecret
        }
    }

    // processes the list of secret numbers to generate sequences of differences and filters them to ensure uniqueness
    private fun List<Long>.transformSecretNumbers(): List<Pair<List<Int>, Int>> =
        this.map { secret -> secret.toInt() % 10 }
            .zipWithNext { a, b -> b to (b - a) }
            .windowed(4) { list ->
                list.map { pair -> pair.second } to list.last().first
            }
            .distinctBy { (list, _) -> list }
}