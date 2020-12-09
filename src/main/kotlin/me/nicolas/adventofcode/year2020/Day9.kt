package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText

// --- Day 9: Encoding Error ---
// https://adventofcode.com/2020/day/9
fun main() {

    println("--- Day 9: Encoding Error ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day9/training.txt")
    val data = readFileDirectlyAsText("/year2020/day9/data.txt")

    val numbers = data.split("\n").map { str -> str.toLong() }

    // Part One
    val sum = partOne(numbers, preamble = 25)

    // Part Two
    partTwo(numbers, sum)
}

private fun partOne(numbers: List<Long>, preamble: Int): Long {
    var index = 0
    var wrongNumber: Long? = null

    while (index < numbers.size && wrongNumber == null) {
        val previous = numbers.subList(index, index + preamble)
        val nextNumbers = numbers.subList(index + preamble, numbers.size)

        val filter = previous.filter { nextNumbers[0] - it in previous && (nextNumbers[0] - it) * 2 != it }
        if (filter.isEmpty()) {
            wrongNumber = nextNumbers[0]
        }
        index++
    }

    println("Part one = $wrongNumber")

    return wrongNumber!!
}


private fun partTwo(numbers: List<Long>, sum: Long) {
    val list = numbers.subList(0, numbers.indexOf(sum))

    var index = 0
    do {
        var k = index + 1
        do {
            val contiguousRange = list.subList(index, k)
            if (contiguousRange.sum() == sum) {
                println("Part two = ${contiguousRange.minOf { it } + contiguousRange.maxOf { it }}")
                return // done -> exit partTwo
            }

            k++
        } while (k < list.size)

        index++
    } while (index < list.size)
}

