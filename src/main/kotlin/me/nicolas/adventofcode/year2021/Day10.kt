package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.prettyPrint
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2021/day/10
@ExperimentalTime
fun main() {


    val training = readFileDirectlyAsText("/year2021/day10/training.txt")
    val data = readFileDirectlyAsText("/year2021/day10/data.txt")

    val lines = data.split("\n")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day10().partOne(lines) })

    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day10().partTwo(lines) })
}

private class Day10 {

    companion object {
        val IN_CHUNK = listOf('(', '[', '{', '<')
        val OUT_CHUNK = listOf(')', ']', '}', '>')
    }

    val partOneValues = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val partTwoValues = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)


    fun partOne(lines: List<String>): Int {

        val illegalCharacters = lines.map { line -> getIllegalCharacter(line) }

        // score
        return OUT_CHUNK.sumOf { char ->
            illegalCharacters.count { it == char } * partOneValues[char]!!
        }
    }

    fun partTwo(lines: List<String>): Long {
        val incompleteLines = lines.filter { !isCorruptedLine(it) }
        val completionStrings = incompleteLines.map { line -> getCompletionString(line) }

        // score
        val scores = completionStrings.map { completion ->
            completion.toCharArray().fold(0L) { score, char ->
                (score * 5) + partTwoValues[char]!!
            }
        }.sorted()

        return scores[scores.size / 2]
    }

    private fun getCompletionString(line: String): String {
        val stack = Stack<Char>()
        for (found in line.toCharArray()) {
            if (found in IN_CHUNK) {
                stack.push(found)
            } else {
                stack.pop()
            }
        }
        return stack.map { OUT_CHUNK[IN_CHUNK.indexOf(it)] }.reversed().joinToString("")
    }

    private fun getIllegalCharacter(line: String): Char? {
        val stack = Stack<Char>()
        for (found in line.toCharArray()) {
            if (found in IN_CHUNK) {
                stack.push(found)
            } else if (found in OUT_CHUNK) {
                if (expected(stack) != found) {
                    return found
                }
            }
        }
        return null
    }

    private fun isCorruptedLine(line: String): Boolean {
        val stack = Stack<Char>()
        for (found in line.toCharArray()) {
            if (found in IN_CHUNK) {
                stack.push(found)
            } else if (found in OUT_CHUNK) {
                if (expected(stack) != found) {
                    return true
                }
            }
        }
        return false
    }

    private fun expected(stack: Stack<Char>) = OUT_CHUNK[IN_CHUNK.indexOf(stack.pop())]
}

