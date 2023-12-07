package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day07/data.txt")
    val day = Day07("--- Day 7: Camel Cards ---", "https://adventofcode.com/2023/day/7")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(data: String): Int {
        val hands = extractHands(data)

        // Compute the score of each hand and sum them
        return hands.withIndex().sumOf { (index, hand) ->
            // The score of a hand is the bid multiplied by its index in the list
            (index + 1) * hand.bid
        }
    }

    fun partTwo(data: String): Int {
        val hands = extractHandsWithNewRule(data)

        // Compute the score of each hand and sum them
        return hands.withIndex().sumOf { (index, hand) ->
            // The score of a hand is the bid multiplied by its index in the list
            (index + 1) * hand.bid
        }
    }

    private enum class HandType {
        FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD;
    }

    private enum class Card {
        A, K, Q, J, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`, `1`
    }

    private data class Hand(val cards: List<Card>, val bid: Int) : Comparable<Hand> {
        private fun getHandType(hand: Hand): HandType {
            // Group cards by their value (A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2, 1) and count the number of cards for each value
            val counts = hand.cards.groupBy { card -> card }
            return when {
                // 1. Five of a Kind: Five cards of the same rank.
                (counts.size == 1) -> HandType.FIVE_OF_A_KIND
                // 2. Four of a Kind: Four cards of the same rank.
                (counts.any { entry -> entry.value.size == 4 }) -> HandType.FOUR_OF_A_KIND
                // 3. Full House: Three of a Kind with a Pair.
                (counts.any { entry -> entry.value.size == 3 } && counts.any { it.value.size == 2 }) -> HandType.FULL_HOUSE
                // 4. Three of a Kind: Three cards of the same rank.
                (counts.any { entry -> entry.value.size == 3 }) -> HandType.THREE_OF_A_KIND
                // 5. Two Pair: Two different Pair.
                (counts.filter { entry -> entry.value.size == 2 }.count() == 2) -> HandType.TWO_PAIR
                // 6. One Pair: Two cards of the same rank.
                (counts.any { entry -> entry.value.size == 2 }) -> HandType.ONE_PAIR
                // 7. High Card: None of the above conditions are met.
                else -> HandType.HIGH_CARD
            }
        }

        override fun compareTo(other: Hand) =
        // Compare by hand type, then by cards in the hand
            // 33332 and 2AAAA are both four of a kind hands, but 33332 is stronger because its first card is stronger
            compareBy<Hand>(
                { hand -> getHandType(hand) },
                { hand -> hand.cards[0] },
                { hand -> hand.cards[1] },
                { hand -> hand.cards[2] },
                { hand -> hand.cards[3] },
                { hand -> hand.cards[4] },
            ).compare(this, other)
    }

    private fun extractHands(data: String): List<Hand> {
        return data.lines().filter { it.isNotBlank() }.map { line ->
            val split = line.split(" ")
            Hand(
                cards = split.first().toCharArray().map { Card.valueOf(it.toString()) },
                bid = split.last().toInt()
            )
        }
            // Sort by hand type
            .sortedDescending()
    }

    private enum class CardWithNewRule {
        A, K, Q, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`, `1`, J
    }

    private data class HandWithNewRule(val cards: List<CardWithNewRule>, val bid: Int) : Comparable<HandWithNewRule> {

        // J cards can pretend to be whatever card is best for the purpose of determining hand type;
        // for example, QJJQ2 is now considered four of a kind. However,
        // for the purpose of breaking ties between two hands of the same type,
        // J is always treated as J, not the card it's pretending to be: JKKK2 is weaker than QQQQ2 because J is weaker than Q.
        private fun getHandType(hand: HandWithNewRule): HandType {
            // Group cards by their value (A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, 1, J) and count the number of cards for each value
            val counts = hand.cards.groupBy { card -> card }
            // Count the number of J cards in the hand (J is a joker)
            val jokerCount = counts[CardWithNewRule.J]?.size ?: 0

            // Sort the counts in descending order (the first element is the most frequent card) and remove J cards
            val sortedCounts = counts.filter { (key, _) -> key != CardWithNewRule.J }.map { (_, value) -> value.size }
                .sortedDescending()
            // The most frequent card
            val first = sortedCounts.getOrElse(0) { 0 } // If the list is empty, return 0 (for hand = JJJJJ)
            val second: Int = sortedCounts.getOrElse(1) { 0 }

            // 1. Five of a Kind: Five cards of the same rank. (Note that JJJQQ is not a Full House.)
            if (first == 5 - jokerCount) return HandType.FIVE_OF_A_KIND
            // 2. Four of a Kind: Four cards of the same rank.
            if (first == 4 - jokerCount) return HandType.FOUR_OF_A_KIND
            // 3. Full House: Three of a Kind with a Pair.
            if (first >= 3 - jokerCount && second == 2) return HandType.FULL_HOUSE
            // 3'. Full House: Three of a Kind with a Pair.
            if (first == 3 && second >= 2 - jokerCount) return HandType.FULL_HOUSE
            // 4. Three of a Kind: Three cards of the same rank.
            if (first == 3 - jokerCount) return HandType.THREE_OF_A_KIND
            // 5. Two Pair: Two different Pair.
            if (first == 2 && second >= 2 - jokerCount) return HandType.TWO_PAIR
            // 6. One Pair: Two cards of the same rank.
            if (first == 2 - jokerCount) return HandType.ONE_PAIR
            // 7. High Card: None of the above conditions are met.
            return HandType.HIGH_CARD
        }

        override fun compareTo(other: HandWithNewRule) =
        // Compare by hand type, then by cards in the hand
            // 33332 and 2AAAA are both four of a kind hands, but 33332 is stronger because its first card is stronger
            compareBy<HandWithNewRule>(
                { hand -> getHandType(hand) },
                { hand -> hand.cards[0] },
                { hand -> hand.cards[1] },
                { hand -> hand.cards[2] },
                { hand -> hand.cards[3] },
                { hand -> hand.cards[4] },
            ).compare(this, other)
    }

    private fun extractHandsWithNewRule(data: String): List<HandWithNewRule> {
        return data.lines().filter { it.isNotBlank() }.map { line ->
            val split = line.split(" ")
            HandWithNewRule(
                cards = split.first().toCharArray().map { CardWithNewRule.valueOf(it.toString()) },
                bid = split.last().toInt()
            )
        }
            // Sort by hand type
            .sortedDescending()
    }
}