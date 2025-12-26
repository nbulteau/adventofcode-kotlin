package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// --- Day 21: Springdroid Adventure ---
// https://adventofcode.com/2019/day/21
fun main() {
    val data = readFileDirectlyAsText("/year2019/day21/data.txt")
    val day = Day21(2019, 21)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day21(year: Int, day: Int, title: String = "Springdroid Adventure") : AdventOfCodeDay(year, day, title) {

    /**
     * Part One (WALK):
     *
     * This function builds a small springscript program that makes the droid jump when necessary
     * using only the available sensors A, B, C and D (ground at 1..4 tiles ahead). The chosen
     * logic is:
     *  - If any of A, B or C is a hole (so continuing forward would fall), the droid should jump
     *    but only if the landing tile D is ground (so it can land safely).
     *
     * The script implements: J = (not A OR not B OR not C) AND D
     * which is encoded with the available instructions using T as temporary storage.
     */
    fun partOne(data: String): Int {

        val script = listOf(
            "NOT A J",
            "NOT B T",
            "OR T J",
            "NOT C T",
            "OR T J",
            "AND D J"
        )

        return runSpringScript(data, script, "WALK")
    }

    /**
     * Part Two (RUN):
     *
     * With RUN mode the droid has extended sensors (A..I). The goal is to jump when needed but
     * also ensure that after landing there is a path that allows future movement/jumps. A common
     * safe strategy is to require that the landing tile D is ground and that either:
     *  - there is ground further ahead (H) to continue, or
     *  - some intermediate tiles ensure we won't immediately be forced into another impossible
     *    jump sequence. The script below extends the Part One logic by combining checks on E and H:
     *
     * Logical idea used here (expressed informally):
     *  J = ((not A) OR (not B) OR (not C)) AND D
     *  then J = J AND (E OR H)  // ensure there's a viable continuation after landing
     *
     * The implemented script uses T as temporary and ends with RUN.
     */
    fun partTwo(data: String): Int {

        val script = listOf(
            "NOT A J",
            "NOT B T",
            "OR T J",
            "NOT C T",
            "OR T J",
            "AND D J",
            "NOT E T",
            "NOT T T",
            "OR H T",
            "AND T J"
        )

        return runSpringScript(data, script, "RUN")
    }

    private fun runSpringScript(data: String, scriptLines: List<String>, runCommand: String): Int {
        val program = data.trim().split(',').map { it.toLong() }
        val computer = IntCodeProgram(program)

        val inputs = mutableListOf<Long>()
        // append each script line + newline
        scriptLines.forEach { line ->
            line.forEach { ch -> inputs.add(ch.code.toLong()) }
            inputs.add(10L)
        }
        // append run command + newline
        runCommand.forEach { ch -> inputs.add(ch.code.toLong()) }
        inputs.add(10L)

        val outputs = computer.execute(inputs)

        if (outputs.isEmpty()) return 0
        val last = outputs.last()
        return if (last > 255L) last.toInt() else 0
    }
}