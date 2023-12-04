package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 21: Allergen Assessment ---
// https://adventofcode.com/2020/day/21
@ExperimentalTime
fun main() {

    println("--- Day 21: Allergen Assessment ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day21/training.txt")
    val data = readFileDirectlyAsText("/year2020/day21/data.txt")

    val lines = data.split("\n")

    // Part One
    val (ingredients, allergens) = Day21().partOne(lines)

    // Part Two
    val duration = measureTime { Day21().partTwo(ingredients, allergens.values.sortedBy { allergen -> allergen.name }) }
    println("Part two duration : $duration")
}


class Day21 {

    data class Food(val ingredients: MutableList<Ingredient>)

    data class Ingredient(val name: String) {

        var occurrence = 0

        // one ingredient = 1 or 0 allergen
        var allergen: Allergen? = null

        fun incOccurrence() {
            occurrence++
        }
    }

    data class Allergen(val name: String) {

        val inFoodList = mutableListOf<Food>()

        fun addToInFoodList(food: Food) {
            inFoodList.add(food)
        }
    }

    fun extractFoods(lines: List<String>): Pair<MutableMap<String, Ingredient>, MutableMap<String, Allergen>> {

        val ingredients = mutableMapOf<String, Ingredient>()
        val allergens = mutableMapOf<String, Allergen>()

        lines.forEach { line ->
            val ingredientsStr = line.substringBefore(" (contains ")
            val allergensStr = line.substringAfter(" (contains ").substringBefore(")")

            // Create Ingredient
            val ingredientsInFood = ingredientsStr.split(" ").map { name ->
                if (!ingredients.containsKey(name)) {
                    ingredients[name] = Ingredient(name)
                }
                ingredients[name]!!.apply { this.incOccurrence() }
            }.toMutableList()

            // Create Allergen
            allergensStr.split(", ").map { name ->
                if (!allergens.containsKey(name)) {
                    allergens[name] = Allergen(name)
                }
                allergens[name]!!.apply { this.addToInFoodList(Food(ingredientsInFood)) }
            }.toMutableList()
        }

        return Pair(ingredients, allergens)
    }

    fun partOne(lines: List<String>): Pair<MutableMap<String, Ingredient>, MutableMap<String, Allergen>> {

        val (ingredients, allergens) = extractFoods(lines)

        checkAllergens(allergens)

        val result = ingredients.values
            .filter { ingredient -> ingredient.allergen == null }
            .map { ingredient -> ingredient.occurrence }
            .reduce { acc, i -> acc + i }

        println("Part one = $result")

        return Pair(ingredients, allergens)
    }

    /**
     * Each allergen is found in exactly one ingredient.
     * Each ingredient contains zero or one allergen.
     */
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
            allergensToCheck.removeAll(allergensChecked)
        }
    }

    fun partTwo(ingredients: MutableMap<String, Ingredient>, allergensSortedByName: List<Allergen>) {

        // in a food, an allergen is only in one ingredient
        val allergenIngredient = ingredients.values
            .filter { ingredient -> ingredient.allergen != null }
            .map { ingredient -> ingredient.allergen!! to ingredient.name }
            .toMap()
        val list = allergensSortedByName.joinToString(",") { allergen -> allergenIngredient[allergen]!! }

        println("Part two = $list")
    }
}


