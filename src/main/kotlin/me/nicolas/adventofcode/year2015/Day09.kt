package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 9: All in a Single Night ---
// https://adventofcode.com/2015/day/9
fun main() {
    val data = readFileDirectlyAsText("/year2015/day09/data.txt")
    val day = Day09(2015, 9)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day09(year: Int, day: Int, title: String = "All in a Single Night") : AdventOfCodeDay(year, day, title) {

    data class City(val name: String, val distances: MutableMap<String, Int> = mutableMapOf())

    private fun extractCities(data: String): List<City> {
        // Parse input and create cities with their distances
        val cityMap = mutableMapOf<String, City>()

        for (input in data.split("\n")) {
            val (from, distanceMap) = parseDistance(input)
            val to = distanceMap.keys.first()
            val distance = distanceMap[to]!!

            // Add 'from' to map if not already present
            if (!cityMap.containsKey(from)) {
                cityMap[from] = City(from)
            }

            // Add 'to' to map if not already present
            if (!cityMap.containsKey(to)) {
                cityMap[to] = City(to)
            }

            // Update distances for both cities
            cityMap[from]?.distances?.put(to, distance)
            cityMap[to]?.distances?.put(from, distance)
        }

        // List of cities
        val cities = cityMap.values.toList()
        return cities
    }

    private fun parseDistance(input: String): Pair<String, Map<String, Int>> {
        val parts = input.split(" to ")
        val from = parts[0]
        val distancePart = parts[1].split(" = ")
        val to = distancePart[0]
        val distance = distancePart[1].toInt()

        return Pair(from, mapOf(to to distance))
    }

    private fun getPermutations(cities: List<City>): List<List<City>> {
        if (cities.size == 1) return listOf(listOf(cities[0]))

        val permutations = mutableListOf<List<City>>()

        for ((index, city) in cities.withIndex()) {
            val remainingCities = cities.toMutableList()
            remainingCities.removeAt(index)
            val subPermutations = getPermutations(remainingCities)

            for (subPermutation in subPermutations) {
                permutations.add(listOf(city) + subPermutation)
            }
        }

        return permutations
    }

    private fun calculateDistance(route: List<City>): Int {
        var totalDistance = 0

        for ((i, city) in route.withIndex()) {
            if (i < route.size - 1) {
                val nextCityName = route[i + 1].name
                val distance = city.distances[nextCityName]
                    ?: throw IllegalArgumentException("Distance not found between ${city.name} and $nextCityName")
                totalDistance += distance
            }
        }

        return totalDistance
    }

    fun partOne(data: String): Int {
        val cities = extractCities(data)

        // Get all permutations of the cities
        val permutations = getPermutations(cities)

        // Calculate the distances for each permutation and find the shortest one
        var minDistance = Int.MAX_VALUE
        var bestRoute: List<City>? = null

        for (route in permutations) {
            val distance = calculateDistance(route)
            if (distance < minDistance) {
                minDistance = distance
                bestRoute = route
            }
        }

        println("The shortest route is: ${bestRoute?.joinToString(" -> ") { it.name }}")

        return minDistance
    }

    fun partTwo(data: String): Int {
        val cities = extractCities(data)

        // Get all permutations of the cities
        val permutations = getPermutations(cities)

        // Calculate the distances for each permutation and find the shortest one
        var maxDistance = -1
        var longestRoute: List<City>? = null

        for (route in permutations) {
            val distance = calculateDistance(route)
            if (distance > maxDistance) {
                maxDistance = distance
                longestRoute = route
            }
        }

        println("The longest route is: ${longestRoute?.joinToString(" -> ") { it.name }}")

        return maxDistance
    }
}