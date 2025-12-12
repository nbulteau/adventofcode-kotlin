package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 22: Crab Combat ---
// https://adventofcode.com/2020/day/22
fun main() {
    val data = readFileDirectlyAsText("/year2020/day22/data.txt")
    val day = Day22(2020, 22, "Crab Combat")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day22(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        val (deckPlayer1, deckPlayer2) = parseDecks(data)

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
        return winner.reversed().mapIndexed { index, i -> i.toLong() * (index + 1) }.reduce { acc, l -> acc + l }
    }

    fun partTwo(data: String): Long {
        val (deckPlayer1, deckPlayer2) = parseDecks(data)

        playSubGame(deckPlayer1, deckPlayer2)

        val winner = if (deckPlayer1.isEmpty()) deckPlayer2 else deckPlayer1
        return winner.reversed().mapIndexed { index, i -> i.toLong() * (index + 1) }.reduce { acc, l -> acc + l }
    }

    private fun parseDecks(data: String): Pair<MutableList<Int>, MutableList<Int>> {
        val input = data.split("\n\n")
        val decks = input.map { str -> str.split("\n").drop(1).map { it.toInt() }.toMutableList() }
        return Pair(decks[0], decks[1])
    }

    private fun playSubGame(deckPlayer1: MutableList<Int>, deckPlayer2: MutableList<Int>) {
        val previousStates = mutableSetOf<String>()

        do {
            val state = "${deckPlayer1.hashCode()}-${deckPlayer2.hashCode()}"

            if (previousStates.contains(state)) {
                deckPlayer1.addAll(deckPlayer2)
                deckPlayer2.clear()
                return
            } else {
                previousStates.add(state)
            }

            val drawPlayer1 = deckPlayer1.removeAt(0)
            val drawPlayer2 = deckPlayer2.removeAt(0)

            if (drawPlayer1 <= deckPlayer1.size && drawPlayer2 <= deckPlayer2.size) {
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