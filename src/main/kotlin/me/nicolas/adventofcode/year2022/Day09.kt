package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

fun main() {

    val training = readFileDirectlyAsText("/year2022/day09/training.txt")
    val data = readFileDirectlyAsText("/year2022/day09/data.txt")

    val lines = data.split("\n")

    val day = Day09(2022, 9, "Rope Bridge")
    prettyPrintPartOne { day.solve(lines, 2) }
    prettyPrintPartTwo { day.solve(lines, 10) }
}

private class Day09(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private val motions = mapOf("R" to Pair(0, 1), "L" to Pair(0, -1), "D" to Pair(1, 0), "U" to Pair(-1, 0))

    /**
     * The head (H) and tail (T) must always be touching
     * (diagonally adjacent and even overlapping both count as touching)
     */
    fun solve(seriesOfMotions: List<String>, nbKnots: Int): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val rope = MutableList(nbKnots) { Pair(0, 0) }

        seriesOfMotions
            .map { it.split(" ") }
            .forEach { (direction, steps) ->
                repeat(steps.toInt()) {
                    val motion = motions[direction]!!
                    rope[0] = Pair(rope.first().first + motion.first, rope.first().second + motion.second) // Head

                    (1 until nbKnots).forEach { index -> // Tail
                        val knot1 = rope[index - 1]
                        val knot2 = rope[index]
                        // The knot1 and knot2 must always be touching
                        // (diagonally adjacent and even overlapping both count as touching)
                        val delta = Pair(abs(knot1.first - knot2.first), abs(knot1.second - knot2.second))
                        if (delta.first == 2 || delta.second == 2) {
                            val first = if (delta.first == 2) {
                                (knot1.first + knot2.first) / 2
                            } else {
                                knot1.first
                            }
                            val second = if (delta.second == 2) {
                                (knot1.second + knot2.second) / 2
                            } else {
                                knot1.second
                            }
                            rope[index] = Pair(first, second)
                        }
                    }
                    visited.add(rope.last())
                }
            }
        // display(visited)
        return visited.size
    }

    private fun display(visited: Set<Pair<Int, Int>>) {
        for (x in visited.minOf { it.first } - 2..visited.maxOf { it.first } + 2) {
            for (y in visited.minOf { it.second } - 2..visited.maxOf { it.second } + 2) {
                val pair = Pair(x, y)
                if (pair == Pair(0, 0)) {
                    print("s")
                } else if (visited.contains(pair)) {
                    print("#")
                } else {
                    print('.')
                }
            }
            println()
        }
    }
}