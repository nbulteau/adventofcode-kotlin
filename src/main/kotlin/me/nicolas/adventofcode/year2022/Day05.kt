package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText
import java.util.*

fun main() {

    val training = readFileDirectlyAsText("/year2022/day05/training.txt")
    val data = readFileDirectlyAsText("/year2022/day05/data.txt")

    val lines = data.split("\n\n")

    val day = Day05("--- Day 5: Supply Stacks ---", "https://adventofcode.com/2022/day/5")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

private class Day05(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(lines: List<String>): String {
        val stacksOfCrates = buildStacksOfCrates(lines)

        val moves = lines.last().split("\n")
        for (move in moves) {
            // Get all integers
            val (quantity, from, to) = "(\\d+)".toRegex().findAll(move).map { it.value.toInt() }.toList()
            repeat(quantity) {
                stacksOfCrates[to - 1].push(stacksOfCrates[from - 1].pop())
            }
        }

        return stacksOfCrates.joinToString("") { it.peek() }.filter { it.isLetter() }
    }

    fun partTwo(lines: List<String>): String {
        val stacksOfCrates = buildStacksOfCrates(lines)

        val moves = lines.last().split("\n")
        for (move in moves) {
            // Get all integers
            val (quantity, from, to) = "(\\d+)".toRegex().findAll(move).map { it.value.toInt() }.toList()
            val toPush = (1..quantity).map { stacksOfCrates[from - 1].pop() }.reversed()
            stacksOfCrates[to - 1].addAll(toPush)
        }

        return stacksOfCrates.joinToString("") { it.peek() }.filter { it.isLetter() }
    }

    private fun buildStacksOfCrates(lines: List<String>): List<Stack<String>> {
        val stacks = lines.first().split("\n")
            .reversed()
            .drop(1)
            .map { it.windowed(3, 4) }

        val stacksOfCrates = (0..8).map { Stack<String>() }
        for (line in stacks) {
            for (index in 0..stacks.size) {
                val crate = line.getOrNull(index)
                if (!crate.isNullOrBlank()) {
                    stacksOfCrates[index].push(crate)
                }
            }
        }

        return stacksOfCrates
    }
}
