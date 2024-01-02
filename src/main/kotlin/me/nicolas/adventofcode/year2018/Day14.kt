package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo


// https://adventofcode.com/2018/day/14
fun main() {
    val day = Day14(2018, 14, "Chocolate Charts")
    prettyPrintPartOne { day.partOne(320851) }
    prettyPrintPartTwo { day.partTwo(320851) }
}

class Day14(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    // It generates a sequence of recipes until the size of the sequence is equal to the input plus 10.
    // It then returns the last 10 recipes as a string.
    fun partOne(input: Int): String {
        val target = input + 10
        val recipes = createNewRecipes { recipes -> recipes.size == target }

        return recipes.takeLast(10).joinToString("")
    }

    // It generates a sequence of recipes until the end of the sequence matches the digits of the input.
    // It then returns the number of recipes before this sequence.
    fun partTwo(input: Int): Int {
        val target = input.toString().map { char -> char.toString().toInt() }.toList()
        val recipes = createNewRecipes { recipes -> recipes.endsWith(target) }

        return (recipes.size - target.size)
    }

    // Helper method to check if a list ends with another list of integers.
    private fun List<Int>.endsWith(other: List<Int>): Boolean {

        return if (this.size < other.size) {
            false
        } else {
            this.slice(this.size - other.size until this.size) == other
        }
    }

    // `stopCondition` determines when to stop generating recipes.
    private fun createNewRecipes(stopCondition: (List<Int>) -> Boolean): List<Int> {
        val recipes = mutableListOf(3, 7)
        var elf1 = 0
        var elf2 = 1
        var stop = false

        while (!stop) {
            val nextValue = recipes[elf1] + recipes[elf2]
            nextValue.toString()
                .map { char -> char.toString().toInt() }
                .forEach { int ->
                    if (!stop) {
                        recipes.add(int)
                        stop = stopCondition(recipes)
                    }
                }
            elf1 = (elf1 + recipes[elf1] + 1) % recipes.size
            elf2 = (elf2 + recipes[elf2] + 1) % recipes.size
        }

        return recipes
    }
}