package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day21/training.txt")
    val data = readFileDirectlyAsText("/year2022/day21/data.txt")

    val lines = data.split("\n")

    val day = Day21(2022, 20, "Monkey Math", lines)
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo() }

}

private class Day21(year: Int, day: Int, title: String, lines: List<String>) : AdventOfCodeDay(year, day, title) {

    companion object {
        lateinit var monkeys: Map<String, Monkey>
    }

    init {
        buildMonkeys(lines)
    }

    sealed class Monkey {

        abstract fun value(): Long

        data class MonkeyOperator(val operator: Char, val left: String, val right: String) : Monkey() {
            var value: Long? = null

            override fun value(): Long {
                return value ?: when (operator) {
                    '+' -> monkeys[left]!!.value() + monkeys[right]!!.value()
                    '-' -> monkeys[left]!!.value() - monkeys[right]!!.value()
                    '/' -> monkeys[left]!!.value() / monkeys[right]!!.value()
                    '*' -> monkeys[left]!!.value() * monkeys[right]!!.value()
                    else -> throw RuntimeException()
                }
            }
        }

        data class MonkeyValue(val value: Long) : Monkey() {
            override fun value(): Long {
                return value
            }
        }
    }

    fun partOne(): Long {
        return monkeys["root"]!!.value()
    }

    fun partTwo(): Long {
        // wrong : 9879574614298 (too high)

        val root = monkeys["root"] as Monkey.MonkeyOperator
        return if (monkeys.containsHumn(root.left)) {
            // left subtree contains humn -monkey > resolve right subTree
            val value = monkeys[root.right]!!.value()
            println("right value : $value")
            (monkeys[root.left] as Monkey.MonkeyOperator).recurseSolve(value, monkeys)
        } else {
            // right subtree contains humn monkey -> resolve left subTree
            val value = monkeys[root.left]!!.value()
            println("left value : $value resole left")
            (monkeys[root.right] as Monkey.MonkeyOperator).recurseSolve(value, monkeys)
        }
    }

    fun Map<String, Monkey>.containsHumn(name: String): Boolean {
        // this is Humn !
        if (name == "humn") {
            return true
        }
        return when (val monkey = this[name]!!) {
            is Monkey.MonkeyValue -> false
            is Monkey.MonkeyOperator -> {
                containsHumn(monkey.left) || containsHumn(monkey.right)
            }
        }
    }

    fun Monkey.MonkeyOperator.recurseSolve(result: Long, subtree: Map<String, Monkey>): Long {
        return if (subtree.containsHumn(this.left)) {
            // Left subtree contains humn monkey
            val leftSubtree = subtree[left]
            if (this.left == "humn") {
                processLeftOperand(operator, result, subtree[right]!!.value())
            } else if (leftSubtree is Monkey.MonkeyValue) {
                leftSubtree.value
            } else {
                (leftSubtree as Monkey.MonkeyOperator)
                    .recurseSolve(processLeftOperand(operator, result, subtree[right]!!.value()), subtree)
            }
        } else {
            // Right subtree contains humn monkey
            val rightSubtree = subtree[right]
            if (this.right == "humn") {
                processRightOperand(operator, result, subtree[left]!!.value())
            } else if (rightSubtree is Monkey.MonkeyValue) {
                rightSubtree.value
            } else {
                (rightSubtree as Monkey.MonkeyOperator)
                    .recurseSolve(processRightOperand(operator, result, subtree[left]!!.value()), subtree)
            }
        }
    }

    // Perform operation in reverse, using known value
    // result = left / right =>  left = result * right
    fun processLeftOperand(operator: Char, result: Long, rightOperand: Long) =
        when (operator) {
            '+' -> result - rightOperand
            '-' -> result + rightOperand
            '*' -> result / rightOperand
            '/' -> result * rightOperand
            else -> throw RuntimeException()
        }

    fun processRightOperand(operator: Char, result: Long, leftOperand: Long) =
        when (operator) {
            '+' -> result - leftOperand
            // subtraction isn't commutative ! result = left - right => left = result + right and right = left - result
            '-' -> leftOperand - result
            '*' -> result / leftOperand
            '/' -> result * leftOperand
            else -> throw RuntimeException()
        }

    private fun buildMonkeys(lines: List<String>) {
        val regexOperator = Regex("([a-z]{4}): ([a-z]{4}) (.) ([a-z]{4})")
        val regexValue = Regex("([a-z]{4}): (\\d{1,4})")

        monkeys = lines.associate { line ->
            if ('+' in line || '-' in line || '*' in line || '/' in line) {
                val (name, left, operator, right) = regexOperator.matchEntire(line)!!.groupValues.drop(1).map { it }
                name to Monkey.MonkeyOperator(operator.first(), left, right)
            } else {
                val (name, value) = regexValue.matchEntire(line)!!.groupValues.drop(1).map { it }
                name to Monkey.MonkeyValue(value.toLong())
            }
        }
    }
}
 
