package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 8: Handheld Halting ---
// https://adventofcode.com/2020/day/8
fun main() {

    val training = readFileDirectlyAsText("/year2020/day08/training.txt")
    val data = readFileDirectlyAsText("/year2020/day08/data.txt")

    val instructions = data.split("\n")

    // Part One
    partOne(instructions)

    // Part Two
    partTwo(instructions)
}

private fun partOne(instructions: List<String>) {

    val (accumulator, _) = instructionsEngine(instructions)

    println("Part one = $accumulator")
}


private fun partTwo(instructions: List<String>) {

    var index = 0
    var accumulator = 0
    do {
        val modifiedInstructions = instructions.toMutableList()

        // By changing exactly one jmp or nop, try to repair the boot code and make it terminate correctly.
        if (modifiedInstructions[index].startsWith("nop")) {
            modifiedInstructions[index] = modifiedInstructions[index].replace("nop", "jmp")
        } else {
            modifiedInstructions[index] = modifiedInstructions[index].replace("jmp", "nop")
        }

        val (result, notALoop) = instructionsEngine(modifiedInstructions)
        accumulator = result
        index++
    } while (!notALoop && index < instructions.size)

    println("Part two = $accumulator")
}

private fun instructionsEngine(instructions: List<String>): Pair<Int, Boolean> {

    var index = 0
    var accumulator = 0
    val alreadyVisited = BooleanArray(instructions.size)

    do {
        alreadyVisited[index] = true
        val instruction = instructions[index]
        when {
            instruction.startsWith("nop") -> index++
            instruction.startsWith("acc") -> {
                accumulator += instruction.substringAfter(" ").toInt()
                index++
            }

            instruction.startsWith("jmp") -> {
                index += instruction.substringAfter(" ").toInt()
            }
        }
    } while (index < instructions.size && !alreadyVisited[index])

    // The program is supposed to terminate by attempting to execute an instruction immediately after the last instruction in the file.
    return Pair(accumulator, index >= instructions.size)
}
