package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText

// --- Day 1: Report Repair ---
// https://adventofcode.com/2020/day/1
fun main() {

    val training = readFileDirectlyAsText("/year2020/day01/training.txt")
    val data = readFileDirectlyAsText("/year2020/day01/data.txt")

    val numbers = data.split("\n").map { string -> string.toInt() }

    // Part One
    partOne(numbers)

    // Part Two
    partTwo(numbers)
}

private fun partOne(numbers: List<Int>) {
    val list = numbers.filter { numbers.contains(2020 - it) }
    if (list.isNotEmpty()) {
        println("Part one answer = ${list[0]} * ${list[1]} = ${list[0] * list[1]}")
    }
}

private fun partTwo(numbers: List<Int>) {
    val list = numbers.flatMap { n -> numbers.filter { numbers.contains(2020 - n - it) } }
    if (list.isNotEmpty()) {
        println("Part two answer = ${list[0]} * ${list[1]} * ${list[2]} = ${list[0] * list[1] * list[2]}")
    }
}


