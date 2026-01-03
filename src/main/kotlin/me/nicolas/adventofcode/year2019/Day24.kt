package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 24: Planet of Discord ---
// https://adventofcode.com/2019/day/24
fun main() {
    val data = readFileDirectlyAsText("/year2019/day24/data.txt")
    val day = Day24(2019, 24)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day24(year: Int, day: Int, title: String = "Planet of Discord") : AdventOfCodeDay(year, day, title) {
    companion object {
        private const val WIDTH = 5
        private const val HEIGHT = 5
        private const val CENTER_X = 2
        private const val CENTER_Y = 2
        private const val CENTER_INDEX = CENTER_Y * WIDTH + CENTER_X
        private const val INV_CENTER_MASK: Int = (Int.MAX_VALUE) xor (1 shl CENTER_INDEX)
    }

    /**
     * partOne:
     * Parse the provided 5x5 input into a 25-bit mask (Int) where bit i is 1 when
     * the tile contains a bug. Index mapping is row-major: index = y*WIDTH + x.
     *
     * Simulate the cellular automaton rules on a single level. On each minute,
     * compute the number of adjacent bugs for every tile (up/down/left/right),
     * then update all tiles simultaneously:
     *  - A bug survives only if it has exactly 1 adjacent bug.
     *  - An empty tile becomes a bug if it has 1 or 2 adjacent bugs.
     *
     * Keep a set of previous layouts (bitmasks). When the current layout
     * repeats one already seen, stop and return the biodiversity rating.
     *
     * Biodiversity is computed by summing 2^i for each tile i that contains a bug.
     */
    fun partOne(data: String): Int {
        val initial = parseToMask(data)
        val seen = mutableSetOf<Int>()
        var mask = initial
        while (!seen.contains(mask)) {
            seen.add(mask)
            mask = stepSingleLevel(mask)
        }
        return biodiversity(mask)
    }

    /**
     * partTwo: keep original signature for production (200 minutes).
     * Delegates to the overload that accepts a variable number of minutes to
     * facilitate unit testing (for example, the puzzle example uses 10 minutes).
     */
    fun partTwo(data: String): Int = partTwo(data, 200)

    /**
     * partTwo (testable overload):
     * Parse input into the base level (level 0) mask and force the center tile to
     * be empty (center is treated as a recursive portal and never contains a bug).
     *
     * Simulate recursive grids for `minutes` iterations. Each level is a 5x5
     * grid; neighbors can come from the same level, the inner level (level+1)
     * when stepping into the center, or the outer level (level-1) when stepping
     * off the edge of the grid.
     *
     * After `minutes` iterations, sum the set bits across all levels to return
     * the total number of bugs.
     */
    fun partTwo(data: String, minutes: Int): Int {
        val initial = parseToMask(data) and INV_CENTER_MASK // ensure center is empty for recursive rules
        val finalLevels = simulateRecursive(initial, minutes)

        return finalLevels.values.sumOf { Integer.bitCount(it) }
    }

    /**
     * Parse the input text (5 lines) into a 25-bit mask. Each '#' sets the bit
     * at index = y*WIDTH + x.
     */
    private fun parseToMask(data: String): Int {
        var mask = 0
        val lines = data.lines().filter { line -> line.isNotBlank() }
        for (y in 0 until HEIGHT) {
            val line = lines.getOrNull(y) ?: continue
            for (x in 0 until WIDTH) {
                if (line.getOrNull(x) == '#') {
                    val idx = y * WIDTH + x
                    mask = mask or (1 shl idx)
                }
            }
        }
        return mask
    }

    /**
     * Compute the next generation for a single-level grid represented by `mask`.
     * Returns the next mask after applying the life/death rules to every tile.
     */
    private fun stepSingleLevel(mask: Int): Int {
        var next = 0
        for (y in 0 until HEIGHT) {
            for (x in 0 until WIDTH) {
                val idx = y * WIDTH + x
                val alive = (mask shr idx) and 1 == 1
                var neighbors = 0
                // four directions
                val dirs = arrayOf(intArrayOf(0, -1), intArrayOf(0, 1), intArrayOf(-1, 0), intArrayOf(1, 0))
                for (d in dirs) {
                    val nx = x + d[0]
                    val ny = y + d[1]
                    if (nx in 0 until WIDTH && ny in 0 until HEIGHT) {
                        val nidx = ny * WIDTH + nx
                        if ((mask shr nidx) and 1 == 1) neighbors++
                    }
                }
                if (alive) {
                    if (neighbors == 1) next = next or (1 shl idx)
                } else {
                    if (neighbors == 1 || neighbors == 2) next = next or (1 shl idx)
                }
            }
        }
        return next
    }

    /**
     * Compute biodiversity rating for the given mask: sum(2^i) for every set bit i.
     */
    private fun biodiversity(mask: Int): Int {
        // sum of 2^i for each bit i set
        var sum = 0
        for (i in 0 until WIDTH * HEIGHT) {
            if ((mask shr i) and 1 == 1) sum += (1 shl i)
        }
        return sum
    }

    /**
     * Recursive simulation for part two.
     *
     * Implementation notes for neighbour counting across levels:
     * - When checking a neighbour that is the centre tile (2,2), it refers to
     *   multiple tiles in the inner level (level+1): depending on the direction
     *   into the centre, you count the corresponding full row or column on the
     *   inner grid (5 tiles).
     * - When a neighbour would be outside the 5x5 grid (off the edge), it maps
     *   to a single tile on the outer level (level-1) adjacent to the centre:
     *     left edge -> (1,2) on outer level
     *     right edge -> (3,2) on outer level
     *     top edge -> (2,1) on outer level
     *     bottom edge -> (2,3) on outer level
     * - The centre tile itself is never simulated as a regular tile in recursive
     *   mode; it acts as a portal only.
     *
     * The function returns a map level->mask containing non-empty levels only.
     */
    private fun simulateRecursive(initial: Int, minutes: Int): Map<Int, Int> {
        var levels = mutableMapOf<Int, Int>()
        levels[0] = initial and INV_CENTER_MASK

        val dirs = arrayOf(intArrayOf(0, -1), intArrayOf(0, 1), intArrayOf(-1, 0), intArrayOf(1, 0))

        repeat(minutes) {
            val newLevels = mutableMapOf<Int, Int>()
            val minLevel = levels.keys.minOrNull() ?: 0
            val maxLevel = levels.keys.maxOrNull() ?: 0
            for (level in (minLevel - 1)..(maxLevel + 1)) {
                var nextMask = 0
                for (y in 0 until HEIGHT) {
                    for (x in 0 until WIDTH) {
                        val idx = y * WIDTH + x
                        if (x == CENTER_X && y == CENTER_Y) continue // center is ignored
                        val alive = ((levels[level] ?: 0) shr idx) and 1 == 1
                        var neighbors = 0
                        for (d in dirs) {
                            val nx = x + d[0]
                            val ny = y + d[1]
                            when {
                                // neighbor is the center -> contributes from inner level (level+1)
                                nx == CENTER_X && ny == CENTER_Y -> {
                                    val inner = levels[level + 1] ?: 0
                                    if (d[0] == 0 && d[1] == -1) {
                                        // moved up into center -> count inner bottom row y=4
                                        for (ix in 0 until WIDTH) {
                                            val ii = 4 * WIDTH + ix
                                            if ((inner shr ii) and 1 == 1) neighbors++
                                        }
                                    } else if (d[0] == 0 && d[1] == 1) {
                                        // moved down into center -> count inner top row y=0
                                        for (ix in 0 until WIDTH) {
                                            val ii = 0 * WIDTH + ix
                                            if ((inner shr ii) and 1 == 1) neighbors++
                                        }
                                    } else if (d[0] == -1 && d[1] == 0) {
                                        // moved left into center -> count inner right column x=4
                                        for (iy in 0 until HEIGHT) {
                                            val ii = iy * WIDTH + 4
                                            if ((inner shr ii) and 1 == 1) neighbors++
                                        }
                                    } else if (d[0] == 1 && d[1] == 0) {
                                        // moved right into center -> count inner left column x=0
                                        for (iy in 0 until HEIGHT) {
                                            val ii = iy * WIDTH + 0
                                            if ((inner shr ii) and 1 == 1) neighbors++
                                        }
                                    }
                                }
                                // neighbor outside grid -> contributes from outer level (level-1)
                                nx < 0 -> {
                                    val outer = levels[level - 1] ?: 0
                                    val ii = 2 * WIDTH + 1 // (1,2)
                                    if ((outer shr ii) and 1 == 1) neighbors++
                                }
                                nx >= WIDTH -> {
                                    val outer = levels[level - 1] ?: 0
                                    val ii = 2 * WIDTH + 3 // (3,2)
                                    if ((outer shr ii) and 1 == 1) neighbors++
                                }
                                ny < 0 -> {
                                    val outer = levels[level - 1] ?: 0
                                    val ii = 1 * WIDTH + 2 // (2,1)
                                    if ((outer shr ii) and 1 == 1) neighbors++
                                }
                                ny >= HEIGHT -> {
                                    val outer = levels[level - 1] ?: 0
                                    val ii = 3 * WIDTH + 2 // (2,3)
                                    if ((outer shr ii) and 1 == 1) neighbors++
                                }
                                else -> {
                                    // normal neighbor in same level
                                    val nmask = levels[level] ?: 0
                                    val nidx = ny * WIDTH + nx
                                    if ((nmask shr nidx) and 1 == 1) neighbors++
                                }
                            }
                        }
                        if (alive) {
                            if (neighbors == 1) nextMask = nextMask or (1 shl idx)
                        } else {
                            if (neighbors == 1 || neighbors == 2) nextMask = nextMask or (1 shl idx)
                        }
                    }
                }
                if (nextMask != 0) newLevels[level] = nextMask
            }
            levels = newLevels
        }

        return levels
    }
}