package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText

typealias Layout = List<String>

// --- Day 11: Seating System ---
// https://adventofcode.com/2020/day/11
fun main() {

    println("--- Day 11: Seating System ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day11/training.txt")
    val data = readFileDirectlyAsText("/year2020/day11/data.txt")

    val layout: Layout = data.split("\n")

    // Part One
    Day11().partOne(layout)

    // Part Two
    Day11().partTwo(layout)
}

class Day11 {

    fun partOne(layout: Layout) {

        var newLayout = layout
        var previousLayout: List<String>
        do {
            previousLayout = newLayout
            newLayout = previousLayout.processLayoutPartOne()

            // newLayout.displayLayout()
        } while (newLayout != previousLayout)

        println("Part one = ${newLayout.count('#')}")
    }

    fun partTwo(layout: Layout) {

        var newLayout = layout
        var previousLayout: List<String>
        do {
            previousLayout = newLayout
            newLayout = previousLayout.processLayoutPartTwo()

            //newLayout.displayLayout()
        } while (newLayout != previousLayout)

        println("Part two = ${newLayout.count('#')}")
    }

    enum class Direction(var dx: Int, var dy: Int) {
        NORTH(0, -1),
        NORTH_EAST(1, -1),
        EAST(1, 0),
        SOUTH_EAST(1, 1),
        SOUTH(0, 1),
        SOUTH_WEST(-1, 1),
        WEST(-1, 0),
        NORTH_WEST(-1, -1);
    }
}

fun Layout.processLayoutPartOne(): Layout {
    val height = this.size - 1
    val width = this[0].length - 1
    val newLayout = mutableListOf<String>()

    for (y in 0..height) {
        var newString = ""

        for (x in 0..width) {
            when (this[y][x]) {
                '.' -> newString += "."
                'L' -> {
                    newString += if (this.lookAround(x, y) == 0) {
                        "#"
                    } else {
                        "L"
                    }
                }
                '#' -> {
                    newString += if (this.lookAround(x, y) >= 4) {
                        "L"
                    } else {
                        "#"
                    }
                }
            }
        }
        newLayout += newString
    }
    return newLayout
}

fun Layout.processLayoutPartTwo(): Layout {
    val height = this.size - 1
    val width = this[0].length - 1
    val newLayout = mutableListOf<String>()

    for (y in 0..height) {
        var newString = ""

        for (x in 0..width) {
            when (this[y][x]) {
                '.' -> newString += "."
                'L' -> {
                    newString += if (this.lookInDirection(x, y) == 0) {
                        "#"
                    } else {
                        "L"
                    }
                }
                '#' -> {
                    newString += if (this.lookInDirection(x, y) >= 5) {
                        "L"
                    } else {
                        "#"
                    }
                }
            }
        }
        newLayout += newString
    }
    return newLayout
}

private fun Layout.displayLayout() {
    println()
    this.forEach { string -> println(string) }
}

private fun Layout.count(charToCount: Char) =
    this.sumOf { string -> string.count { char -> char == charToCount } }

fun Layout.lookAround(x: Int, y: Int) =
    Day11.Direction.values().sumBy { direction -> this.lookAround(x, y, direction) }

fun Layout.lookAround(x: Int, y: Int, direction: Day11.Direction): Int {

    val height = this.size - 1
    val width = this[0].length - 1

    val nextX = x + direction.dx
    val nextY = y + direction.dy

    if (nextX in 0..width && nextY in 0..height) {
        if (this[nextY][nextX] == '#') {
            return 1
        }
    }

    return 0
}

fun Layout.lookInDirection(x: Int, y: Int) =
    Day11.Direction.values().sumBy { direction -> this.lookInDirection(x, y, direction) }

fun Layout.lookInDirection(x: Int, y: Int, direction: Day11.Direction): Int {

    val height = this.size - 1
    val width = this[0].length - 1

    var nextX = x + direction.dx
    var nextY = y + direction.dy

    while (nextX in 0..width && nextY in 0..height) {
        if (this[nextY][nextX] == 'L') {
            return 0
        }
        if (this[nextY][nextX] == '#') {
            return 1
        }

        nextX += direction.dx
        nextY += direction.dy
    }

    return 0
}

