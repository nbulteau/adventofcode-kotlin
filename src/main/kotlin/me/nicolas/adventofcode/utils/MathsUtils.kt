package me.nicolas.adventofcode.utils

import kotlin.math.abs

/**
 * GCD (Greatest Common Divisor) or HCF (Highest Common Factor) of two numbers is the largest number that divides both of them.
 */
tailrec fun Long.gcd(other: Long): Long =
    if (other == 0L) this
    else other.gcd(this % other)

/**
 * LCM (Least Common Multiple) of two numbers is the smallest number that is divisible by both.
 * The formula to find the LCM of some numbers a and b is (a*b) / gcd(a, b) where GCD is the Greatest Common Divisor.
 */
fun Long.lcm(other: Long): Long =
    (this * other) / this.gcd(other)

/**
 * LCM (Least Common Multiple) for a list of numbers
 */
fun List<Long>.lcm(): Long {
    return this.reduce { acc, i -> (acc * i) / acc.gcd(i) }.toLong()
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
    var a = 0L
    for (i in 0 until size - 1) {
        a += points[i].first * points[i + 1].second - points[i + 1].first * points[i].second
    }
    return abs(a + points[size - 1].first * points[0].second - points[0].first * points[size - 1].second) / 2
}