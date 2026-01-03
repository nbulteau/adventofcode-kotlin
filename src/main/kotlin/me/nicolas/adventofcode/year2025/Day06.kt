package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 6: Trash Compactor ---
// https://adventofcode.com/2025/day/6
fun main() {
    val data = readFileDirectlyAsText("/year2025/day06/data.txt")
    val day = Day06(2025, 6)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}


class Day06(year: Int, day: Int, title: String = "Trash Compactor") : AdventOfCodeDay(year, day, title) {

    /**
     * Solves the math worksheet by reading problems left-to-right in columns.
     * Each column represents one problem with numbers vertically arranged and
     * an operator at the bottom.
     */
    fun partOne(data: String): Long {
        // Parse worksheet into a 2D structure, filtering out empty tokens
        val worksheet = data.lines().map { line -> line.split(' ').filter { line -> line.isNotEmpty() } }
        val rowCount = worksheet.size - 1 // Exclude operator row
        val columnCount = worksheet[0].size

        // Solve each problem (column-wise)
        val problemResults = (0 until columnCount).map { columnIndex ->
            val operator = worksheet.last()[columnIndex]
            // Extract all numbers in this column from top to bottom
            val operands = (0 until rowCount).map { rowIndex -> worksheet[rowIndex][columnIndex].toLong() }
            when (operator) {
                "+" -> operands.reduce { acc, value -> Math.addExact(acc, value) }
                "*" -> operands.reduce { acc, value -> Math.multiplyExact(acc, value) }
                else -> throw NotImplementedError(operator)
            }
        }

        // Return the grand total of all problem results
        return problemResults.sum()
    }

    // Data class to represent a problem's operator and its column range
    private data class ProblemRange(val operator: Char, val startIndex: Int, val endIndex: Int)

    /**
     * Solves the math worksheet using cephalopod math (right-to-left reading).
     * Each number is written vertically with the most significant digit at the top.
     * Problems are separated by blank columns.
     */
    fun partTwo(data: String): Long {
        val worksheet = data.lines()
        val rowCount = worksheet.size - 1 // Exclude operator row
        val maxWidth = worksheet.maxOf { line -> line.length } // not all lines have the same length
        val operatorRow = worksheet.last()

        // Extract problem ranges by finding operators and their associated column ranges
        val operatorIndices = operatorRow.withIndex()
            .filter { (_, char) -> char in "+*" }
            .map { (index, _) -> index }

        val problems = operatorIndices.mapIndexed { index, startIndex ->
            val operator = operatorRow[startIndex]
            // The end index for the current problem range (next operator's index or the maximum worksheet width if this is the last operator)
            val endIndex = operatorIndices.getOrNull(index + 1) ?: maxWidth

            ProblemRange(operator, startIndex, endIndex)
        }

        // Process each problem by mapping over the ranges
        return problems.sumOf { (operator, startIndex, endIndex) ->
            // Extract all vertical numbers for this problem
            val operands = (startIndex until endIndex).mapNotNull { columnIndex ->
                // Read digits vertically from top to bottom to form a number
                val verticalNumber = String(CharArray(rowCount) { rowIndex ->
                    worksheet[rowIndex].getOrNull(columnIndex) ?: ' '
                })

                if (verticalNumber.isBlank()) {
                    null // Skip empty columns
                } else {
                    verticalNumber.trim().toLong()
                }
            }

            // Calculate result for this problem
            when (operator) {
                '+' -> operands.sum() // reduce { acc, value -> Math.addExact(acc, value) }
                '*' -> operands.fold(1L, Long::times) // reduce { acc, value -> Math.multiplyExact(acc, value) }
                else -> throw NotImplementedError("$operator")
            }
        }
    }
}

