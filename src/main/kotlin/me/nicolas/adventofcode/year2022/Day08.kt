package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day08/training.txt")
    val data = readFileDirectlyAsText("/year2022/day08/data.txt")

    val lines = data.split("\n")

    val day = Day08("--- Day 8: Treetop Tree House ---", "https://adventofcode.com/2022/day/8")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

private class Day08(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(lines: List<String>): Int {
        val trees = lines.map { line -> line.toList().map { point -> point.toString().toInt() } }
        var visible = 2 * trees.size + 2 * (trees.first().size - 2)

        for (x in 1 until trees.first().size - 1) {
            for (y in 1 until trees.size - 1) {
                if (trees.isVisible(x, y)) {
                    visible++
                }
            }
        }

        return visible
    }

    fun partTwo(lines: List<String>): Int {
        val trees = lines.map { line -> line.toList().map { point -> point.toString().toInt() } }
        var maxScenicScore = -1

        for (x in 1 until trees.first().size - 1) {
            for (y in 1 until trees.size - 1) {
                maxScenicScore = maxOf(maxScenicScore, trees.scenicScore(x, y))
            }
        }

        return maxScenicScore
    }

    private enum class Direction(val deltaX: Int, val deltaY: Int) {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0),
    }

    /**
     * A tree is visible if all of the other trees between it and an edge of the grid are shorter than it.
     * Only consider trees in the same row or column; that is, only look up, down, left, or right from any given tree.
     */
    private fun List<List<Int>>.isVisible(x: Int, y: Int): Boolean {
        return this.isVisible(x, y, Direction.UP) ||
                this.isVisible(x, y, Direction.DOWN) ||
                this.isVisible(x, y, Direction.LEFT) ||
                this.isVisible(x, y, Direction.RIGHT)
    }

    private fun List<List<Int>>.isVisible(x: Int, y: Int, direction: Direction): Boolean {
        val treeSize = this[y][x]

        var dx = x + direction.deltaX
        var dy = y + direction.deltaY
        while (dx in 0 until this.size && dy in 0 until this.first().size) {
            if (this[dy][dx] >= treeSize) {
                return false
            }
            dx += direction.deltaX
            dy += direction.deltaY
        }

        return true
    }

    /**
     * A tree's scenic score is found by multiplying together its viewing distance in each of the four directions.
     * For this tree, this is 4 (found by multiplying 1 * 1 * 2 * 2).
     */
    private fun List<List<Int>>.scenicScore(x: Int, y: Int): Int {
        return this.scenicScore(x, y, Direction.UP) *
                this.scenicScore(x, y, Direction.DOWN) *
                this.scenicScore(x, y, Direction.LEFT) *
                this.scenicScore(x, y, Direction.RIGHT)
    }

    private fun List<List<Int>>.scenicScore(x: Int, y: Int, direction: Direction): Int {
        val treeSize = this[y][x]

        var dx = x + direction.deltaX
        var dy = y + direction.deltaY
        var score = 0
        while (dx in 0 until this.size && dy in 0 until this.first().size) {
            if (this[dy][dx] >= treeSize) {
                score++
                break
            }
            score++
            dx += direction.deltaX
            dy += direction.deltaY
        }

        return score
    }
}