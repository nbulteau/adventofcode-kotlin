package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 14: Space Stoichiometry ---
// https://adventofcode.com/2019/day/14
fun main() {
    val data = readFileDirectlyAsText("/year2019/day14/data.txt")
    val day = Day14(2019, 14)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day14(year: Int, day: Int, title: String = "Space Stoichiometry") : AdventOfCodeDay(year, day, title) {

    data class Chemical(val name: String, val quantity: Long)
    data class Reaction(val inputs: List<Chemical>, val output: Chemical)

    private fun parseReactions(data: String): Map<String, Reaction> {
        return data.lines()
            .filter { it.isNotBlank() }
            .associate { line ->
                val (inputsStr, outputStr) = line.split(" => ")

                val inputs = inputsStr.split(", ").map {
                    val (qty, name) = it.trim().split(" ")
                    Chemical(name, qty.toLong())
                }

                val (qty, name) = outputStr.trim().split(" ")
                val output = Chemical(name, qty.toLong())

                name to Reaction(inputs, output)
            }
    }

    /**
     * Calculate the minimum amount of ORE needed to produce a given quantity of FUEL.
     *
     * The algorithm uses a greedy approach with surplus tracking:
     * - Start with the desired FUEL quantity
     * - Iteratively break down each chemical into its constituent inputs
     * - Track surplus chemicals produced by reactions (since reactions produce fixed quantities)
     * - Continue until only ORE remains in the needs
     *
     * @param reactions Map of chemical name to its production reaction
     * @param fuelQuantity The amount of FUEL to produce (default = 1)
     * @return The minimum amount of ORE required
     */
    private fun calculateOreNeeded(reactions: Map<String, Reaction>, fuelQuantity: Long = 1): Long {
        // Track leftover chemicals from previous reactions (since reactions produce fixed quantities)
        val surplus = mutableMapOf<String, Long>()

        // Track chemicals we still need to produce
        val needed = mutableMapOf("FUEL" to fuelQuantity)

        // Continue until only ORE remains (ORE is the base resource that doesn't need production)
        while (needed.any { (name, _) -> name != "ORE" }) {
            // Pick any non-ORE chemical that we need
            val (chemical, quantity) = needed.entries.first { it.key != "ORE" }
            needed.remove(chemical)

            // Check if we have surplus from previous reactions to use first
            val available = surplus.getOrDefault(chemical, 0)
            val actualNeeded = quantity - available

            // If surplus is enough to cover our needs, update surplus and continue
            if (actualNeeded <= 0) {
                surplus[chemical] = -actualNeeded
                continue
            }

            // We used all available surplus, reset it
            surplus[chemical] = 0

            // Get the reaction that produces this chemical
            val reaction = reactions[chemical]!!
            val outputQuantity = reaction.output.quantity

            // Calculate how many times we need to run the reaction (round up division)
            // Example: need 15, reaction produces 10 → need to run 2 times
            val times = (actualNeeded + outputQuantity - 1) / outputQuantity

            // Calculate surplus: if reaction produces more than we need, save the extra
            val produced = times * outputQuantity
            if (produced > actualNeeded) {
                surplus[chemical] = produced - actualNeeded
            }

            // Add all input requirements to our needs map
            // Each input is multiplied by the number of times we run the reaction
            for (input in reaction.inputs) {
                needed[input.name] = needed.getOrDefault(input.name, 0) + input.quantity * times
            }
        }

        // Return the total ORE needed
        return needed.getOrDefault("ORE", 0)
    }

    /**
     * Part One: Calculate the minimum amount of ORE required to produce exactly 1 FUEL.
     *
     * The algorithm works by:
     * 1. Parsing the input into a map of chemical reactions
     * 2. Using a greedy approach to calculate the ORE needed, starting from 1 FUEL
     * 3. Iteratively breaking down each chemical into its components until only ORE remains
     * 4. Tracking surplus chemicals produced to avoid waste
     *
     * @param data The puzzle input containing chemical reactions
     * @return The minimum amount of ORE needed to produce 1 FUEL
     */
    fun partOne(data: String): Long {
        val reactions = parseReactions(data)
        return calculateOreNeeded(reactions)
    }

    /**
     * Part Two: Calculate the maximum amount of FUEL that can be produced with 1 trillion ORE.
     *
     * Uses binary search to efficiently find the maximum FUEL quantity:
     * 1. Start with a search range from 1 to 1 trillion (theoretical maximum)
     * 2. For each midpoint, calculate the ORE needed for that amount of FUEL
     * 3. If the ORE needed is within budget, try a higher amount (search right)
     * 4. If the ORE needed exceeds budget, try a lower amount (search left)
     * 5. Converge to the maximum FUEL that can be produced
     *
     * @param data The puzzle input containing chemical reactions
     * @return The maximum amount of FUEL that can be produced with 1 trillion ORE
     */
    fun partTwo(data: String): Long {
        val reactions = parseReactions(data)
        val totalOre = 1_000_000_000_000L

        // Binary search for the maximum fuel we can produce
        var low = 1L
        var high = totalOre

        while (low < high) {
            val mid = (low + high + 1) / 2
            val oreNeeded = calculateOreNeeded(reactions, mid)

            if (oreNeeded <= totalOre) {
                low = mid
            } else {
                high = mid - 1
            }
        }

        return low
    }
}