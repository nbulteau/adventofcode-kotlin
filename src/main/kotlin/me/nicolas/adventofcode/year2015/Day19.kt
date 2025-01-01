package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// --- Day 19: Medicine for Rudolph ---
// https://adventofcode.com/2015/day/19
fun main() {
    val data = readFileDirectlyAsText("/year2015/day19/data.txt")
    val day = Day19(2015, 19)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day19(year: Int, day: Int, title: String = "Medicine for Rudolph") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val (replacements, molecule) = parseInput(data)

        // Calculates the number of distinct molecules that can be created after one replacement on the given medicine molecule.
        val distinct = mutableSetOf<String>()
        // Iterates over each replacement rule and applies it to all possible positions in the molecule.
        // Collects all distinct molecules generated from these replacements and returns their count.
        for (replacement in replacements) {
            var index = -1
            do {
                index = molecule.indexOf(replacement[0], startIndex = index + 1)
                if (index != -1) {
                    val newMolecule = molecule.substring(0, index) +
                            replacement[1] +
                            molecule.substring(startIndex = index + replacement[0].length)
                    distinct.add(newMolecule)
                }
            } while (index != -1)
        }
        return distinct.size
    }

    private class Step(var molecule: String, var repNum: Int, var ind: Int)

    /**
     * Had to look for a solution for part two because my solution was too slow.
     * Uses a simulation approach to apply replacements in reverse, starting from the target molecule and working towards "e".
     * Keeps track of the number of steps taken and returns the count when "e" is reached.
     * I also found a better solution than this one by looking at : https://www.happycoders.eu/algorithms/advent-of-code-2015
     */
    fun partTwo(data: String): Int {
        val (replacementRules, medicineMolecule) = parseInput(data)

        // Determines the fewest number of steps to go from the molecule "e" to the given medicine molecule.
        val replacements = replacementRules.toMutableList()
        while (true) {
            val path = mutableListOf(Step(medicineMolecule, 0, -1))
            var stepCount = 0
            // simulate the replacement process until we reach the molecule "e"
            for (count in 0..100000) {
                val step = path[stepCount]
                if (step.molecule == "e") {
                    return stepCount
                }
                val replacement = replacements[step.repNum]

                // find the next occurrence of the replacement
                step.ind = if (replacement[0] == "e" && replacement[1] != step.molecule) {
                    -1
                } else {
                    step.molecule.indexOf(replacement[1], startIndex = step.ind + 1)
                }

                // if we can't find a replacement, we have to try the next one
                if (step.ind == -1) {
                    step.repNum++
                    if (step.repNum >= replacements.size) {
                        stepCount--
                        if (stepCount <= 0) {
                            return -1
                        } else {
                            continue
                        }
                    }
                } else {
                    val m = step.molecule.substring(0, step.ind) +
                            replacement[0] +
                            step.molecule.substring(startIndex = step.ind + replacement[1].length)
                    stepCount++
                    val sNext = Step(m, 0, -1)
                    if (path.size <= stepCount) {
                        path.add(sNext)
                    } else {
                        path[stepCount] = sNext
                    }
                }
            }

            // if we reach this point, we have to start over with a new random order of replacements
            replacements.shuffle()
        }
    }


    private fun parseInput(data: String): Pair<List<List<String>>, String> {
        val input = data.lines()
        val replacements = input.take(input.size - 2).map { it.split(" => ") }
        val molecule = input.last()
        return Pair(replacements, molecule)
    }
}