package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.pow

fun main() {
    val data = readFileDirectlyAsText("/year2023/day04/data.txt")
    val lines = data.split("\n")
    val day = Day04("--- Day 4: Scratchcards ---", "https://adventofcode.com/2023/day/4")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day04(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    private val spaces = Pattern.compile(" +")

    data class Card(val winningNumbers: List<Int>, val numbersYouHave: List<Int>) {
        val points: Int
            // if you have 1 winning number, you win 1 point
            // if winning is <= 0, you win 0 points thanks to the max(0, winning - 1)
            get() = 2.0.pow(max(0, winning - 1)).toInt()

        val winning: Int
            get() = numbersYouHave.count { winningNumbers.contains(it) }
    }

    fun partOne(lines: List<String>): Int {
        val cards = buildCards(lines)

        return cards
            .sumOf { card -> card.points }
    }

    fun partTwo(lines: List<String>): Int {
        val cards = buildCards(lines)
        val counts = IntArray(cards.size) { 1 }
        cards.withIndex().forEach { (index, card) ->
            for (i in 1..card.winning) {
                if (index + i < cards.size) {
                    counts[index + i] += counts[index]
                }
            }
        }
        return counts.sum()
    }

    private fun buildCards(lines: List<String>): List<Card> {
        val cards = lines
            .map { line ->
                val numbers = line.split(":")[1]
                val (leftPart, rightPart) = numbers.split("|")
                val winningNumbers = leftPart.trim().split(spaces).map { it.toInt() }
                val numbersYouHave = rightPart.trim().split(spaces).map { it.toInt() }

                Card(winningNumbers, numbersYouHave)
            }
        return cards
    }
}

