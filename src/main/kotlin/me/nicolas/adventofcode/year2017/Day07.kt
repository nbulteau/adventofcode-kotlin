package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 7: Recursive Circus ---
// https://adventofcode.com/2017/day/7
fun main() {
    val data = readFileDirectlyAsText("/year2017/day07/data.txt")
    val day = Day07()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day07(year: Int = 2017, day: Int = 7, title: String = "Recursive Circus") : AdventOfCodeDay(year, day, title) {

    private data class Program(
        val name: String,
        val weight: Int,
        val children: List<String>
    )

    /**
     * Part One
     * Determine the name of the bottom program in the tower.
     *
     * Algorithm:
     * - Parse the input into Program(name, weight, children).
     * - Collect all child names from every program.
     * - The root is the program whose name never appears as a child.
     * - Return that program's name.
     */
    fun partOne(data: String): String {
        val programs = getPrograms(data)

        // Find the root program (the one that is not a child of any other program)
        val allChildren = programs.values.flatMap { program -> program.children }.toSet()
        val rootProgram = programs.values.first { program -> program.name !in allChildren }


        return rootProgram.name
    }

    /**
     * Part Two
     * Find the program that needs to be adjusted to balance the entire tower and determine the correct weight.
     * Assumes there is exactly one program causing the imbalance.
     *
     * Algorithm overview:
     * 1. Parse input into a map of Program(name, weight, childrenNames).
     * 2. Find the root as in part one (name not present in any children lists).
     * 3. Define a recursive function totalWeight(name) = own weight + sum(totalWeight(child)).
     *    - Use a cache (memoization) to avoid recomputing subtree totals.
     * 4. Traverse the tree from the root downwards, always recursing into children first
     *    so that we detect the deepest unbalanced subtree.
     * 5. For a node, collect the total weights of each of its child subtrees.
     *    - If all totals are equal the node is balanced; continue searching.
     *    - If one total differs, identify the unique (wrong) total and the expected total
     *      (the one shared by the majority of siblings).
     * 6. The corrected own weight for the wrong child is: child.weight + (expectedTotal - wrongTotal).
     * 7. Return that corrected weight when found.
     */
    fun partTwo(data: String): Int {
        val programs = getPrograms(data)

        // Find root
        val allChildren = programs.values.flatMap { program -> program.children }.toSet()
        val rootName = programs.values.first { program -> program.name !in allChildren }.name

        // Cache for subtree total weights
        val totalCache = mutableMapOf<String, Int>()

        // Recursive function to compute total weight of a node (own weight + children's totals)
        fun totalWeight(name: String): Int {
            totalCache[name]?.let { return it }
            val program = programs[name] ?: throw IllegalArgumentException("Unknown program: $name")
            val sumChildren = program.children.sumOf { child -> totalWeight(child) }
            val total = program.weight + sumChildren
            totalCache[name] = total
            return total
        }

        // Recursive search for the deepest imbalance. Returns corrected own weight if found, else null.
        fun findCorrection(name: String): Int? {
            val program = programs[name] ?: return null

            // First search deeper: if any child subtree contains the correction, return it
            for (child in program.children) {
                val res = findCorrection(child)
                if (res != null) return res
            }

            // Collect totals of each child
            if (program.children.isEmpty()) return null
            val childTotals = program.children.map { child -> child to totalWeight(child) }

            // Group totals by value
            val groups = childTotals.groupBy({ it.second }, { it.first })
            if (groups.size <= 1) return null // balanced at this node

            // The expected total is the one with the largest group (most frequent)
            val expectedTotal = groups.maxByOrNull { it.value.size }!!.key
            // The wrong total is the one with the smallest group (likely size 1)
            val wrongTotal = groups.minByOrNull { it.value.size }!!.key

            // The child with the wrong total
            val wrongChildName = groups[wrongTotal]!!.first()
            val wrongChild = programs[wrongChildName]!!

            // Compute corrected own weight for the wrong child
            val difference = expectedTotal - wrongTotal
            val correctedWeight = wrongChild.weight + difference
            return correctedWeight
        }

        // Ensure totals are computed at least once
        totalWeight(rootName)

        return findCorrection(rootName) ?: 0
    }

    private fun getPrograms(data: String): Map<String, Program> {
        val programs = data.lines().map { line ->
            val parts = line.split(" -> ")
            val nameAndWeight = parts[0].split(" ")
            val name = nameAndWeight[0]
            val weight = nameAndWeight[1].removePrefix("(").removeSuffix(")").toInt()
            val children = if (parts.size > 1) parts[1].split(", ").toList() else emptyList()
            Program(name, weight, children)
        }.associateBy { program -> program.name }
        return programs
    }
}