package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2019/day/7
fun main() {

    val training = readFileDirectlyAsText("/year2019/day07/training.txt")
    val data = readFileDirectlyAsText("/year2019/day07/data.txt")

    val inputs = data.split(",").map { it.toInt() }.toIntArray()

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day07().partOne(inputs.clone()) })
    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day07().partTwo(inputs.clone()) })
}

private class Day07 {


    fun partOne(program: IntArray): Int {
        val permutations = listOf(0, 1, 2, 3, 4).permutations()

        val all = permutations.map { runPhase(program, it) }

        return all.maxOf { it }
    }

    fun partTwo(program: IntArray): Int {
        val permutations = listOf(5, 6, 7, 8, 9).permutations()

        var inputValue = 0
        permutations.map {
            inputValue = runAmplified(program, it, inputValue)
            inputValue
        }
        val all = permutations.map { runPhase(program, it) }

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


