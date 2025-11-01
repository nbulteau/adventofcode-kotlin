package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.Point
import me.nicolas.adventofcode.utils.SimpleGrid
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs
import kotlin.time.ExperimentalTime

// https://adventofcode.com/2019/day/10
@ExperimentalTime
fun main() {
    val data = readFileDirectlyAsText("/year2019/day10/data.txt")

    val day = Day10(2019, 11, "Space Police")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day10(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    /**
     * Part One: Find the best location for a monitoring station.
     *
     * The best location is the asteroid that can detect the most other asteroids.
     * An asteroid can detect another if there's a direct line of sight (no other asteroid in between).
     *
     * Strategy: For each asteroid, count how many other asteroids are visible from it.
     * Asteroids in the same direction (on the same ray) share a normalized direction vector,
     * so we count unique direction vectors instead of individual asteroids.
     *
     * @return The number of asteroids visible from the best location
     */
    fun partOne(data: String): Int {
        val grid = SimpleGrid.of(data)
        val asteroids = grid.findAll('#')

        // Find the asteroid with the maximum number of visible asteroids
        val (bestLocation, visibleCount) = asteroids.findBestLocation()
        println("Best location: $bestLocation can detect $visibleCount asteroids")

        return visibleCount
    }

    fun partTwo(data: String): Int {
        val grid = SimpleGrid.of(data)
        val asteroids = grid.findAll('#')

        // Find the best location for the monitoring station
        val (station, _) = asteroids.findBestLocation()
        println("Station location: $station")

        // Get all asteroids except the station
        val targets = asteroids.filter { it != station }

        // Group asteroids by their direction from the station
        val asteroidsByDirection = targets.groupBy { asteroid ->
            getDirection(station, asteroid)
        }

        // Sort asteroids in each direction by distance (closest first)
        val sortedByDirection = asteroidsByDirection.mapValues { (_, asteroidList) ->
            asteroidList.sortedBy { asteroid ->
                distance(station, asteroid)
            }.toMutableList()
        }

        // Calculate angles for each direction and sort them clockwise from up
        // up = angle 0, right = 90, down = 180, left = 270
        val directionsByAngle = sortedByDirection.keys.sortedBy { direction ->
            calculateAngle(direction)
        }

        // Vaporize asteroids in order
        val vaporized = mutableListOf<Point>()

        while (vaporized.size < targets.size) {
            // One full rotation
            for (direction in directionsByAngle) {
                val asteroidList = sortedByDirection[direction]!!
                if (asteroidList.isNotEmpty()) {
                    // Vaporize the closest asteroid in this direction
                    val asteroid = asteroidList.removeAt(0)
                    vaporized.add(asteroid)

                    if (vaporized.size == 200) {
                        println("200th vaporized asteroid at problem coords: (${asteroid.y}, ${asteroid.x})")
                        // SimpleGrid stores coordinates with swapped convention:
                        // Problem coordinates (X, Y) = (col, row) map to Point(x=row, y=col)
                        // So to convert back: problem X = asteroid.y, problem Y = asteroid.x
                        return asteroid.y * 100 + asteroid.x
                    }
                }
            }
        }

        return 0
    }

    /**
     * Calculates the angle in degrees from the station to a direction vector.
     * Angle 0 is straight up (decreasing row), and increases clockwise.
     *
     * Point(x, y) represents (row, col), so:
     * - dx = row difference (positive = down, negative = up)
     * - dy = col difference (positive = right, negative = left)
     *
     * @param direction Normalized direction vector (dx, dy)
     * @return Angle in degrees [0, 360)
     */
    private fun calculateAngle(direction: Pair<Int, Int>): Double {
        val (dx, dy) = direction
        // atan2(dx, dy) gives angle with dy as x-axis
        // Adding 90 degrees rotates to make "up" (dx=-1, dy=0) be at 0 degrees
        val angleRadians = kotlin.math.atan2(dx.toDouble(), dy.toDouble())
        var angleDegrees = Math.toDegrees(angleRadians) + 90.0

        // Normalize to [0, 360)
        angleDegrees = (angleDegrees + 360.0) % 360.0

        return angleDegrees
    }

    /**
     * Calculates the Manhattan distance between two points.
     *
     * @param from Starting point
     * @param to Ending point
     * @return Manhattan distance
     */
    private fun distance(from: Point, to: Point): Int {
        return abs(to.x - from.x) + abs(to.y - from.y)
    }

    /**
     * Finds the best location for the monitoring station.
     *
     * Evaluates each asteroid by counting how many other asteroids it can see.
     * Returns both the location and the count for convenience.
     *
     * @return Pair of ( the best asteroid location, number of visible asteroids from that location)
     */
    private fun  List<Point>.findBestLocation(): Pair<Point, Int> {
        return this.maxByOrNull { asteroid ->
            countVisibleAsteroids(asteroid)
        }?.let { point -> point to countVisibleAsteroids(point) } ?: (Point(0, 0) to 0)
    }

    /**
     * Counts how many asteroids are visible from a given station point.
     *
     * Key insight: Multiple asteroids can be on the same line from the station.
     * Only the closest one is visible; the others are blocked.
     * Asteroids on the same line have the same normalized direction vector.
     *
     * Example: From (0,0), asteroids at (2,4) and (3,6) both have direction (1,2) after normalization.
     * They're on the same ray, so only one is visible.
     *
     * @param station The point from which we're observing
     * @return The number of unique directions, which equals the number of visible asteroids
     */
    private fun  List<Point>.countVisibleAsteroids(station: Point): Int {
        // For each other asteroid, calculate the direction vector (reduced by GCD)
        // Count unique directions - each direction means we can see at least one asteroid
        return this
            .filter { point -> point != station }  // Exclude the station itself
            .map { asteroid -> getDirection(station, asteroid) }  // Get normalized direction to each asteroid
            .toSet()  // Keep only unique directions (removes duplicates)
            .size  // Count of unique directions = count of visible asteroids
    }

    /**
     * Calculate the direction from 'from' to 'to' as a normalized vector.
     * We reduce the direction vector by dividing by GCD to get the simplest form.
     * This way, all asteroids in the same direction will have the same normalized vector.
     */
    private fun getDirection(from: Point, to: Point): Pair<Int, Int> {
        val dx = to.x - from.x
        val dy = to.y - from.y
        val gcd = gcd(abs(dx), abs(dy))

        return if (gcd == 0) {
            Pair(0, 0)
        } else {
            Pair(dx / gcd, dy / gcd)
        }
    }

    /**
     * Calculates the Greatest Common Divisor (GCD) of two integers using Euclid's algorithm.
     *
     * The GCD is the largest positive integer that divides both numbers without a remainder.
     * This is used to normalize direction vectors to their simplest form.
     *
     * Example: gcd(6, 9) = 3, so direction (6, 9) normalizes to (6/3, 9/3) = (2, 3)
     *
     * Euclid's algorithm:
     * - gcd(a, b) = gcd(b, a % b) recursively until b = 0
     * - When b = 0, the GCD is a
     *
     * @param a First integer
     * @param b Second integer
     * @return The greatest common divisor of a and b
     */
    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) {
            a  // Base case: when b is 0, a is the GCD
        } else {
            gcd(b, a % b)  // Recursive case: replace (a, b) with (b, a mod b)
        }
    }
}