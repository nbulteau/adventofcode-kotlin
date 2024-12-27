package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.max

// --- Day 15: Science for Hungry People ---
// https://adventofcode.com/2015/day/15
fun main() {
    val data = readFileDirectlyAsText("/year2015/day15/data.txt")
    val day = Day15(2015, 15)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day15(year: Int, day: Int, title: String = "Science for Hungry People") : AdventOfCodeDay(year, day, title) {

    private data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
    )

    fun partOne(data: String): Int {
        val ingredients = parseIngredients(data)
        return findBestScore(ingredients)
    }

    fun partTwo(data: String): Int {
        val ingredients = parseIngredients(data)
        return findBestScore(ingredients, 500)
    }

    private val regex =
        """(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""".toRegex()

    private fun parseIngredients(data: String): List<Ingredient> {
        return data.lines().mapNotNull { line ->
            regex.matchEntire(line)?.destructured?.let { (name, capacity, durability, flavor, texture, calories) ->
                Ingredient(
                    name,
                    capacity.toInt(),
                    durability.toInt(),
                    flavor.toInt(),
                    texture.toInt(),
                    calories.toInt()
                )
            }
        }
    }

    // Brute force all possible combinations of ingredients and amounts
    private fun findBestScore(ingredients: List<Ingredient>, calorieTarget: Int? = null): Int {
        var bestScore = 0
        for (a in 0..100) {
            for (b in 0..(100 - a)) {
                for (c in 0..(100 - a - b)) {
                    val d = 100 - a - b - c
                    val amounts = listOf(a, b, c, d)
                    val (capacity, durability, flavor, texture, calories) = calculateProperties(ingredients, amounts)
                    if (calorieTarget == null || calories == calorieTarget) {
                        val score = max(0, capacity) * max(0, durability) * max(0, flavor) * max(0, texture)
                        bestScore = max(bestScore, score)
                    }
                }
            }
        }
        return bestScore
    }

    // Calculate the properties of a cookie based on the ingredients and amounts
    private fun calculateProperties(ingredients: List<Ingredient>, amounts: List<Int>): List<Int> {
        val properties = MutableList(5) { 0 }
        for ((ingredient, amount) in ingredients.zip(amounts)) {
            properties[0] += ingredient.capacity * amount
            properties[1] += ingredient.durability * amount
            properties[2] += ingredient.flavor * amount
            properties[3] += ingredient.texture * amount
            properties[4] += ingredient.calories * amount
        }

        return properties
    }
}