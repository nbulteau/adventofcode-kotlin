package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 21: RPG Simulator 20XX ---
// https://adventofcode.com/2015/day/21
fun main() {
    val data = readFileDirectlyAsText("/year2015/day21/data.txt")
    val day = Day21(2015, 21)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day21(year: Int, day: Int, title: String = "RPG Simulator 20XX") : AdventOfCodeDay(year, day, title) {

    private data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)

    private val weapons = listOf(
        Item("Dagger", 8, 4, 0),
        Item("Shortsword", 10, 5, 0),
        Item("Warhammer", 25, 6, 0),
        Item("Longsword", 40, 7, 0),
        Item("Greataxe", 74, 8, 0)
    )

    private val armors = listOf(
        Item("None", 0, 0, 0),  // No armor option
        Item("Leather", 13, 0, 1),
        Item("Chainmail", 31, 0, 2),
        Item("Splintmail", 53, 0, 3),
        Item("Bandedmail", 75, 0, 4),
        Item("Platemail", 102, 0, 5)
    )

    private val rings = listOf(
        Item("None", 0, 0, 0),  // No ring option
        Item("Damage +1", 25, 1, 0),
        Item("Damage +2", 50, 2, 0),
        Item("Damage +3", 100, 3, 0),
        Item("Defense +1", 20, 0, 1),
        Item("Defense +2", 40, 0, 2),
        Item("Defense +3", 80, 0, 3)
    )

    private data class Character(var hitPoints: Int, val damage: Int, val armor: Int)

    fun partOne(data: String): Int {
        val (hitPoints, damage, armor) = data.lines().map { line -> line.split(": ")[1].toInt() }
        val boss = Character(hitPoints, damage, armor)

        return findMinimumCostToWin(boss)
    }

    fun partTwo(data: String): Int {
        val (hitPoints, damage, armor) = data.lines().map { line -> line.split(": ")[1].toInt() }
        val boss = Character(hitPoints, damage, armor)

        return findMaximumCostToLose(boss)
    }

    // Generate all possible combinations of items to equip the player
    private fun generateCombinations(): List<List<Item>> {
        val combinations = mutableListOf<List<Item>>()
        for (weapon in weapons) {
            for (armor in armors) {
                for (ring1 in rings) {
                    for (ring2 in rings) {
                        if (ring1 != ring2 || ring1.name == "None") {
                            combinations.add(listOf(weapon, armor, ring1, ring2).filter { it.name != "None" })
                        }
                    }
                }
            }
        }
        return combinations
    }

    // Simulate the battle between the player and the boss and return true if the player wins
    private fun simulateBattle(player: Character, boss: Character): Boolean {
        while (true) {
            boss.hitPoints -= maxOf(1, player.damage - boss.armor)
            if (boss.hitPoints <= 0) {
                return true
            }
            player.hitPoints -= maxOf(1, boss.damage - player.armor)
            if (player.hitPoints <= 0) {
                return false
            }
        }
    }

    // Find the minimum cost to win the battle
    private fun findMinimumCostToWin(boss: Character): Int {
        val combinations = generateCombinations()
        var minCost = Int.MAX_VALUE

        for (combination in combinations) {
            val cost = combination.sumOf { item -> item.cost }
            val damage = combination.sumOf { item -> item.damage }
            val armor = combination.sumOf { item -> item.armor }
            val player = Character(100, damage, armor)

            if (simulateBattle(player, boss.copy())) {
                minCost = minOf(minCost, cost)
            }
        }

        return minCost
    }

    // Find the maximum cost to lose the battle
    private fun findMaximumCostToLose(boss: Character): Int {
        val combinations = generateCombinations()
        var maxCost = 0

        for (combination in combinations) {
            val cost = combination.sumOf { item -> item.cost }
            val damage = combination.sumOf { item -> item.damage }
            val armor = combination.sumOf { item -> item.armor }
            val player = Character(100, damage, armor)

            if (!simulateBattle(player, boss.copy())) {
                maxCost = maxOf(maxCost, cost)
            }
        }

        return maxCost
    }
}