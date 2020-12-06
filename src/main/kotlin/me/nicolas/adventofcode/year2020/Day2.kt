package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText

// --- Day 2: Password Philosophy ---
// https://adventofcode.com/2020/day/2
fun main() {

    val training = readFileDirectlyAsText("/year2020/day2/training.txt")
    val data = readFileDirectlyAsText("/year2020/day2/data.txt")

    val list = data.split("\n")

    partOne(list)

    partTwo(list)
}

private fun partOne(list: List<String>) {
    val result = list.count { string ->
        val pattern = string.substringBefore(":")
        val lower = pattern.substringBefore("-").toInt()
        val upper = pattern.substringAfter("-").substringBefore(" ").toInt()
        val letter = pattern.substringAfter(" ")
        val password = string.substringAfter(":")
        val count = password.count { letter.contains(it) }

        (count <= upper) && (count >= lower)
    }

    println("Part one : valid passwords $result")
}

private fun partTwo(list: List<String>) {
    val result = list.count { string ->
        val pattern = string.substringBefore(":")
        val lower = pattern.substringBefore("-").toInt()
        val upper = pattern.substringAfter("-").substringBefore(" ").toInt()
        val letter = pattern.substringAfter(" ")[0]
        val password = string.substringAfter(": ")

        (password[lower - 1] == letter) xor (password[upper - 1] == letter)
    }

    println("Part two : valid passwords $result")
}



