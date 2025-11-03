package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import me.nicolas.adventofcode.utils.lcm
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

    internal data class Vec3(var x: Int, var y: Int, var z: Int) {
        fun energy() = abs(x) + abs(y) + abs(z)

        operator fun plusAssign(other: Vec3) {
            x += other.x
            y += other.y
            z += other.z
        }

        fun copy() = Vec3(x, y, z)
    }

    internal data class Moon(val position: Vec3, val velocity: Vec3 = Vec3(0, 0, 0)) {
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

    /**
     * Apply gravity between all pairs of moons to update their velocities.
     *
     * For each pair of moons, gravity is calculated independently on each axis (x, y, z).
     * If moon1's position on an axis is less than moon2's, moon1 accelerates toward moon2
     * (velocity increases) and moon2 accelerates toward moon1 (velocity decreases), and vice versa.
     * When positions are equal on an axis, no gravity is applied on that axis.
     */
    private fun applyGravity(moons: List<Moon>) {
        // Consider each unique pair of moons exactly once
        // Using i and j ensures we don't process the same pair twice
        for (i in moons.indices) {
            for (j in i + 1 until moons.size) {
                val moon1 = moons[i]
                val moon2 = moons[j]

                // Apply gravity on x-axis
                // If moon1 is to the left of moon2, moon1 is pulled right (+x) and moon2 is pulled left (-x)
                when {
                    moon1.position.x < moon2.position.x -> {
                        moon1.velocity.x++
                        moon2.velocity.x--
                    }

                    moon1.position.x > moon2.position.x -> {
                        moon1.velocity.x--
                        moon2.velocity.x++
                    }
                    // If positions are equal, no gravity is applied
                }

                // Apply gravity on y-axis
                // If moon1 is below moon2, moon1 is pulled up (+y) and moon2 is pulled down (-y)
                when {
                    moon1.position.y < moon2.position.y -> {
                        moon1.velocity.y++
                        moon2.velocity.y--
                    }

                    moon1.position.y > moon2.position.y -> {
                        moon1.velocity.y--
                        moon2.velocity.y++
                    }
                    // If positions are equal, no gravity is applied
                }

                // Apply gravity on z-axis
                // If moon1 is behind moon2, moon1 is pulled forward (+z) and moon2 is pulled backward (-z)
                when {
                    moon1.position.z < moon2.position.z -> {
                        moon1.velocity.z++
                        moon2.velocity.z--
                    }

                    moon1.position.z > moon2.position.z -> {
                        moon1.velocity.z--
                        moon2.velocity.z++
                    }
                    // If positions are equal, no gravity is applied
                }
            }
        }
    }

    private fun applyVelocity(moons: List<Moon>) {
        for (moon in moons) {
            moon.position += moon.velocity
        }
    }

    internal fun simulateStep(moons: List<Moon>) {
        applyGravity(moons)
        applyVelocity(moons)
    }

    /**
     * Part One: Calculate the total energy in the system after 1000 steps.
     *
     * The simulation follows these rules:
     * 1. Apply gravity: For each pair of moons, compare their positions on each axis.
     *    The moon with the lower position gets pulled toward the higher position (velocity increases),
     *    and vice versa. This happens independently on x, y, and z axes.
     * 2. Apply velocity: Each moon moves by adding its velocity to its position.
     *
     * Energy calculations:
     * - Potential energy: sum of absolute values of position coordinates (|x| + |y| + |z|)
     * - Kinetic energy: sum of absolute values of velocity coordinates (|vx| + |vy| + |vz|)
     * - Total energy per moon: potential energy × kinetic energy
     * - System total energy: sum of all moons' total energies
     *
     * @return The total energy in the system after 1000 simulation steps
     */
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

    /**
     * Part Two: Find when the system returns to a previous state.
     *
     * Problem: The system could take billions of steps to repeat, making brute force infeasible.
     *
     * Key Insight: Motion on each axis (x, y, z) is completely independent!
     * - Gravity on the x-axis only depends on x-positions and affects x-velocities
     * - Gravity on the y-axis only depends on y-positions and affects y-velocities
     * - Gravity on the z-axis only depends on z-positions and affects z-velocities
     *
     * Solution Strategy:
     * 1. Find the cycle length for x-axis: when all x-positions and x-velocities return to initial
     * 2. Find the cycle length for y-axis: when all y-positions and y-velocities return to initial
     * 3. Find the cycle length for z-axis: when all z-positions and z-velocities return to initial
     * 4. The full system repeats at the Least Common Multiple (LCM) of these three cycles
     *
     * Example: If x repeats every 18 steps, y every 28 steps, and z every 44 steps,
     * the full 3D system repeats at LCM(18, 28, 44) = 2772 steps.
     *
     * This optimization reduces the problem from tracking O(billions) 3D states
     * to tracking three separate 1D states that cycle much faster.
     *
     * @return The number of steps until the system returns to its initial state
     */
    fun partTwo(): Long {
        // Parse the initial positions of the moons
        val moons = parseMoons(data).map { it.copy() }

        // Find cycle length for each axis independently
        // The motion on each axis is independent, so we can find when each axis repeats
        val xCycle = findAxisCycle(moons.map { it.copy() }, { it.x })
        val yCycle = findAxisCycle(moons.map { it.copy() }, { it.y })
        val zCycle = findAxisCycle(moons.map { it.copy() }, { it.z })

        // The overall cycle is the LCM of all three axis cycles
        return xCycle.lcm(yCycle).lcm(zCycle)
    }

    /**
     * Find the cycle length for a single axis.
     *
     * Since each axis is independent, we can track just the positions and velocities for one axis.
     * We simulate the full 3D system but only check when the specified axis returns to its initial state.
     *
     * The axis parameter is a lambda that extracts the coordinate value from a Vec3:
     * - { it.x } extracts x-coordinate
     * - { it.y } extracts y-coordinate
     * - { it.z } extracts z-coordinate
     *
     * @param moons The list of moons to simulate (should be fresh copies)
     * @param axis Lambda function to extract the coordinate (x, y, or z) from a Vec3
     * @return The number of steps until this axis returns to its initial state
     */
    private fun findAxisCycle(moons: List<Moon>, axis: (Vec3) -> Int): Long {
        // Store initial state for this axis (positions and velocities)
        val initialPositions = moons.map { axis(it.position) }
        val initialVelocities = moons.map { axis(it.velocity) }

        var steps = 0L
        val currentMoons = moons.map { it.copy() }

        do {
            // Simulate one full step (all 3 dimensions, but we only check one axis)
            simulateStep(currentMoons)
            steps++

            // Check if current state matches initial state for this specific axis
            // All moons must have their axis position AND velocity match the initial values
            val matches = currentMoons.indices.all { i ->
                axis(currentMoons[i].position) == initialPositions[i] &&
                        axis(currentMoons[i].velocity) == initialVelocities[i]
            }
        } while (!matches)

        return steps
    }
}

