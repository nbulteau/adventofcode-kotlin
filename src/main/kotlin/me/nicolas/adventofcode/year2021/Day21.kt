package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2021/day/21
fun main() {
    val data = readFileDirectlyAsText("/year2021/day21/data.txt")
    val day = Day21(2021, 21, "Dirac Dice")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day21(year: Int, day: Int, title: String = "Dirac Dice") : AdventOfCodeDay(year, day, title) {

    fun partOne(input: String): Int {
        val (posPlayer1, posPlayer2) = parseInput(input)
        return processGamePartOne(posPlayer1, posPlayer2)
    }

    fun partTwo(input: String): ULong {
        val (posPlayer1, posPlayer2) = parseInput(input)

        val (gameWinPlayer1, gameWinPlayer2) = processGames(State(posPlayer1, posPlayer2))

        return if (gameWinPlayer1 > gameWinPlayer2) {
            gameWinPlayer1
        } else {
            gameWinPlayer2
        }
    }

    private fun processGamePartOne(pos1: Int, pos2: Int): Int {
        var index = 1
        val scorePlayer = IntArray(2)
        val posPlayer = listOf(pos1, pos2).toIntArray()

        var nbRoll = 0
        fun next(start: Int): Int {
            nbRoll += 3
            return (start + start + 1 + start + 2)
        }

        var playerIndex = 0
        while (true) {
            posPlayer[playerIndex] = (posPlayer[playerIndex] + next(index) - 1) % 10 + 1
            scorePlayer[playerIndex] += posPlayer[playerIndex]
            if (scorePlayer[playerIndex] >= 1000) {
                return nbRoll * scorePlayer[1 - playerIndex]
            }
            index += 3
            playerIndex = (playerIndex + 1) % 2
        }
    }

    // cache is needed !!!
    val cache = mutableMapOf<State, Pair<ULong, ULong>>()

    data class State(
        val posPlayer1: Int,
        val posPlayer2: Int,
        val scorePlayer1: Int = 0,
        val scorePlayer2: Int = 0,
        val turn: Int = 0,
    )

    private fun processGames(state: State): Pair<ULong, ULong> {
        cache[state]?.let { return it }
        if (state.scorePlayer1 >= 21) return 1UL to 0UL
        if (state.scorePlayer2 >= 21) return 0UL to 1UL

        val results = mutableListOf<Pair<ULong, ULong>>()
        (1..3).forEach { first ->
            (1..3).forEach { second ->
                (1..3).forEach { third ->
                    val nextState = if (state.turn % 2 == 0) {
                        val nextPosPlayer1 = (state.posPlayer1 + (first + second + third) - 1) % 10 + 1
                        val nextScorePlayer1 = state.scorePlayer1 + nextPosPlayer1
                        State(
                            nextPosPlayer1,
                            state.posPlayer2,
                            nextScorePlayer1,
                            state.scorePlayer2,
                            (state.turn + 1) % 2
                        )
                    } else {
                        val nextPosPlayer2 = (state.posPlayer2 + (first + second + third) - 1) % 10 + 1
                        val nextScorePlayer2 = state.scorePlayer2 + nextPosPlayer2
                        State(
                            state.posPlayer1,
                            nextPosPlayer2,
                            state.scorePlayer1,
                            nextScorePlayer2,
                            (state.turn + 1) % 2
                        )
                    }
                    results.add(processGames(nextState))
                }
            }
        }
        val result =
            results.fold(Pair(0UL, 0UL)) { acc, scores -> Pair(acc.first + scores.first, acc.second + scores.second) }
        cache[state] = result

        return result
    }

    private fun parseInput(input: String): Pair<Int, Int> {
        val posPlayer1 = input.split("\n").first().substringAfter("Player 1 starting position: ").toInt()
        val posPlayer2 = input.split("\n").last().substringAfter("Player 2 starting position: ").toInt()

        return Pair(posPlayer1, posPlayer2)
    }
}
