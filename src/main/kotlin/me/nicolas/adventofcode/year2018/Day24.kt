package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// https://adventofcode.com/2018/day/24
fun main() {
    val data = readFileDirectlyAsText("/year2018/day24/data.txt")
    val day = Day24(2018, 24, "Immune System Simulator 20XX")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day24(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    data class Group(
        val id: Int,
        val army: String,
        var units: Int,
        val hitPoints: Int,
        val attackDamage: Int,
        val attackType: String,
        val initiative: Int,
        val weaknesses: Set<String>,
        val immunities: Set<String>
    ) {
        val effectivePower: Int
            get() = units * attackDamage

        fun damageDealtTo(other: Group): Int {
            if (attackType in other.immunities) return 0
            val baseDamage = effectivePower
            return if (attackType in other.weaknesses) baseDamage * 2 else baseDamage
        }
    }

    private fun parseInput(data: String, boost: Int = 0): List<Group> {
        val groups = mutableListOf<Group>()
        val lines = data.lines()
        var currentArmy = ""
        var groupId = 1

        for (line in lines) {
            when {
                line.startsWith("Immune System:") -> currentArmy = "Immune System"
                line.startsWith("Infection:") -> currentArmy = "Infection"
                line.isBlank() -> continue
                else -> {
                    val regex =
                        """(\d+) units each with (\d+) hit points (\([^)]+\) )?with an attack that does (\d+) (\w+) damage at initiative (\d+)""".toRegex()
                    val match = regex.find(line) ?: continue

                    val units = match.groupValues[1].toInt()
                    val hitPoints = match.groupValues[2].toInt()
                    val properties = match.groupValues[3]
                    val attackDamage = match.groupValues[4].toInt()
                    val attackType = match.groupValues[5]
                    val initiative = match.groupValues[6].toInt()

                    val weaknesses = mutableSetOf<String>()
                    val immunities = mutableSetOf<String>()

                    if (properties.isNotEmpty()) {
                        val propsText = properties.trim('(', ')', ' ')
                        val parts = propsText.split("; ", ";")
                        for (part in parts) {
                            val trimmedPart = part.trim()
                            when {
                                trimmedPart.startsWith("weak to ") -> {
                                    weaknesses.addAll(trimmedPart.substring(8).split(", ").map { it.trim() })
                                }

                                trimmedPart.startsWith("immune to ") -> {
                                    immunities.addAll(trimmedPart.substring(10).split(", ").map { it.trim() })
                                }
                            }
                        }
                    }

                    val finalAttackDamage = if (currentArmy == "Immune System") attackDamage + boost else attackDamage

                    groups.add(
                        Group(
                            id = groupId++,
                            army = currentArmy,
                            units = units,
                            hitPoints = hitPoints,
                            attackDamage = finalAttackDamage,
                            attackType = attackType,
                            initiative = initiative,
                            weaknesses = weaknesses,
                            immunities = immunities
                        )
                    )
                }
            }
        }
        return groups
    }

    /**
     * Simulates combat between two armies following specific rules:
     * 1. Target selection phase: Groups choose targets based on damage potential
     * 2. Attack phase: Groups attack in initiative order
     *
     * Returns a Pair containing:
     * - The winning army name (or null if stalemate)
     * - The total number of units remaining
     */
    private fun simulateCombat(groups: List<Group>): Pair<String?, Int> {
        // Create mutable copies of groups to track state during combat
        val activeGroups = groups.map { group -> group.copy() }.toMutableList()

        // Continue combat while both armies have active groups
        while (activeGroups.any { group -> group.army == "Immune System" } && activeGroups.any { group -> group.army == "Infection" }) {
            // Target selection phase: each group selects an enemy target
            val targets = mutableMapOf<Int, Int>() // attacker index to target index
            val targetedIndices = mutableSetOf<Int>()

            // Sort groups by effective power (descending), then by initiative (descending)
            val sortedForTargeting = activeGroups
                .mapIndexed { index, group -> index to group }
                .sortedWith(
                    compareByDescending<Pair<Int, Group>> { it.second.effectivePower }
                        .thenByDescending { it.second.initiative }
                )

            // Each group selects a target in order of effective power and initiative
            for ((attackerIdx, attacker) in sortedForTargeting) {
                // Find all enemy groups that haven't been targeted yet
                val potentialTargets = activeGroups
                    .mapIndexed { index, group -> index to group }
                    .filter { (idx, group) ->
                        group.army != attacker.army && idx !in targetedIndices
                    }

                // Choose target that would take the most damage
                // Ties broken by target's effective power, then initiative
                val bestTarget = potentialTargets
                    .map { (idx, target) -> idx to attacker.damageDealtTo(target) }
                    .filter { (_, damage) -> damage > 0 } // Only target if can deal damage
                    .maxWithOrNull(
                        compareBy<Pair<Int, Int>> { it.second } // Sort by damage dealt
                            .thenBy { activeGroups[it.first].effectivePower } // Then by target's effective power
                            .thenBy { activeGroups[it.first].initiative } // Then by target's initiative
                    )

                // Record the target selection
                if (bestTarget != null) {
                    targets[attackerIdx] = bestTarget.first
                    targetedIndices.add(bestTarget.first)
                }
            }

            // Attack phase: groups attack in order of initiative (highest first)
            val sortedForAttacking = activeGroups
                .mapIndexed { index, group -> index to group }
                .sortedByDescending { it.second.initiative }

            var totalUnitsKilled = 0

            // Execute attacks in initiative order
            for ((attackerIdx, attacker) in sortedForAttacking) {
                if (attacker.units <= 0) continue // Skip if group is already dead
                val targetIdx = targets[attackerIdx] ?: continue // Skip if no target selected
                val target = activeGroups[targetIdx]

                // Calculate damage and units killed
                val damage = attacker.damageDealtTo(target)
                val unitsKilled = damage / target.hitPoints
                target.units -= unitsKilled
                totalUnitsKilled += unitsKilled
            }

            // Remove groups with no units remaining
            activeGroups.removeIf { group -> group.units <= 0 }

            // Check for stalemate: if no units were killed, combat cannot progress
            if (totalUnitsKilled == 0) {
                return null to 0
            }
        }

        // Determine winner and count remaining units
        val winner = activeGroups.firstOrNull()?.army
        val remainingUnits = activeGroups.sumOf { group -> group.units }
        return winner to remainingUnits
    }

    /**
     * Simulates a combat between the Immune System and Infection armies without any boost.
     * Returns the total number of units remaining in the winning army after combat ends.
     */
    fun partOne(data: String): Int {
        val groups = parseInput(data)
        val (_, remainingUnits) = simulateCombat(groups)
        return remainingUnits
    }

    /**
     * Finds the minimum boost needed for the Immune System to win.
     * Incrementally increases the attack damage boost applied to Immune System groups
     * until they win the combat. Returns the number of units remaining after victory.
     */
    fun partTwo(data: String): Int {
        var boost = 0
        while (true) {
            val groups = parseInput(data, boost)
            val (winner, remainingUnits) = simulateCombat(groups)
            if (winner == "Immune System") {
                return remainingUnits
            }
            boost++
        }
    }
}