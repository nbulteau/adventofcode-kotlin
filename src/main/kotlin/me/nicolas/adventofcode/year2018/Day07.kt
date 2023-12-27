package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2018/day/7

fun main() {
    val data = readFileDirectlyAsText("/year2018/day07/data.txt")
    val day = Day07(2018, 7, "The Sum of Its Parts", data)
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo(5) { char -> 60 + (char - 'A' + 1) } }
}


class Day07(year: Int, day: Int, title: String, data: String) : AdventOfCodeDay(year, day, title) {

    // Parse the input data into pairs of characters
    private val allPairs = parseInput(data)
    // Generate a map of each character to its dependencies
    private val childrenOf: Map<Char, Set<Char>> = generateDependencies(allPairs)
    // Generate a map of each character to its dependents
    private val parentsOf: Map<Char, Set<Char>> = generateDependencies(allPairs.map { (from, to) -> to to from })
    // Get all unique characters
    private val allKeys = childrenOf.keys.union(parentsOf.keys)

    private fun parseInput(data: String): List<Pair<Char, Char>> =
        data.lines().map { row ->
            row.split(" ").run { this[1].first() to this[7].first() }
        }

    private fun generateDependencies(pairs: List<Pair<Char, Char>>): Map<Char, Set<Char>> =
        pairs
            .groupBy { pair -> pair.first }
            .mapValues { (_, value) -> value.map { pair -> pair.second }.toSet() }

    // Determine the order in which the steps should be completed
    fun partOne(): String {
        val ready = allKeys.filterNot { it in parentsOf }.toMutableList()
        val done = mutableListOf<Char>()
        while (ready.isNotEmpty()) {
            val next = ready.minOf { it }.also { ready.remove(it) }
            done.add(next)
            childrenOf[next]?.let { maybeReadySet ->
                ready.addAll(maybeReadySet.filter { maybeReady ->
                    parentsOf.getValue(maybeReady).all { it in done }
                })
            }
        }
        return done.joinToString(separator = "")
    }

    // Determine the time it takes to complete all the steps with a given number of workers and a cost function
    fun partTwo(workers: Int, costFunction: (Char) -> Int): Int {
        val ready = allKeys.filterNot { char -> char in parentsOf }.map { char -> char to costFunction(char) }.toMutableList()
        val done = mutableListOf<Char>()
        var time = 0

        while (ready.isNotEmpty()) {
            // Work on things that are ready.
            // Do one unit of work per worker, per item at the head of the queue.
            ready.take(workers).forEachIndexed { idx, work ->
                ready[idx] = Pair(work.first, work.second - 1)
            }

            // These are done
            ready.filter { (_, value) -> value == 0 }.forEach { workItem ->
                done.add(workItem.first)

                // Now that we are done, add some to ready that have become unblocked
                childrenOf[workItem.first]?.let { maybeReadySet ->
                    ready.addAll(
                        maybeReadySet.filter { maybeReady ->
                            parentsOf.getValue(maybeReady).all { char -> char in done }
                        }
                            .map { char -> char to costFunction(char) }
                            .sortedBy { (char, _) -> char }
                    )
                }
            }

            // Remove anything that we don't need to look at anymore.
            ready.removeIf { (_, value) -> value == 0 }

            time++
        }

        return time
    }
}


