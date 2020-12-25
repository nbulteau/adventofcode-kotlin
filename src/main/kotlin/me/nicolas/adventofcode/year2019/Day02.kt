package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.readFileDirectlyAsText

typealias Program = MutableMap<Int, Int>

// --- Day 2: 1202 Program Alarm ---
// https://adventofcode.com/2019/day/2
fun main() {

    println("--- Day 2: 1202 Program Alarm ---")
    println()

    val training = readFileDirectlyAsText("/year2019/day02/training.txt")
    val data = readFileDirectlyAsText("/year2019/day02/data.txt")

    var index = 0
    val program = data.split(",").map { string -> index++ to string.toInt() }.toMap().toMutableMap()

    // Part One
    partOne(program.toMutableMap())
    // Part Two
    partTwo(program.toMutableMap())
}

private fun partOne(program: Program) {

    program.execute(12, 2)

    println("Part one ${program[0]}")
}

private fun Program.execute(noun: Int, verb: Int) {
    this[1] = noun
    this[2] = verb

    var index = 0
    while (this[index] != 99) {
        when (this[index]) {
            1 -> {
                this[this[index + 3]!!] = this[this[index + 1]]!! + this[this[index + 2]]!!
            }
            2 -> {
                this[this[index + 3]!!] = this[this[index + 1]]!! * this[this[index + 2]]!!
            }
        }
        index += 4
    }
}

private fun partTwo(program: Program) {
    val expected = 19690720

    for (noun in 0..99) {
        for (verb in 0..99) {
            val programToTest: Program = program.toMutableMap()
            programToTest.execute(noun, verb)

            if (programToTest[0] == expected) {
                println("Part two ${100 * noun + verb}")
                return
            }
        }
    }
}
