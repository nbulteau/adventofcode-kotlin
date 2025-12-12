package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 21: Allergen Assessment ---
// https://adventofcode.com/2020/day/21
fun main() {
    val data = readFileDirectlyAsText("/year2020/day21/data.txt")
    val day = Day21(2020, 21, "Allergen Assessment")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day21(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val lines = data.split("\n")
        val (ingredients, allergens) = extractFoods(lines)
        checkAllergens(allergens)
        return ingredients.values
            .filter { ingredient -> ingredient.allergen == null }
            .sumOf { ingredient -> ingredient.occurrence }
    }

    fun partTwo(data: String): String {
        val lines = data.split("\n")
        val (ingredients, allergens) = extractFoods(lines)
        checkAllergens(allergens)
        val allergensSortedByName = allergens.values.sortedBy { allergen -> allergen.name }
        val allergenIngredient = ingredients.values
            .filter { ingredient -> ingredient.allergen != null }
            .associate { ingredient -> ingredient.allergen!! to ingredient.name }
        return allergensSortedByName.joinToString(",") { allergen -> allergenIngredient[allergen]!! }
    }

    private data class Food(val ingredients: MutableList<Ingredient>)

    private data class Ingredient(val name: String) {
        var occurrence = 0
        var allergen: Allergen? = null
        fun incOccurrence() {
            occurrence++
        }
    }

    private data class Allergen(val name: String) {
        val inFoodList = mutableListOf<Food>()
        fun addToInFoodList(food: Food) {
            inFoodList.add(food)
        }
    }

    private fun extractFoods(lines: List<String>): Pair<MutableMap<String, Ingredient>, MutableMap<String, Allergen>> {
        val ingredients = mutableMapOf<String, Ingredient>()
        val allergens = mutableMapOf<String, Allergen>()

        lines.forEach { line ->
            if (line.isNotEmpty()) {
                val ingredientsStr = line.substringBefore(" (contains ")
                val allergensStr = line.substringAfter(" (contains ").substringBefore(")")

                val ingredientsInFood = ingredientsStr.split(" ").map { name ->
                    ingredients.computeIfAbsent(name) { Ingredient(it) }.apply { incOccurrence() }
                }.toMutableList()

                allergensStr.split(", ").forEach { name ->
                    allergens.computeIfAbsent(name) { Allergen(it) }.addToInFoodList(Food(ingredientsInFood))
                }
            }
        }
        return Pair(ingredients, allergens)
    }

    private fun checkAllergens(allergens: MutableMap<String, Allergen>) {
        val allergensToCheck = allergens.values.toMutableList()

        while (allergensToCheck.isNotEmpty()) {
            val allergensChecked = mutableListOf<Allergen>()

            for (allergen in allergensToCheck) {
                var listOfAcceptableIngredients: List<Ingredient> = allergen.inFoodList[0].ingredients.toList()

                for (food in allergen.inFoodList) {
                    val listOfRemainIngredients = mutableListOf<Ingredient>()

                    for (ingredient in food.ingredients) {
                        if (ingredient.allergen == null && listOfAcceptableIngredients.contains(ingredient)) {
                            listOfRemainIngredients.add(ingredient)
                        }
                    }
                    listOfAcceptableIngredients = listOfRemainIngredients
                }

                // one ingredient = 1 or 0 allergen
                if (listOfAcceptableIngredients.size == 1) {
                    listOfAcceptableIngredients.first().allergen = allergen
                    allergensChecked.add(allergen)
                }
            }
            allergensToCheck.removeAll(allergensChecked.toSet())
        }
    }
}
