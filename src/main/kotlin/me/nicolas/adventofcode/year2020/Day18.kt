package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.math.BigInteger
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 18: Operation Order ---
// https://adventofcode.com/2020/day/18
@ExperimentalTime
fun main() {

    println("--- Day 18: Operation Order ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day18/training.txt")
    val data = readFileDirectlyAsText("/year2020/day18/data.txt")

    val expressions = data.split("\n")

    // Part One
    Day18().partOne(expressions)

    // Part Two
    val duration = measureTime { Day18().partTwo(expressions) }
    println("Part two duration : $duration")
}

private class Day18 {

    fun partOne(expressions: List<String>) {

        // operators don't have priority
        val priority = mapOf(";" to 0, "(" to 1, ")" to 2, "*" to 3, "+" to 3)

        val result = expressions
            .map { expression -> BigInteger.valueOf(solve(expression, priority)) }
            .reduce { acc: BigInteger, bigInteger -> acc + bigInteger }

        println("Part one = $result")
    }

    fun partTwo(expressions: List<String>) {

        // addition is evaluated before multiplication
        val priority = mapOf(";" to 0, "(" to 1, ")" to 2, "*" to 3, "+" to 4)

        val result = expressions
            .map { expression -> BigInteger.valueOf(solve(expression, priority)) }
            .reduce { acc: BigInteger, bigInteger -> acc + bigInteger }

        println("Part two = $result")
    }

    private fun solve(expression: String, priority: Map<String, Int>): Long {

        val values = Stack<Long>()
        val operands = Stack<String>()

        // Add ';' as an operator to end the expression
        val symbols = ("$expression;").replace(" ", "").chunked(1)

        for (symbol in symbols) {
            if (priority.containsKey(symbol)) {
                while (symbol != "(" && operands.isNotEmpty() && priority[operands.peek()]!! >= priority[symbol]!!) {
                    val right = values.pop()
                    val left = values.pop()

                    val result = if (operands.pop() == "+") {
                        right + left
                    } else {
                        right * left
                    }
                    values.push(result)
                }

                if (symbol == ")") {
                    operands.pop()
                } else {
                    operands.push(symbol)
                }
            } else {
                values.push(symbol.toLong())
            }
        }
        return values.pop()
    }
}