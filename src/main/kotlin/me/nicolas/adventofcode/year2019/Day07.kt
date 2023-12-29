package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2019/day/7

fun main() {
    val data = readFileDirectlyAsText("/year2019/day07/data.txt")
    val day = Day07(2019, 7, "Amplification Circuit")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val program = data.split(",").map { it.toInt() }.toIntArray()

        val permutations = listOf(0, 1, 2, 3, 4).permutations()

        val all = permutations.map { runPhase(program, it) }

        return all.maxOf { it }
    }

    fun partTwo(data: String): Int {

        val program = data.split(",").map { it.toInt() }.toIntArray()

        val permutations = listOf(5, 6, 7, 8, 9).permutations()

        var inputValue = 0
        permutations.map {
            inputValue = runAmplified(program, it, inputValue)
            inputValue
        }
        val all = permutations.map { runPhase(program, it) }

        // find the largest output signal that can be sent to the thrusters by trying every combination of phase settings on the amplifiers
        return all.maxOf { it }
    }

    private fun runPhase(program: IntArray, settings: List<Int>): Int =
        (0..4).fold(0) { previous, id ->
            IntCodeProgram(program.clone()).execute(mutableListOf(settings[id], previous)).first()
        }

    private fun runAmplified(program: IntArray, settings: List<Int>, firstInputValue: Int): Int =
        (0..4).fold(firstInputValue) { previous, id ->
            IntCodeProgram(program.clone()).execute(mutableListOf(settings[id], previous)).first()
        }

    // Permutations of a list of integers (https://rosettacode.org/wiki/Permutations#Kotlin)
    private fun List<Int>.permutations(): List<List<Int>> =
        if (this.size <= 1) {
            listOf(this)
        } else {
            val elementToInsert = first()
            drop(1).permutations().flatMap { permutation ->
                (0..permutation.size).map { i ->
                    permutation.toMutableList().apply { add(i, elementToInsert) }
                }
            }
        }
}


