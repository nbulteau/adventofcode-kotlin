package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.*
import kotlin.collections.ArrayDeque

// --- Day 5: ---
// https://adventofcode.com/2024/day/5
fun main() {
    val data = readFileDirectlyAsText("/year2024/day05/data.txt")
    val day = Day05(2024, 5, "")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day05(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {

        val (rules, manuals) = extractRulesAndManuals(data)

        return manuals.filter { manual -> manual.isValid(rules) }.sumOf { it -> it[it.size / 2] }
    }


    fun partTwo(data: String): Int {
        val (rules, manuals) = extractRulesAndManuals(data)

        fun List<Int>.reorder(): List<Int> {
            val orderingGraph = mutableMapOf<Int, MutableList<Int>>()
            val inDegree = mutableMapOf<Int, Int>()

            rules.forEach { (left, right) ->
                if (left in this && right in this) {
                    orderingGraph.computeIfAbsent(left) { mutableListOf() }.add(right)
                    inDegree[right] = inDegree.getOrDefault(right, 0) + 1
                    inDegree.putIfAbsent(left, 0)
                }
            }

            val zeroInDegree =  ArrayDeque(inDegree.filter { (_, degree) ->  degree == 0 }.map {(page, _) ->  page  })

            val sortedOrder = mutableListOf<Int>()
            while (zeroInDegree.isNotEmpty()) {
                val page = zeroInDegree.removeFirst()
                sortedOrder.add(page)
                orderingGraph[page]?.forEach { neighbor ->
                    inDegree[neighbor] = inDegree[neighbor]!! - 1
                    if (inDegree[neighbor] == 0) {
                        zeroInDegree.add(neighbor)
                    }
                }
            }

            return sortedOrder
        }

        return manuals
            .filter { manual -> !manual.isValid(rules) }
            .map { manual -> manual.reorder() }
            .sumOf { it[it.size / 2] }
    }

    private fun extractRulesAndManuals(data: String): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val (firstPart, secondPart) = data.split("\r\n\r\n")

        val rules = firstPart.split("\n").map { line ->
            val (left, right) = line.split("|")
            Pair(left.trim().toInt(), right.trim().toInt())
        }

        val manuals = secondPart.split("\n").map { line ->
            line.split(",").map { page -> page.trim().toInt() }
        }

        return Pair(rules, manuals)
    }

    private fun List<Int>.isValid(rules: List<Pair<Int, Int>>): Boolean {
        var prevVals = Stack<Int>()
        var isValid = true
        this.forEach { num ->

            rules.forEach { rule ->
                if (rule.first == num) {
                    if (prevVals.contains(rule.second)) {
                        isValid = false
                    }
                }
            }
            prevVals.add(num)
        }

        return isValid
    }
}

