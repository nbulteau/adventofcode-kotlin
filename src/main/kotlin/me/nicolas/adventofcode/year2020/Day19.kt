package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 19: Monster Messages ---
// https://adventofcode.com/2020/day/19
@ExperimentalTime
fun main() {

    println("--- Day 19: Monster Messages ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day19/training.txt")
    val data = readFileDirectlyAsText("/year2020/day19/data.txt")

    val sections = data.split("\n\n")
    val rules = extractRules(sections)
    val messages = extractMessages(sections)

    // Part One
    Day19().partOne(rules, messages)


    // Part Two
    val duration = measureTime { Day19().partTwo(rules, messages) }
    println("Part two duration : $duration")
}

fun extractMessages(sections: List<String>): List<String> {
    return sections[1].split("\n")
}

private sealed class Rule

private data class RuleMatch(val match: String) : Rule()
private data class RuleExpr(val expr: List<Int>) : Rule()
private data class RuleOr(val left: RuleExpr, val right: RuleExpr) : Rule()

private fun extractRules(sections: List<String>): Map<Int, Rule> {
    return sections[0]
        .split("\n")
        .map { line ->
            val index = line.substringBefore(": ").toInt()
            val rule = line.substringAfter(": ").run {
                when {
                    line.contains("|") -> {
                        val parts = this.split(" | ")
                        RuleOr(
                            RuleExpr(parts[0].split(" ").map { it.toInt() }),
                            RuleExpr(parts[1].split(" ").map { it.toInt() })
                        )
                    }

                    line.contains("\"") -> {
                        RuleMatch(this.replace("\"", ""))
                    }

                    else -> {
                        RuleExpr(this.split(" ").map { it.toInt() })
                    }
                }
            }
            index to rule
        }.toMap()
}


private class Day19 {

    fun createRegex(rules: MutableMap<Int, Rule>, ruleIndex: Int): String {

        return when (val rule = rules[ruleIndex]) {
            is RuleMatch -> rule.match
            is RuleExpr -> rule.expr.joinToString("") { createRegex(rules, it) }
            is RuleOr -> {
                val left = rule.left.expr.joinToString("") { createRegex(rules, it) }
                val right = rule.right.expr.joinToString("") { createRegex(rules, it) }

                "(?:${left}|${right})"
            }

            else -> throw RuntimeException()
        }
    }

    fun matchesRules(rules: Map<Int, Rule>, message: String, rulesToVisit: List<Int>): Boolean {

        if (message.isEmpty()) {
            return rulesToVisit.isEmpty()
        }
        if (rulesToVisit.isEmpty()) {
            return false
        }

        return when (val rule = rules[rulesToVisit.first()]) {
            is RuleMatch -> if (message.startsWith(rule.match)) {
                matchesRules(rules, message.drop(1), rulesToVisit.drop(1))
            } else {
                false
            }

            is RuleExpr -> matchesRules(rules, message, rule.expr + rulesToVisit.drop(1))
            is RuleOr -> matchesRules(rules, message, rule.left.expr + rulesToVisit.drop(1))
                    || matchesRules(rules, message, rule.right.expr + rulesToVisit.drop(1))

            else -> throw RuntimeException()
        }
    }

    // With regex
    fun partOne(rules: Map<Int, Rule>, expressions: List<String>) {

        val regex = Regex("^${createRegex(rules.toMutableMap(), 0)}$")

        //println(regex)
        val result = expressions.count { expression -> regex.matches(expression) }
        println("Part two = $result")
    }

    // Without regex
    fun partTwo(rules: Map<Int, Rule>, expressions: List<String>) {

        val newRules = rules.toMutableMap()
        newRules[8] = RuleOr(RuleExpr(listOf(42)), RuleExpr(listOf(42, 8)))
        newRules[11] = RuleOr(RuleExpr(listOf(42, 31)), RuleExpr(listOf(42, 11, 31)))

        val result = expressions.count { expression -> matchesRules(newRules, expression, listOf(0)) }
        println("Part two = $result")
    }
}