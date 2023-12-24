package me.nicolas.adventofcode.year2023

import com.microsoft.z3.*
import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day24/data.txt")
    val day = Day24(2023, 24, "Never Tell Me The Odds")
    prettyPrintPartOne { day.partOne(data, Pair(200000000000000, 400000000000000)) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day24(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String, area: Pair<Long, Long>): Long {
        val hailstones = parseData(data)

        return countIntersections(hailstones, area)
    }

    fun partTwo(data: String): Long {
        val hailstones = parseData(data)

        // https://github.com/Z3Prover/z3/blob/master/examples/java/JavaExample.java
        // Use Z3 to find the time t when the hailstones are at the same position.
        Context().use { context ->
            with(context) {
                // Z3 API is not very Kotlin friendly, so we need to add some extensions.
                operator fun <R : ArithSort> ArithExpr<R>.times(other: Expr<out R>): Expr<R> = mkMul(this, other)
                operator fun <R : ArithSort> ArithExpr<R>.plus(other: Expr<out R>): Expr<R> = mkAdd(this, other)
                infix fun <R : Sort> Expr<R>.eq(other: Expr<R>): Expr<BoolSort>? = mkEq(this, other)
                fun Long.real() = mkReal(this)
                operator fun Model.get(x: Expr<RealSort>) = (getConstInterp(x) as RatNum).let {
                    it.bigIntNumerator.toBigDecimal().divide(it.bigIntDenominator.toBigDecimal())
                }

                // We need to find the time t when the hailstones are at the same position.
                val solver = mkSolver()
                val x = mkRealConst("x")
                val y = mkRealConst("y")
                val z = mkRealConst("z")
                val vx = mkRealConst("vx")
                val vy = mkRealConst("vy")
                val vz = mkRealConst("vz")

                // three stones are enough to get all variables (6 + 3 variables).
                hailstones.take(3).forEachIndexed { i, hailstone ->
                    val t = mkRealConst("t$i")
                    // t > 0
                    solver.add(mkGe(t, 0L.real()))
                    // x + vx * t = hx + hvx * t
                    solver.add(x + vx * t eq hailstone.position.x.real() + hailstone.velocity.first.real() * t)
                    // y + vy * t = hy + hvy * t
                    solver.add(y + vy * t eq hailstone.position.y.real() + hailstone.velocity.second.real() * t)
                    // z + vz * t = hz + hvz * t
                    solver.add(z + vz * t eq hailstone.position.z.real() + hailstone.velocity.third.real() * t)
                }

                return when (solver.check()) {
                    Status.UNSATISFIABLE -> error("UNSATISFIABLE")
                    null, Status.UNKNOWN -> error("UNKNOWN")
                    Status.SATISFIABLE -> {
                        solver.model[x].toLong() + solver.model[y].toLong() + solver.model[z].toLong()
                    }
                }
            }
        }
    }

    // Point3D is used to represent the position of a hailstone.
    private data class Point3D(val x: Long, val y: Long, val z: Long)

    // The positions indicate where the hailstones are right now (at time 0).
    // The velocities are constant and indicate exactly how far each hailstone will move in one nanosecond.
    private data class Hailstone(val position: Point3D, val velocity: Triple<Long, Long, Long>)

    /**
     * Returns the intersection point of two lines.
     * Returns null if the lines are parallel.
     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
     * https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
     */
    private fun intersect(hailstone1: Hailstone, hailstone2: Hailstone): Pair<Double, Double>? {
        // Calculate the slope and y-intercept of each line. (y = mx + b)
        val m1 = hailstone1.velocity.second.toDouble() / hailstone1.velocity.first
        val b1 = -m1 * hailstone1.position.x + hailstone1.position.y
        val m2 = hailstone2.velocity.second.toDouble() / hailstone2.velocity.first
        val b2 = -m2 * hailstone2.position.x + hailstone2.position.y

        // Calculate the intersection point.
        val x = if (m1 == m2) {
            // Parallel lines.
            return null
        } else {
            // Not parallel lines.
            (b2 - b1) / (m1 - m2)
        }
        val y = m1 * x + b1

        // Calculate the time t when the hailstones are at the intersection point.
        val t1 = (x - hailstone1.position.x) / hailstone1.velocity.first
        val t2 = (x - hailstone2.position.x) / hailstone2.velocity.first

        // Only if t1 and t2 are positive (not in the past).
        return if (t1 >= 0 && t2 >= 0) {
            Pair(x, y)
        } else {
            null
        }
    }

    // Count the number of intersections between all hailstones.
    private fun countIntersections(hailstones: List<Hailstone>, area: Pair<Long, Long>): Long {
        var intersections = 0L
        for (i in hailstones.indices) {
            for (j in i + 1 until hailstones.size) {
                // p0 is the intersection point.
                val p0 = intersect(hailstones[i], hailstones[j])
                // If the intersection point is inside the area, then we have an intersection.
                // area.first is the lower bound and area.second is the upper bound.
                // area.first <= p0.first <= area.second && area.first <= p0.second <= area.second
                if (p0 != null
                    && p0.first >= area.first && p0.first <= area.second
                    && p0.second >= area.first && p0.second <= area.second
                ) {
                    intersections++
                }
            }
        }
        return intersections
    }

    private fun parseData(data: String): List<Hailstone> {
        return data.lines().map { line ->
            val (position, velocity) = line.split("@").map { it.trim() }
            val (x, y, z) = position.split(",").map { it.trim().toLong() }
            val (vx, vy, vz) = velocity.split(",").map { it.trim().toLong() }

            Hailstone(Point3D(x, y, z), Triple(vx, vy, vz))
        }
    }
}



