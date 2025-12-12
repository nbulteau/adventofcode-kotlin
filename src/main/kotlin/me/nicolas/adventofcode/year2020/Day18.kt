package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.math.BigInteger
import java.util.*

// --- Day 18: Operation Order ---
// https://adventofcode.com/2020/day/18
fun main() {
    val data = readFileDirectlyAsText("/year2020/day18/data.txt")
    val day = Day18(2020, 18, "Operation Order")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day18(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): BigInteger {
        val expressions = data.split("\n").filter { it.isNotEmpty() }
        // operators don't have priority
        val priority = mapOf(";" to 0, "(" to 1, ")" to 2, "*" to 3, "+" to 3)
        return expressions
            .map { expression -> BigInteger.valueOf(solve(expression, priority)) }
            .reduce { acc: BigInteger, bigInteger -> acc + bigInteger }
    }

    fun partTwo(data: String): BigInteger {
        val expressions = data.split("\n").filter { it.isNotEmpty() }
        // addition is evaluated before multiplication
        val priority = mapOf(";" to 0, "(" to 1, ")" to 2, "*" to 3, "+" to 4)
        return expressions
            .map { expression -> BigInteger.valueOf(solve(expression, priority)) }
            .reduce { acc: BigInteger, bigInteger -> acc + bigInteger }
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