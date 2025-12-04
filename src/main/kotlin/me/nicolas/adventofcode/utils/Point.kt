package me.nicolas.adventofcode.utils

import kotlin.math.abs

data class Point(val x: Int, val y: Int) : Comparable<Point> {

    val up by lazy { Point(x, y - 1) }
    val down by lazy { Point(x, y + 1) }
    val left by lazy { Point(x - 1, y) }
    val right by lazy { Point(x + 1, y) }

    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    operator fun plus(delta: Pair<Int, Int>) = Point(x + delta.first, y + delta.second)

    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    fun distanceTo(otherX: Int, otherY: Int): Int = abs(x - otherX) + abs(y - otherY)

    fun distanceTo(other: Point): Int = distanceTo(other.x, other.y)

    fun cardinalNeighbors(): List<Point> =
        // Note: Generate in reading order!
        listOf(
            Point(x, y - 1),
            Point(x - 1, y),
            Point(x + 1, y),
            Point(x, y + 1)
        ).filter { it.x >= 0 && it.y >= 0 }

    fun neighbors(): List<Point> =
        // Note: Generate in reading order!
        listOf(
            Point(x-1, y-1), Point(x-1 to y), Point(x-1 to y+1),
            Point(x to y-1),                        Point(x to y+1),
            Point(x+1 to y-1),  Point(x+1 to y),  Point(x+1 to y+1)
        ).filter { it.x >= 0 && it.y >= 0 }

    fun isNeighbourWith(other: Point): Boolean {
        return other in this.cardinalNeighbors()
    }

    override fun compareTo(other: Point): Int =
        when {
            y < other.y -> -1
            y > other.y -> 1
            x < other.x -> -1
            x > other.x -> 1
            else -> 0
        }
}
