package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day11/training.txt")
    val data = readFileDirectlyAsText("/year2022/day11/data.txt")

    val monkeys = data.split("\n\n")

    val day = Day11(2022, 11,"Monkey in the Middle")
    prettyPrintPartOne { day.partOne(monkeys) }
    prettyPrintPartTwo { day.partTwo(monkeys) }
}

private class Day11(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(inputs: List<String>): Int {
        val monkeys = buildMonkeys(inputs)

        repeat(20) {
            monkeys.forEach { monkey ->
                val transfers = monkey.processRound { worryLevel -> worryLevel / 3 }
                transfers.forEach { transfer ->
                    monkeys[transfer.first].items += transfer.second
                }
            }
        }

        return monkeys
            .sortedByDescending { it.inspected }
            .take(2)
            .map { it.inspected }
            .reduce { a: Int, b: Int -> a * b }
    }

    fun partTwo(inputs: List<String>): Long {
        val monkeys = buildMonkeys(inputs)

        // Tests are always modulo (5, 2, 13, 19, 11, 3, 7, 17)
        // We need to figure out some math operation that doesn’t change the outcome of x % n, but reduces x.
        // i.e. : Least Common Multiple
        // val leastCommonMultiple = monkeys.map { e: Monkey -> e.divisibleBy }.lcm()
        // 5, 2, 13, 19, 11, 3, 7, 17 are all prime among themselves
        // The LCM of two or more prime numbers is equal to their product.
        val leastCommonMultiple = monkeys.map { e: Monkey -> e.divisibleBy }.reduce { acc: Long, i: Long -> acc * i }

        val rounds = 10000
        repeat(rounds) {
            monkeys.forEach { monkey ->
                val transfers = monkey.processRound { worryLevel -> worryLevel % leastCommonMultiple }
                transfers.forEach { transfer ->
                    monkeys[transfer.first].items += transfer.second
                }
            }
        }

        // display(rounds, monkeys)

        return monkeys
            .sortedByDescending { it.inspected }
            .take(2)
            .map { it.inspected.toLong() }
            .reduce { acc: Long, i: Long -> acc * i }
    }

    private fun display(rounds: Int, monkeys: List<Monkey>) {
        println("== After round $rounds ==")
        monkeys.forEach { monkey ->
            println("Monkey ${monkey.name} inspected items ${monkey.inspected} times.")
        }
    }

    data class Monkey(
        val name: String,
        var items: MutableList<Long>,
        val operation: Pair<String, String>,
        val divisibleBy: Long,
        val action: Pair<String, String>,
    ) {
        var inspected = 0

        fun processRound(lamda: (Long) -> Long): List<Pair<Int, Long>> {
            val transfers = this.items.map {
                inspected++
                var worryLevel = operation.process(it)
                worryLevel = lamda(worryLevel)
                if (worryLevel % this.divisibleBy == 0L) {
                    action.first.toInt() to worryLevel
                } else {
                    action.second.toInt() to worryLevel
                }
            }

            items.clear()

            return transfers
        }

        private fun Pair<String, String>.process(worryLevel: Long): Long {
            val value = if (this.second == "old") worryLevel else this.second.toLong()
            return when (this.first) {
                "+" -> worryLevel + value
                else -> worryLevel * value
            }
        }
    }

    private fun buildMonkeys(inputs: List<String>): List<Monkey> {
        return inputs.map { monkey ->
            val lines = monkey.split("\n")
            Monkey(
                name = lines[0].substringAfter("Monkey ").substringBefore(":"),
                items = lines[1].substringAfter("Starting items:").split(", ").map { it.trim().toLong() }
                    .toMutableList(),
                operation = lines[2].substringAfter("Operation: new = old ").split(" ").windowed(2, 2)
                    .map { Pair(it[0], it[1].trim()) }.first(),
                divisibleBy = lines[3].substringAfter("Test: divisible by ").toLong(),
                action = Pair(
                    lines[4].substringAfter("If true: throw to monkey "),
                    lines[5].substringAfter("If false: throw to monkey ")
                )
            )
        }
    }
}


