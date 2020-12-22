package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 22: Crab Combat ---
// https://adventofcode.com/2020/day/22
@ExperimentalTime
fun main() {

    println("--- Day 22: Crab Combat ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day22/training.txt")
    val data = readFileDirectlyAsText("/year2020/day22/data.txt")

    val input = data.split("\n\n")
    val (deckPlayer1, deckPlayer2) = extractDecks(input)

    // Part One
    Day22().partOne(deckPlayer1.toMutableList(), deckPlayer2.toMutableList())

    // Part Two
    val duration = measureTime { Day22().partTwo(deckPlayer1.toMutableList(), deckPlayer2.toMutableList()) }
    println("Part two duration : $duration")
}

private fun extractDecks(input: List<String>): Pair<List<Int>, List<Int>> {

    val decks = input.map { str -> str.split("\n").drop(1).map { it.toInt() } }

    return Pair(decks[0], decks[1])
}


private class Day22 {

    fun partOne(deckPlayer1: MutableList<Int>, deckPlayer2: MutableList<Int>) {

        do {
            val drawPlayer1 = deckPlayer1.removeAt(0)
            val drawPlayer2 = deckPlayer2.removeAt(0)

            if (drawPlayer1 > drawPlayer2) {
                deckPlayer1.addAll(listOf(drawPlayer1, drawPlayer2))
            } else {
                deckPlayer2.addAll(listOf(drawPlayer2, drawPlayer1))
            }
        } while (deckPlayer1.isNotEmpty() && deckPlayer2.isNotEmpty())

        val winner = if (deckPlayer1.isEmpty()) deckPlayer2 else deckPlayer1
        val result = winner.reversed().mapIndexed { index, i -> i.toLong() * (index + 1) }.reduce { acc, l -> acc + l }

        println("Part one = $result")
    }

    fun partTwo(deckPlayer1: MutableList<Int>, deckPlayer2: MutableList<Int>) {

        playSubGame(deckPlayer1, deckPlayer2)

        val winner = if (deckPlayer1.isEmpty()) deckPlayer2 else deckPlayer1
        val result = winner.reversed().mapIndexed { index, i -> i.toLong() * (index + 1) }.reduce { acc, l -> acc + l }

        println("Part two = $result")
    }

    fun playSubGame(deckPlayer1: MutableList<Int>, deckPlayer2: MutableList<Int>) {

        val previousStates = mutableSetOf<String>()

        do {
            //val state = deckPlayer1.joinToString("") + "-" + deckPlayer2.joinToString("")
            val state = "${deckPlayer1.hashCode()}-${deckPlayer2.hashCode()}"

            // If there was a previous round in this game that had exactly the same cards in the same order in the same players' decks,
            // the game instantly ends in a win for player 1.
            if (previousStates.contains(state)) {
                deckPlayer1.addAll(deckPlayer2)
                deckPlayer2.clear()
                return
            } else {
                previousStates.add(state)
            }

            // draw cards
            val drawPlayer1 = deckPlayer1.removeAt(0)
            val drawPlayer2 = deckPlayer2.removeAt(0)

            // If both players have at least as many cards remaining in their deck as the value of the card they just drew
            if (drawPlayer1 <= deckPlayer1.size && drawPlayer2 <= deckPlayer2.size) {

                // To play a sub-game of Recursive Combat, each player creates a new deck by making a copy of the next cards in their deck
                // (the quantity of cards copied is equal to the number on the card they drew to trigger the sub-game)
                val deckPlayer1SubList = deckPlayer1.subList(0, drawPlayer1).toMutableList()
                playSubGame(deckPlayer1SubList, deckPlayer2.subList(0, drawPlayer2).toMutableList())

                if (deckPlayer1SubList.isNotEmpty()) {
                    deckPlayer1.addAll(listOf(drawPlayer1, drawPlayer2))
                } else {
                    deckPlayer2.addAll(listOf(drawPlayer2, drawPlayer1))
                }
            } else {
                if (drawPlayer1 > drawPlayer2) {
                    deckPlayer1.addAll(listOf(drawPlayer1, drawPlayer2))
                } else {
                    deckPlayer2.addAll(listOf(drawPlayer2, drawPlayer1))
                }
            }
        } while (deckPlayer1.isNotEmpty() && deckPlayer2.isNotEmpty())
    }
}