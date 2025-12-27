package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 3: Spiral Memory ---
// https://adventofcode.com/2017/day/3
fun main() {
    val data = "265149"
    val day = Day03()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day03(year: Int = 2017, day: Int = 3, title: String = "Spiral Memory") : AdventOfCodeDay(year, day, title) {

    /**
     * Part One:
     * The spiral fills outward in concentric square "rings" (layers).
     * Each ring has an odd side length m (1,3,5,7,...). The maximum value
     * in a ring is m*m and the ring index (distance from center) is (m-1)/2.
     * For a given n, we locate its ring by finding the smallest odd m with m*m >= n.
     * On that ring the Manhattan distance from center equals the ring index plus
     * the offset from the closest midpoint of any side. The midpoints of the four
     * sides are located at maxVal - ring - k*(sideLen) for k = 0..3. We compute
     * the minimum absolute difference between n and these midpoints and add the ring.
     */
    fun partOne(data: String): Int {
        val trimmed = data.trim()
        if (trimmed.isEmpty()) return 0
        val n = trimmed.toIntOrNull() ?: return 0
        if (n <= 1) return 0

        /**
         * Find the ring (layer) where n is located.
         * m is the side length of the square ring (odd number), maxVal is m*m
         */
        var m = kotlin.math.ceil(kotlin.math.sqrt(n.toDouble())).toInt()
        if (m % 2 == 0) m += 1
        val ring = (m - 1) / 2
        val maxVal = m * m
        val sideLen = m - 1

        /** Midpoints of the four sides are at maxVal - ring - k*sideLen for k in 0..3 */
        var minOffset = Int.MAX_VALUE
        for (k in 0..3) {
            val midpoint = maxVal - ring - k * sideLen
            val offset = kotlin.math.abs(n - midpoint)
            if (offset < minOffset) minOffset = offset
        }

        return ring + minOffset
    }

    /**
     * Part Two:
     * We build the spiral by coordinates, starting at (0,0) = 1. Then we move in
     * the pattern: right 1, up 1, left 2, down 2, right 3, up 3, ... increasing
     * the step size after every two directions. For each new coordinate we compute
     * the value as the sum of all 8 surrounding neighbors that have already been
     * assigned. We store values in a map keyed by Pair(x,y). The algorithm stops
     * and returns the first value strictly greater than the target.
     */
    fun partTwo(data: String): Int {
        val trimmed = data.trim()
        if (trimmed.isEmpty()) return 0
        val target = trimmed.toIntOrNull() ?: return 0
        if (target < 1) return 0

        /** Map of coordinates to values */
        val values = mutableMapOf<Pair<Int, Int>, Int>()
        values[Pair(0, 0)] = 1

        /** Directions: right, up, left, down */
        val dirs = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
        var x = 0
        var y = 0
        var stepSize = 1

        /**
         * Sum the values of the 8 neighbors around (px,py). If a neighbor is not
         * present in the map it contributes 0.
         */
        fun neighSum(px: Int, py: Int): Int {
            var sum = 0
            for (dx in -1..1) for (dy in -1..1) {
                if (dx == 0 && dy == 0) continue
                sum += values.getOrDefault(Pair(px + dx, py + dy), 0)
            }
            return sum
        }

        // Build the spiral until we find a value greater than target. We return
        // directly from inside the nested loops when the condition is met; no label
        // is necessary.
        while (true) {
            for (dirIndex in dirs.indices) {
                val dx = dirs[dirIndex].first
                val dy = dirs[dirIndex].second
                val steps = stepSize
                repeat(steps) {
                    x += dx
                    y += dy
                    val v = neighSum(x, y)
                    values[Pair(x, y)] = v
                    if (v > target) return v
                }

                // increase step size after completing two directions (up to match
                // the spiral pattern). In this code we increment after the 2nd and 4th
                // directions within each outer iteration, which produces the sequence
                // 1,1,2,2,3,3,... as required.
                if (dirIndex == 1 || dirIndex == 3) {
                    stepSize += 1
                }
            }
        }
    }
}