package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day20/training.txt")
    val data = readFileDirectlyAsText("/year2022/day20/data.txt")

    val lines = data.split("\n")

    val day = Day20("--- Day 20: Grove Positioning System ---", "https://adventofcode.com/2022/day/20")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }

}

private class Day20(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(lines: List<String>): Long {
        val initial = lines.mapIndexed { index, i -> Pair(index, i.toLong()) }
        val mixed = initial.toMutableList()

        // mixing
        mix(initial, mixed)

        return mixed.map { pair -> pair.second }.let { values ->
            val index0 = values.indexOf(0)
            values[(1000 + index0) % mixed.size] + values[(2000 + index0) % mixed.size] + values[(3000 + index0) % mixed.size]
        }
    }

    fun partTwo(lines: List<String>): Long {
        val decryptKey = 811589153
        val initial = lines.mapIndexed { index, i -> Pair(index, i.toLong() * decryptKey) }
        val mixed = initial.toMutableList()

        // mixing
        repeat(10) {
            mix(initial, mixed)
        }

        return mixed.map { pair -> pair.second }.let { values ->
            val index0 = values.indexOf(0)
            values[(1000 + index0) % mixed.size] + values[(2000 + index0) % mixed.size] + values[(3000 + index0) % mixed.size]
        }
    }

    private fun mix(
        initial: List<Pair<Int, Long>>,
        mixed: MutableList<Pair<Int, Long>>,
    ) {
        initial.forEach { pair ->
            val index = mixed.indexOf(pair)
            mixed.removeAt(index)
            mixed.add((index + pair.second).mod(mixed.size), pair)
        }
    }
}
 
