package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

/**
 * Inspired by Wilfrid Rabot's code : https://github.com/wrabot/AdventOfCode/blob/master/src/main/kotlin/aoc2023/Day7.kt
 */
fun main() {
    val data = readFileDirectlyAsText("/year2023/day07/data.txt")
    val day = Day07New(2023, 7, "Camel Cards")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07New(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val lines = data.lines()
        return solve(lines, withJoker = false)
    }

    fun partTwo(data: String): Int {
        val lines = data.lines()
        return solve(lines, withJoker = true)
    }

    private fun solve(lines: List<String>, withJoker: Boolean): Int {
        val cardsOrder = if (withJoker) "J23456789TQKA" else "23456789TQKA"
        val hands = lines
            // Convert each line to a Hand
            .map { line ->
                line.split(' ').let { (cards, bid) ->
                    // Convert cards using their index in the cardsOrder (their rank) string
                    Hand(cards.map { card -> cardsOrder.indexOf(card) }, bid.toInt(), withJoker)
                }
            }
            // Sort hands by their type and then by their cards
            .sorted()

        // Compute the score of each hand and sum them
        return hands.foldIndexed(0) { index, acc, hand ->
            acc + (index + 1) * hand.bid
        }
    }
    
    private enum class HandType { HighCard, OnePair, TwoPair, ThreeKind, FullHouse, FourKind, FiveKind }

    private data class Hand(val cards: List<Int>, val bid: Int, val withJoker: Boolean) : Comparable<Hand> {
        private val type = cards.groupingBy { it }.eachCount().let { counts ->
            // Count joker if present
            val joker = if (withJoker) counts.getOrElse(0) { 0 } else 0
            // Sort counts in descending order
            val others = counts.values.sortedDescending()
            // Primary pair is the biggest group of cards
            val primarySize = others.getOrNull(0) ?: 0
            // If there is a secondary pair, count must be of size 2
            val hasSecondaryPair = others.getOrNull(1) == 2
            when (primarySize + joker) {
                // 1. Five of a Kind: Five cards of the same rank.
                5 -> HandType.FiveKind
                // 2. Four of a Kind: Four cards of the same rank.
                4 -> HandType.FourKind
                // 3. Full House: Three of a Kind with a Pair or Three of a Kind if there is no secondary pair.
                3 -> if (hasSecondaryPair) HandType.FullHouse else HandType.ThreeKind
                // 4. Two Pair: Two different Pair or One Pair if there is no secondary pair.
                2 -> if (hasSecondaryPair) HandType.TwoPair else HandType.OnePair
                // 5. High Card: No other hand type.
                else -> HandType.HighCard
            }
        }

        // Compare by hand type, then by cards in the hand
        // 33332 and 2AAAA are both four of a kind hands, but 33332 is stronger because its first card is stronger
        override fun compareTo(other: Hand) =
            compareBy<Hand>(
                { hand -> hand.type },
                { hand -> hand.cards.zip(other.cards).map { it.first - it.second }.find { it != 0 } ?: 0 }
            ).compare(this, other)
    }
}
