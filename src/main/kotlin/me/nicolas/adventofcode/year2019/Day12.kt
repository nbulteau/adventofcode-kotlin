package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.abs

// --- Day 12: The N-Body Problem ---
// https://adventofcode.com/2019/day/12

fun main() {
    val data = readFileDirectlyAsText("/year2019/day12/data.txt")
    val day = Day12(2019, 12, "The N-Body Problem", data)
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo() }
}

class Day12(year: Int, day: Int, title: String, private val data: String) : AdventOfCodeDay(year, day, title) {

    data class Vec3(var x: Int, var y: Int, var z: Int) {
        fun energy() = abs(x) + abs(y) + abs(z)

        operator fun plusAssign(other: Vec3) {
            x += other.x
            y += other.y
            z += other.z
        }

        fun copy() = Vec3(x, y, z)
    }

    data class Moon(val position: Vec3, val velocity: Vec3 = Vec3(0, 0, 0)) {
        fun potentialEnergy() = position.energy()
        fun kineticEnergy() = velocity.energy()
        fun totalEnergy() = potentialEnergy() * kineticEnergy()

        fun copy() = Moon(position.copy(), velocity.copy())
    }

    internal fun parseMoons(input: String): List<Moon> {
        val regex = """<x=(-?\d+), y=(-?\d+), z=(-?\d+)>""".toRegex()
        return input.lines().mapNotNull { line ->
            regex.matchEntire(line.trim())?.let { match ->
                val (x, y, z) = match.destructured
                Moon(Vec3(x.toInt(), y.toInt(), z.toInt()))
            }
        }
    }

    internal fun applyGravity(moons: List<Moon>) {
        for (i in moons.indices) {
            for (j in i + 1 until moons.size) {
                val moon1 = moons[i]
                val moon2 = moons[j]

                // Apply gravity on x-axis
                when {
                    moon1.position.x < moon2.position.x -> {
                        moon1.velocity.x++
                        moon2.velocity.x--
                    }
                    moon1.position.x > moon2.position.x -> {
                        moon1.velocity.x--
                        moon2.velocity.x++
                    }
                }

                // Apply gravity on y-axis
                when {
                    moon1.position.y < moon2.position.y -> {
                        moon1.velocity.y++
                        moon2.velocity.y--
                    }
                    moon1.position.y > moon2.position.y -> {
                        moon1.velocity.y--
                        moon2.velocity.y++
                    }
                }

                // Apply gravity on z-axis
                when {
                    moon1.position.z < moon2.position.z -> {
                        moon1.velocity.z++
                        moon2.velocity.z--
                    }
                    moon1.position.z > moon2.position.z -> {
                        moon1.velocity.z--
                        moon2.velocity.z++
                    }
                }
            }
        }
    }

    internal fun applyVelocity(moons: List<Moon>) {
        for (moon in moons) {
            moon.position += moon.velocity
        }
    }

    internal fun simulateStep(moons: List<Moon>) {
        applyGravity(moons)
        applyVelocity(moons)
    }

    fun partOne(): Int {
        // Parse the initial positions of the moons from input and create deep copies
        // to avoid mutating the original state
        val moons = parseMoons(data).map { moon -> moon.copy() }

        // Simulate 1000 time steps
        // Each step: apply gravity to update velocities, then apply velocity to update positions
        repeat(1000) {
            simulateStep(moons)
        }

        // Calculate and return the total energy in the system
        // Total energy = sum of each moon's (potential energy * kinetic energy)
        return moons.sumOf { moon -> moon.totalEnergy() }
    }

    fun partTwo(): Long {
        // Part 2 will be implemented later
        return 0L
    }
}

