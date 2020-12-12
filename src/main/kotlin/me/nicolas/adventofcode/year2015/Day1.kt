package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.readFileDirectlyAsText


// --- Day 1: Not Quite Lisp ---
// https://adventofcode.com/2015/day/1
fun main(args: Array<String>) {

    println("--- Day 1: Not Quite Lisp ---")
    println()

    val training = readFileDirectlyAsText("/year2015/day01/training.txt")
    val data = readFileDirectlyAsText("/year2015/day01/data.txt")

    // Part One
    partOne(data)

    // Part Two
    partTwo(data)
}

private fun partOne(directions: String) {
    val result = directions.count { char -> char == '(' } - directions.count { char -> char == ')' }

    println("Part one $result")
}

private fun partTwo(directions: String) {
    var floor = 0
    var index = 0
    do {
        when (directions[index]) {
            '(' -> floor += 1
            ')' -> floor -= 1
        }
        index++
    } while (floor != -1 && index < directions.length)

    println("Part two $index")
}
