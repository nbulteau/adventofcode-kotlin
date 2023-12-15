package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day15/data.txt")
    val day = Day15(2023, 15, "Lens Library")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day15(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val initializationSequence = data.split(",")

        return initializationSequence.sumOf { string ->
            string.hash()
        }
    }

    private data class Lens(val label: String, val focalLength: Int)

    fun partTwo(data: String): Int {
        val initializationSequence = data.split(",").map { it }

        // 256 boxes, each box contains a list of lens (can be empty) with a label and a focal length
        val boxes = List(256) { mutableListOf<Lens>() }
        initializationSequence.forEach { step ->
            // Get the label of the lens
            val label = step.takeWhile { char -> char.isLetter() }
            val hash = label.hash() // 0..255 inclusive (256) possible values for hash
            val box = boxes[hash]
            // Get the operation to do
            val operation = step.dropWhile { char -> char.isLetter() }.take(1).first()

            when (operation) {
                '-' -> {
                    // If the label exists, remove the lens
                    val lensToRemove = box.find { lens -> lens.label == label }
                    box.remove(lensToRemove)
                }

                '=' -> {
                    // Get the value of the focal length
                    val value = step.dropWhile { char -> char.isLetter() }.drop(1).toInt()
                    val lens = Lens(label, value)
                    // If the label already exists, replace the lens, otherwise add it
                    val index = box.indexOfFirst { it.label == lens.label }
                    if (index == -1) {
                        box.add(lens)
                    } else {
                        box[index] = lens
                    }

                }
            }
        }

        // Sum of the focal length of each lens multiplied by the index of the box + 1 and the index of the lens + 1
        return boxes.withIndex().sumOf { (indexBoxes, listOfLens) ->
            listOfLens.withIndex().sumOf { (indexLens, lens) ->
                (indexBoxes + 1) * (indexLens + 1) * lens.focalLength
            }
        }
    }

    // Hash function for a string (used for the label) -> 0..255 inclusive (256) possible values for hash
    private fun String.hash() = this.map { char -> char.code }.fold(0) { acc, i -> ((acc + i) * 17) % 256 }
}

