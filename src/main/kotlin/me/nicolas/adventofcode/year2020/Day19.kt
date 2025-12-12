package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 19: Monster Messages ---
// https://adventofcode.com/2020/day/19
fun main() {
    val data = readFileDirectlyAsText("/year2020/day19/data.txt")
    val day = Day19(2020, 19, "Monster Messages")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day19(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val (rules, messages) = parseData(data)
        val regex = Regex("^${createRegex(rules.toMutableMap(), 0)}$")
        return messages.count { message -> regex.matches(message) }
    }

    fun partTwo(data: String): Int {
        val (rules, messages) = parseData(data)
        val newRules = rules.toMutableMap()
        newRules[8] = RuleOr(RuleExpr(listOf(42)), RuleExpr(listOf(42, 8)))
        newRules[11] = RuleOr(RuleExpr(listOf(42, 31)), RuleExpr(listOf(42, 11, 31)))
        return messages.count { expression -> matchesRules(newRules, expression, listOf(0)) }
    }

    private fun parseData(data: String): Pair<Map<Int, Rule>, List<String>> {
        val sections = data.split("\n\n")
        val rules = extractRules(sections)
        val messages = extractMessages(sections)
        return Pair(rules, messages)
    }

    private fun extractMessages(sections: List<String>): List<String> {
        return sections[1].split("\n").filter { it.isNotEmpty() }
    }

    private sealed class Rule
    private data class RuleMatch(val match: String) : Rule()
    private data class RuleExpr(val expr: List<Int>) : Rule()
    private data class RuleOr(val left: RuleExpr, val right: RuleExpr) : Rule()

    private fun extractRules(sections: List<String>): Map<Int, Rule> {
        return sections[0]
            .split("\n").associate { line ->
                val index = line.substringBefore(": ").toInt()
                val rule = line.substringAfter(": ").run {
                    when {
                        contains("|") -> {
                            val parts = this.split(" | ")
                            RuleOr(
                                RuleExpr(parts[0].split(" ").map { it.toInt() }),
                                RuleExpr(parts[1].split(" ").map { it.toInt() })
                            )
                        }
                        contains("\"") -> RuleMatch(this.replace("\"", ""))
                        else -> RuleExpr(this.split(" ").map { it.toInt() })
                    }
                }
                index to rule
            }
    }

    private fun createRegex(rules: MutableMap<Int, Rule>, ruleIndex: Int): String {
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

    private fun matchesRules(rules: Map<Int, Rule>, message: String, rulesToVisit: List<Int>): Boolean {
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
}