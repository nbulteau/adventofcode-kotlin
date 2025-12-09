package me.nicolas.adventofcode.utils

import kotlin.math.abs

/**
 * Calculate the Greatest Common Divisor using Euclidean algorithm.
 *
 * The GCD is the largest positive integer that divides both numbers without a remainder.
 * This is used as a helper for calculating the LCM.
 *
 * @return The GCD of a and b
 */
tailrec fun Long.gcd(other: Long): Long =
    if (other == 0L) this
    else other.gcd(this % other)

/**
 * Calculate the Least Common Multiple.
 *
 * The LCM is the smallest positive integer that is divisible by both numbers.
 * When three cycles of lengths A, B, and C run simultaneously, they all align
 * at time LCM(LCM(A, B), C).
 *
 * @return The LCM of a and b
 */
fun Long.lcm(other: Long): Long =
    (this * other) / this.gcd(other)

/**
 * LCM (Least Common Multiple) for a list of numbers
 */
fun List<Long>.lcm(): Long {
    return this.reduce { acc, i -> (acc * i) / acc.gcd(i) }
}

/**
 * Manhattan distance between two points
 */
fun Pair<Int, Int>.manhattanDistance(other: Pair<Int, Int>): Long =
    abs(this.first - other.first).toLong() + abs(this.second - other.second).toLong()


/**
 * Shoelace formula for polygonal area : https://en.wikipedia.org/wiki/Shoelace_formula to calculate area
 * The shoelace formula or shoelace algorithm (also known as Gauss's area formula and the surveyor's formula)
 * is a mathematical algorithm to determine the area of a simple polygon whose vertices are described by their Cartesian coordinates in the plane.
 * https://rosettacode.org/wiki/Shoelace_formula_for_polygonal_area#Kotlin
 *
 * And we can use Pick's theorem (https://en.wikipedia.org/wiki/Pick%27s_theorem) to calculate the interior points (area).
 * A = I + b / 2 -1  -> I = - b /2 + A + 1
 */
fun shoelaceArea(points: List<Pair<Long, Long>>): Long {
    val size = points.size
    var area = 0L
    for (i in 0 until size - 1) {
        area += points[i].first * points[i + 1].second - points[i + 1].first * points[i].second
    }
    return abs(area + points[size - 1].first * points[0].second - points[0].first * points[size - 1].second) / 2
}

/**
 * Ray-casting algorithm to determine if a point is inside a polygon.
 * https://en.wikipedia.org/wiki/Point_in_polygon
 * https://rosettacode.org/wiki/Ray-casting_algorithm#Kotlin
 *
 * Cast a horizontal ray from the point to the right and count edge crossings:
 * odd number of crossings = inside, even = outside.
 *
 * @param point The point to test (x, y coordinates)
 * @param polygon The vertices of the polygon in order
 * @return true if the point is inside the polygon, false otherwise
 */
fun isPointInsidePolygon(point: Pair<Double, Double>, polygon: List<Pair<Int, Int>>): Boolean {
    val (x, y) = point
    var isInside = false

    var previousIndex = polygon.size - 1
    for (currentIndex in polygon.indices) {
        val (currentX, currentY) = polygon[currentIndex]
        val (previousX, previousY) = polygon[previousIndex]

        // For the ray to cross an edge, two conditions must be met:
        // 1. The edge must straddle the y-coordinate: one endpoint above y, one below
        // 2. The crossing point must be to the right of x (in the ray's path)
        if ((currentY > y) != (previousY > y) &&
            x < (previousX - currentX) * (y - currentY) / (previousY - currentY) + currentX
        ) {
            isInside = !isInside
        }
        previousIndex = currentIndex
    }

    return isInside
}
