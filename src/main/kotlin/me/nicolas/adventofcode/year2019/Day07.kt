package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2019/day/7

fun main() {
    val data = readFileDirectlyAsText("/year2019/day07/data.txt")
    val day = Day07(2019, 7)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07(year: Int, day: Int, title: String = "Amplification Circuit") : AdventOfCodeDay(year, day, title) {

    /**
     * Part One: Amplifiers in series configuration
     *
     * Five amplifiers are connected in a chain: A -> B -> C -> D -> E
     * Each amplifier receives a phase setting (0-4, each used once) and an input signal.
     * The output from one amplifier becomes the input to the next.
     * Starting with input signal 0, find the phase setting sequence that produces
     * the highest output signal from amplifier E.
     */
    fun partOne(data: String): Long {
        val program = data.split(",").map { it.toLong() }

        // Generate all possible permutations of phase settings [0, 1, 2, 3, 4]
        val permutations = listOf(0, 1, 2, 3, 4).permutations()

        // Run each permutation through the amplifier chain
        val all = permutations.map { runPhase(program, it) }

        // Return the maximum thruster signal across all permutations
        return all.maxOf { it }
    }

    /**
     * Part Two: Amplifiers in feedback loop configuration
     *
     * Five amplifiers are connected in a feedback loop: A -> B -> C -> D -> E -> A (loops back)
     * Each amplifier receives a phase setting (5-9, each used once).
     * Unlike Part One, amplifiers run concurrently and maintain their state:
     * - Each amplifier executes until it produces an output, then pauses
     * - The output feeds into the next amplifier's input queue
     * - The process continues in a loop until all amplifiers halt
     * - The final output from amplifier E (before all halt) is the thruster signal
     *
     * Find the phase setting sequence that produces the highest thruster signal.
     */
    fun partTwo(data: String): Long {
        val program = data.split(",").map { it.toLong() }

        // Generate all possible permutations of phase settings [5, 6, 7, 8, 9]
        val permutations = listOf(5, 6, 7, 8, 9).permutations()

        // Run each permutation through the feedback loop
        val all = permutations.map { runFeedbackLoop(program, it) }

        // Return the maximum thruster signal across all permutations
        return all.maxOf { it }
    }

    private fun runFeedbackLoop(program: List<Long>, settings: List<Int>): Long {
        // Create 5 amplifiers
        val amplifiers = List(5) { IntCodeProgram(program) }

        // Initialize input queues with phase settings
        val inputQueues = settings.map { mutableListOf(it.toLong()) }.toMutableList()

        // Start with 0 as input to the first amplifier
        var signal = 0L

        // Keep running until all amplifiers halt
        while (!amplifiers.all { it.isHalted() }) {
            for (i in 0..4) {
                if (!amplifiers[i].isHalted()) {
                    inputQueues[i].add(signal)
                    val output = amplifiers[i].executeUntilOutput(inputQueues[i])
                    if (output != null) {
                        signal = output
                    }
                }
            }
        }

        return signal
    }

    private fun runPhase(program: List<Long>, settings: List<Int>): Long =
        (0..4).fold(0L) { previous, id ->
            IntCodeProgram(program).execute(mutableListOf(settings[id].toLong(), previous)).first()
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


