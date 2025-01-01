package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 20: Infinite Elves and Infinite Houses ---
// https://adventofcode.com/2015/day/20
fun main() {
    val day = Day20(2015, 20)
    prettyPrintPartOne { day.partOne(36000000) }
    prettyPrintPartTwo { day.partTwo(36000000) }
}

class Day20(year: Int, day: Int, title: String = "Infinite Elves and Infinite Houses") : AdventOfCodeDay(year, day, title) {

    fun partOne(targetPresents: Int): Int {
        val maxHouses = targetPresents / 10
        val houses = IntArray(maxHouses + 1)

        for (elf in 1..maxHouses) {
            for (house in elf..maxHouses step elf) {
                houses[house] += elf * 10
            }
        }

        for (house in 1..maxHouses) {
            if (houses[house] >= targetPresents) {
                return house
            }
        }

        return -1
    }

    fun partTwo(targetPresents: Int): Int {
        val maxHouses = targetPresents / 11
        val houses = IntArray(maxHouses + 1)

        for (elf in 1..maxHouses) {
            var houseCount = 0
            for (house in elf..maxHouses step elf) {
                houses[house] += elf * 11
                houseCount++
                if (houseCount == 50) {
                    break
                }
            }
        }

        for (house in 1..maxHouses) {
            if (houses[house] >= targetPresents) {
                return house
            }
        }

        return -1
    }
}