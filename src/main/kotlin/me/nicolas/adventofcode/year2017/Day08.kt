package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 8: I Heard You Like Registers ---
// https://adventofcode.com/2017/day/8
fun main() {
    val data = readFileDirectlyAsText("/year2017/day08/data.txt")
    val day = Day08()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day08(year: Int = 2017, day: Int = 8, title: String = "I Heard You Like Registers") : AdventOfCodeDay(year, day, title) {

    /**
     * Part one: execute instructions and return the largest value in any register after completion.
     */
    fun partOne(data: String): Int {
        val registers = mutableMapOf<String, Int>()

        for (instr in parseInstructions(data)) {
            if (instr.condition.evaluate(registers)) {
                val current = registers.getOrDefault(instr.target, 0)
                val updated = instr.op.apply(current, instr.amount)
                registers[instr.target] = updated
            }
        }

        return registers.values.maxOrNull() ?: 0
    }

    /**
     * Part two: track the highest value held in any register at any point during execution.
     */
    fun partTwo(data: String): Int {
        val registers = mutableMapOf<String, Int>()
        var maxEver: Int? = null

        for (instr in parseInstructions(data)) {
            if (instr.condition.evaluate(registers)) {
                val current = registers.getOrDefault(instr.target, 0)
                val updated = instr.op.apply(current, instr.amount)
                registers[instr.target] = updated
                maxEver = if (maxEver == null) updated else maxOf(maxEver, updated)
            }
        }

        return maxEver ?: 0
    }

    // Regex to parse an instruction line into groups
    private val instructionRegex = Regex("""^(\w+)\s+(inc|dec)\s+(-?\d+)\s+if\s+(\w+)\s+([!<>=]{1,2})\s+(-?\d+)$""")

    // Domain types to represent parsed instructions and conditions
    private data class Instruction(
        val target: String,
        val op: Operator,
        val amount: Int,
        val condition: Condition
    )

    private data class Condition(val register: String, val operator: Operator.Companion.ConditionOp, val value: Int) {
        // Evaluate the condition against the current register state
        fun evaluate(registers: Map<String, Int>): Boolean {
            val regVal = registers.getOrDefault(register, 0)
            return operator.eval(regVal, value)
        }
    }

    private enum class Operator {
        INC, DEC;

        fun apply(current: Int, amount: Int): Int = when (this) {
            INC -> current + amount
            DEC -> current - amount
        }

        companion object {
            fun fromString(s: String): Operator = when (s) {
                "inc" -> INC
                "dec" -> DEC
                else -> throw IllegalArgumentException("Unknown operator: $s")
            }

            // Condition operators used for if-clauses
            enum class ConditionOp {
                GT, LT, GTE, LTE, EQ, NEQ;

                fun eval(a: Int, b: Int): Boolean = when (this) {
                    GT -> a > b
                    LT -> a < b
                    GTE -> a >= b
                    LTE -> a <= b
                    EQ -> a == b
                    NEQ -> a != b
                }

                companion object {
                    fun fromString(s: String): ConditionOp = when (s) {
                        ">" -> GT
                        "<" -> LT
                        ">=" -> GTE
                        "<=" -> LTE
                        "==" -> EQ
                        "!=" -> NEQ
                        else -> throw IllegalArgumentException("Unknown condition operator: $s")
                    }
                }
            }
        }
    }

    // Parse input text into a list of Instruction objects. Lines that don't match the expected
    // format are ignored.
    private fun parseInstructions(data: String): Sequence<Instruction> =
        data.lineSequence()
            .mapNotNull { instructionRegex.matchEntire(it.trim()) }
            .map { m ->
                val target = m.groupValues[1]
                val op = Operator.fromString(m.groupValues[2])
                val amount = m.groupValues[3].toInt()
                val condReg = m.groupValues[4]
                val condOp = Operator.Companion.ConditionOp.fromString(m.groupValues[5])
                val condVal = m.groupValues[6].toInt()
                Instruction(target, op, amount, Condition(condReg, condOp, condVal))
            }
}
