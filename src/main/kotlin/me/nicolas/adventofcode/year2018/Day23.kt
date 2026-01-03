package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.*


// https://adventofcode.com/2018/day/23
fun main() {
    val data = readFileDirectlyAsText("/year2018/day23/data.txt")
    val day = Day23(2018, 23, "Experimental Emergency Teleportation")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day23(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private data class Nanobot(val x: Long, val y: Long, val z: Long, val radius: Long) {
        /**
         * Calculates the Manhattan distance between this nanobot and another nanobot.
         * Manhattan distance = |x1 - x2| + |y1 - y2| + |z1 - z2|
         */
        fun manhattanDistance(other: Nanobot): Long {
            return kotlin.math.abs(x - other.x) + kotlin.math.abs(y - other.y) + kotlin.math.abs(z - other.z)
        }

        /**
         * Checks if another nanobot is within this nanobot's signal range.
         */
        fun isInRange(other: Nanobot): Boolean {
            return manhattanDistance(other) <= radius
        }

        /**
         * Checks if this nanobot's signal can reach any point within the given box.
         * Uses the minimum Manhattan distance from the nanobot to the box.
         */
        fun canReachBox(box: Box): Boolean {
            val dist = box.distanceToPoint(x, y, z)
            return dist <= radius
        }
    }

    private data class Box(
        val minX: Long,
        val minY: Long,
        val minZ: Long,
        val maxX: Long,
        val maxY: Long,
        val maxZ: Long
    ) {
        fun distanceToPoint(px: Long, py: Long, pz: Long): Long {
            val dx = maxOf(0L, maxOf(minX - px, px - maxX))
            val dy = maxOf(0L, maxOf(minY - py, py - maxY))
            val dz = maxOf(0L, maxOf(minZ - pz, pz - maxZ))
            return dx + dy + dz
        }

        fun distanceToOrigin(): Long {
            return distanceToPoint(0, 0, 0)
        }

        fun size(): Long {
            return maxOf(maxX - minX, maxOf(maxY - minY, maxZ - minZ))
        }

        fun subdivide(): List<Box> {
            if (size() == 0L) return emptyList()

            val midX = (minX + maxX) / 2
            val midY = (minY + maxY) / 2
            val midZ = (minZ + maxZ) / 2

            return listOf(
                Box(minX, minY, minZ, midX, midY, midZ),
                Box(midX + 1, minY, minZ, maxX, midY, midZ),
                Box(minX, midY + 1, minZ, midX, maxY, midZ),
                Box(midX + 1, midY + 1, minZ, maxX, maxY, midZ),
                Box(minX, minY, midZ + 1, midX, midY, maxZ),
                Box(midX + 1, minY, midZ + 1, maxX, midY, maxZ),
                Box(minX, midY + 1, midZ + 1, midX, maxY, maxZ),
                Box(midX + 1, midY + 1, midZ + 1, maxX, maxY, maxZ)
            ).filter { it.minX <= it.maxX && it.minY <= it.maxY && it.minZ <= it.maxZ }
        }
    }

    private fun parseNanobots(data: String): List<Nanobot> {
        // Parse lines like: pos=<0,0,0>, r=4
        val pattern = Regex("""pos=<(-?\d+),(-?\d+),(-?\d+)>,\s*r=(\d+)""")
        return data.lines()
            .filter { line -> line.isNotBlank() }
            .map { line ->
                val match = pattern.matchEntire(line) ?: error("Invalid line: $line")
                val (x, y, z, r) = match.destructured
                Nanobot(x.toLong(), y.toLong(), z.toLong(), r.toLong())
            }
    }

    /**
     * Part One: Find the nanobot with the largest signal radius and count how many nanobots
     * are within its range (using Manhattan distance).
     *
     * Algorithm:
     * 1. Parse all nanobots from input
     * 2. Find the strongest nanobot (the one with maximum radius)
     * 3. Count how many nanobots are within Manhattan distance <= strongest's radius
     *
     * @return The number of nanobots in range of the strongest nanobot
     */
    fun partOne(data: String): Int {
        val nanobots = parseNanobots(data)

        // Find the nanobot with the largest signal radius
        val strongest = nanobots.maxByOrNull { nanobot -> nanobot.radius } ?: return 0

        // Count how many nanobots are in range of the strongest
        return nanobots.count { nanobot -> strongest.isInRange(nanobot) }
    }

    /**
     * Part Two: Find the coordinate in range of the largest number of nanobots.
     * If multiple coordinates have the same count, choose the one closest to origin (0,0,0).
     *
     * Algorithm: Octree-based priority queue search
     * 1. Create initial bounding box containing all nanobots and their coverage areas
     * 2. Use a priority queue that prioritizes boxes by:
     *    - Maximum number of nanobots that can reach the box (descending)
     *    - Minimum distance to origin (ascending) - tiebreaker
     *    - Minimum box size (ascending) - secondary tiebreaker
     * 3. Iteratively:
     *    - Pop the best box from queue
     *    - If it's a single point (size 0), return its distance to origin
     *    - Otherwise, subdivide into 8 smaller boxes (octree subdivision)
     *    - Calculate how many nanobots can reach each sub-box
     *    - Add all sub-boxes back to the priority queue
     * 4. The algorithm converges to the optimal point
     *
     * @return Manhattan distance from the optimal coordinate to origin (0,0,0)
     */
    fun partTwo(data: String): Long {
        val nanobots = parseNanobots(data)

        // Find the bounding box that contains all nanobots and their ranges
        val minX = nanobots.minOf { nanobot -> nanobot.x - nanobot.radius }
        val maxX = nanobots.maxOf { nanobot -> nanobot.x + nanobot.radius }
        val minY = nanobots.minOf { nanobot -> nanobot.y - nanobot.radius }
        val maxY = nanobots.maxOf { nanobot -> nanobot.y + nanobot.radius }
        val minZ = nanobots.minOf { nanobot -> nanobot.z - nanobot.radius }
        val maxZ = nanobots.maxOf { nanobot -> nanobot.z + nanobot.radius }

        val initialBox = Box(minX, minY, minZ, maxX, maxY, maxZ)

        // Priority queue: prioritize by (max bots reached, min distance to origin, min box size)
        val queue = PriorityQueue(compareByDescending<Pair<Box, Int>> { it.second }
            .thenBy { it.first.distanceToOrigin() }
            .thenBy { it.first.size() })

        val initialCount = nanobots.count { it.canReachBox(initialBox) }
        queue.add(initialBox to initialCount)

        while (queue.isNotEmpty()) {
            val (box, _) = queue.poll()

            // If this is a single point, we found our answer
            if (box.size() == 0L) {
                return box.distanceToOrigin()
            }

            // Subdivide and add to queue
            for (subBox in box.subdivide()) {
                val subCount = nanobots.count { nanobot -> nanobot.canReachBox(subBox) }
                queue.add(subBox to subCount)
            }
        }

        return 0L
    }
}