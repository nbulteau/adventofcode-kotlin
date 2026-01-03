package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.*
import kotlin.math.abs
import kotlin.time.ExperimentalTime

// https://adventofcode.com/2019/day/10
@ExperimentalTime
fun main() {
    val data = readFileDirectlyAsText("/year2019/day10/data.txt")

    val day = Day10(2019, 10)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day10(year: Int, day: Int, title: String = "Monitoring Station") : AdventOfCodeDay(year, day, title) {

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
        val (_, visibleCount) = asteroids.findBestLocation()

        return visibleCount
    }

    /**
     * Part Two: Find the 200th asteroid to be vaporized by the rotating laser.
     *
     * The laser starts pointing up and rotates clockwise, vaporizing one asteroid per direction
     * per rotation. If multiple asteroids are in the same direction, only the closest is vaporized
     * in each rotation, then the laser continues to the next direction.
     *
     * Strategy:
     * 1. Find the best monitoring station location (from Part One)
     * 2. Group all other asteroids by their direction from the station
     * 3. Sort asteroids within each direction by distance (closest first)
     * 4. Sort all directions by angle (starting from up = 0°, going clockwise)
     * 5. Simulate the laser rotation, vaporizing one asteroid per direction per rotation
     * 6. Return the coordinate encoding (X*100 + Y) of the 200th vaporized asteroid
     *
     * @return X*100 + Y coordinate encoding of the 200th vaporized asteroid
     */
    fun partTwo(data: String): Int {
        val grid = SimpleGrid.of(data)
        val asteroids = grid.findAll('#')

        // Step 1: Find the best location for the monitoring station (same as Part One)
        val (station, _) = asteroids.findBestLocation()

        // Step 2: Get all asteroids except the station itself (these are our targets)
        val targets = asteroids.filter { it != station }

        // Step 3: Group asteroids by their normalized direction vector from the station
        // Asteroids in the same direction (collinear) will be in the same group
        // Example: From station at (5,5), asteroids at (3,5) and (1,5) both have direction (-1,0)
        val asteroidsByDirection = targets.groupBy { asteroid ->
            getDirection(station, asteroid)
        }

        // Step 4: Within each direction, sort asteroids by distance from station (closest first)
        // This ensures we vaporize the closest asteroid first in each direction
        // We use mutableList so we can remove asteroids as they're vaporized
        val sortedByDirection = asteroidsByDirection.mapValues { (_, asteroidList) ->
            asteroidList.sortedBy { asteroid ->
                distance(station, asteroid)
            }.toMutableList()
        }

        // Step 5: Sort all directions by their angle from "up", going clockwise
        // The laser starts pointing up (angle 0°) and rotates clockwise
        // up = 0°, right = 90°, down = 180°, left = 270°
        val directionsByAngle = sortedByDirection.keys.sortedBy { direction ->
            calculateAngle(direction)
        }

        // Step 6: Simulate the laser vaporization process
        val vaporized = mutableListOf<Point>()

        // Continue rotating until all asteroids are vaporized
        while (vaporized.size < targets.size) {
            // One full rotation: visit each direction once
            for (direction in directionsByAngle) {
                val asteroidList = sortedByDirection[direction]!!

                // If there are still asteroids in this direction
                if (asteroidList.isNotEmpty()) {
                    // Vaporize the closest asteroid (first in the sorted list)
                    val asteroid = asteroidList.removeAt(0)
                    vaporized.add(asteroid)

                    // Check if this is the 200th asteroid
                    if (vaporized.size == 200) {
                        println("200th vaporized asteroid at problem coords: (${asteroid.x}, ${asteroid.y})")

                        // Convert Point coordinates back to problem coordinates:
                        // SimpleGrid stores Point(x=col, y=row), and problem uses (X=col, Y=row)
                        // So: problem X = asteroid.x, problem Y = asteroid.y
                        // Answer format: X * 100 + Y
                        return asteroid.x * 100 + asteroid.y
                    }
                }
                // If the list is empty, the laser just passes through this direction
                // and continues to the next direction
            }
            // After one full rotation, loop back to start another rotation
            // (Some directions may now be empty if all asteroids in that direction were vaporized)
        }

        // Should never reach here if there are at least 200 asteroids
        return 0
    }

    /**
     * Calculates the angle in degrees from the station to a direction vector.
     * Angle 0 is straight up (decreasing row), and increases clockwise.
     *
     * Point(x, y) represents (col, row), so:
     * - dx = col difference (positive = right, negative = left)
     * - dy = row difference (positive = down, negative = up)
     *
     * @param direction Normalized direction vector (dx, dy)
     * @return Angle in degrees [0, 360)
     */
    private fun calculateAngle(direction: Pair<Int, Int>): Double {
        val (dx, dy) = direction
        // atan2(dy, dx) gives angle with dx as x-axis
        // We need to adjust to make "up" (dx=0, dy=-1) be at 0 degrees
        val angleRadians = kotlin.math.atan2(dx.toDouble(), -dy.toDouble())
        var angleDegrees = Math.toDegrees(angleRadians)

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
    private fun List<Point>.findBestLocation(): Pair<Point, Int> {
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
    private fun List<Point>.countVisibleAsteroids(station: Point): Int {
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