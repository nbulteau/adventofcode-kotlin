package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 13: Knights of the Dinner Table ---
// https://adventofcode.com/2015/day/13
fun main() {
    val data = readFileDirectlyAsText("/year2015/day13/data.txt")
    val day = Day13(2015, 13)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartOne { day.partOneOptimized(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day13(year: Int, day: Int, title: String = "Knights of the Dinner Table") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val (guests, happinessMap) = parseInput(data)

        return findOptimalHappiness(guests, happinessMap)
    }

    fun partTwo(data: String): Int {
        val (guests, map) = parseInput(data)

        val happinessMap = map.toMutableMap()
        val me = "Me"
        guests.forEach { guest ->
            happinessMap[guest to me] = 0
            happinessMap[me to guest] = 0
        }

        return findOptimalHappiness(guests + me, happinessMap)
    }

    fun partOneOptimized(data: String): Int {
        val (guests, happinessMap) = parseInput(data)
        val guestList = guests.toList()
        val guestListSize = guestList.size

        // Initialize memoization array with minimum integer values
        val cache = Array(1 shl guestListSize) { IntArray(guestListSize) { Int.MIN_VALUE } }

        // Define a recursive function to calculate the maximum happiness
        fun calculateMaxHappiness(mask: Int, last: Int): Int {
            // If all guests are seated, return the happiness of the last and first guest
            if (mask == (1 shl guestListSize) - 1) {
                return happinessMap.getOrDefault(guestList[last] to guestList[0], 0) +
                        happinessMap.getOrDefault(guestList[0] to guestList[last], 0)
            }
            // If the value is already computed, return it
            if (cache[mask][last] != Int.MIN_VALUE) return cache[mask][last]

            var maxHappiness = Int.MIN_VALUE
            // Try seating each guest who is not yet seated
            for (i in 0 until guestListSize) {
                if (mask and (1 shl i) == 0) {
                    val nextMask = mask or (1 shl i)
                    val happiness = happinessMap.getOrDefault(guestList[last] to guestList[i], 0) +
                            happinessMap.getOrDefault(guestList[i] to guestList[last], 0) +
                            calculateMaxHappiness(nextMask, i)
                    maxHappiness = maxOf(maxHappiness, happiness)
                }
            }
            // Store the computed value in the cache
            cache[mask][last] = maxHappiness
            return maxHappiness
        }

        var maxHappiness = Int.MIN_VALUE
        // Start the recursive function for each guest
        for (i in 0 until guestListSize) {
            maxHappiness = maxOf(maxHappiness, calculateMaxHappiness(1 shl i, i))
        }
        return maxHappiness
    }

    private val regex = """(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).""".toRegex()

    private fun parseInput(data: String): Pair<Set<String>, Map<Pair<String, String>, Int>> {
        val happinessMap = data.lines().mapNotNull { line ->
            regex.matchEntire(line)?.destructured?.let { (person1, gainOrLose, units, person2) ->
                val happiness = if (gainOrLose == "gain") units.toInt() else -units.toInt()
                (person1 to person2) to happiness
            }
        }.toMap()

        val guests = happinessMap.keys.flatMap { pair -> listOf(pair.first, pair.second) }.toSet()

        return guests to happinessMap
    }

    // Find the optimal happiness by evaluating every possible seating arrangement.
    private fun findOptimalHappiness(guests: Set<String>, happinessMap: Map<Pair<String, String>, Int>): Int {
        return guests.permutations().maxOf { arrangement ->
            val happiness = arrangement.zipWithNext { a, b ->
                val happinessAB = happinessMap.getOrDefault(a to b, 0)
                val happinessBA = happinessMap.getOrDefault(b to a, 0)
                happinessAB + happinessBA
            }.sum() + (happinessMap.getOrDefault(arrangement.last() to arrangement.first(), 0) + happinessMap.getOrDefault(arrangement.first() to arrangement.last(), 0))
            happiness
        }
    }

    // Generate all possible permutations of a set of elements : evaluating every possible seating arrangement.
    private fun <T> Set<T>.permutations(): Sequence<List<T>> = sequence {
        if (this@permutations.isEmpty()) yield(emptyList())
        else {
            for (element in this@permutations) {
                for (permutation in (this@permutations - element).permutations()) {
                    yield(listOf(element) + permutation)
                }
            }
        }
    }
}