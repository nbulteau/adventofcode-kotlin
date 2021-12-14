package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2019/day/7
@OptIn(ExperimentalTime::class)
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

    fun partOne(inputs: IntArray): Int {
        (0..99999).forEach { int ->

        }
        return compute(inputs)
    }

    private fun compute(program: IntArray): Int {
        val permutations = generatePermutationsIterative(5)

        val all = permutations.map { runPhase(program, it) }

        return all.maxOf { it }
    }

    fun partTwo(program: IntArray): Int {


        return 0
    }

    private fun runPhase(program: IntArray, settings: IntArray): Int =
        (0..4).fold(0) { previous, id ->
            IntCodeProgram(program.clone()).execute(mutableListOf(settings[id], previous)).first()
        }

    private fun generatePermutationsIterative(size: Int): Set<IntArray> {

        val permutations = mutableSetOf<IntArray>()

        val figures = (0..size).toList().toIntArray()
        val factorials = IntArray(size + 1)
        factorials[0] = 1
        for (i in 1..size) {
            factorials[i] = factorials[i - 1] * i
        }
        for (i in 0 until factorials[size]) {
            var onePermutation = IntArray(0)
            var temp = figures
            var positionCode = i
            for (position in size downTo 1) {
                val selected = positionCode / factorials[position - 1]
                onePermutation += temp[selected]
                positionCode %= factorials[position - 1]
                temp = temp.sliceArray(0 until selected) + temp.sliceArray(selected + 1 until temp.size)
            }

            permutations.add(onePermutation)
        }

        return permutations
    }


}


