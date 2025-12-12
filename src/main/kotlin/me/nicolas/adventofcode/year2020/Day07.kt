package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 07: Handy Haversacks ---
// https://adventofcode.com/2020/day/7
fun main() {
    val data = readFileDirectlyAsText("/year2020/day07/data.txt")
    val day = Day07(2020, 7, "Handy Haversacks")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val rules = parseRules(data.split("\n").filter { it.isNotEmpty() })
        return rules.keys.count { canContain("shiny gold", it, rules) }
    }

    fun partTwo(data: String): Int {
        val rules = parseRules(data.split("\n").filter { it.isNotEmpty() })
        return countBags("shiny gold", rules)
    }

    private fun parseRules(bags: List<String>): Map<String, Map<String, Int>> {
        val matchResult = Regex("""(\d+) ([a-z ]+) bags?""")
        return bags.associate { line ->
            val parts = line.split(" bags contain ")
            val bagName = parts[0]
            val content = parts[1]
            val innerBags = if (content == "no other bags.") {
                emptyMap()
            } else {
                matchResult.findAll(content).associate {
                    val (count, color) = it.destructured
                    color to count.toInt()
                }
            }
            bagName to innerBags
        }
    }

    private fun canContain(target: String, bag: String, rules: Map<String, Map<String, Int>>): Boolean {
        val contents = rules[bag] ?: return false
        if (target in contents) return true
        return contents.keys.any { canContain(target, it, rules) }
    }

    private fun countBags(bag: String, rules: Map<String, Map<String, Int>>): Int {
        val contents = rules[bag] ?: return 0
        return contents.entries.sumOf { (color, count) ->
            count * (1 + countBags(color, rules))
        }
    }
}
