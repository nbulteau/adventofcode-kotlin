package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.Point
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// https://adventofcode.com/2018/day/22
fun main() {
    val data = readFileDirectlyAsText("/year2018/day22/data.txt")
    val day = Day22(2018, 22, "Mode Maze")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day22(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private enum class RegionType(val risk: Int) {
        ROCKY(0), WET(1), NARROW(2)
    }

    private enum class Tool {
        TORCH, CLIMBING_GEAR, NEITHER
    }

    private fun parseInput(data: String): Pair<Int, Point> {
        val lines = data.trim().lines()
        val depth = lines[0].substringAfter("depth: ").toInt()
        val targetParts = lines[1].substringAfter("target: ").split(",")
        val target = Point(targetParts[0].toInt(), targetParts[1].toInt())

        return depth to target
    }

    /**
     * Calculates the geologic index for a given position based on the following rules:
     * - The mouth of the cave (0,0) has a geologic index of 0
     * - The target coordinates have a geologic index of 0
     * - If Y coordinate is 0, the geologic index is X * 16807
     * - If X coordinate is 0, the geologic index is Y * 48271
     * - Otherwise, the geologic index is the product of the erosion levels
     *   of the regions at (X-1, Y) and (X, Y-1)
     */
    private fun calculateGeologicIndex(x: Int, y: Int, target: Point, erosionLevels: MutableMap<Point, Int>): Int {
        return when {
            // Mouth of the cave
            x == 0 && y == 0 -> 0
            // Target location
            x == target.x && y == target.y -> 0
            // Top edge (Y = 0)
            y == 0 -> x * 16807
            // Left edge (X = 0)
            x == 0 -> y * 48271
            // Interior: multiply erosion levels of left and up neighbors
            else -> {
                val erosionLeft = erosionLevels[Point(x - 1, y)]!!
                val erosionUp = erosionLevels[Point(x, y - 1)]!!
                erosionLeft * erosionUp
            }
        }
    }

    /**
     * Calculates the erosion level for a region.
     * Formula: (geologic index + cave depth) modulo 20183
     * Note: This method appears to be misnamed - it calculates erosion level, not geologic index
     */
    private fun calculateErosionLevel(geologicIndex: Int, depth: Int)= (geologicIndex + depth) % 20183

    private fun getRegionType(erosionLevel: Int): RegionType {
        return when (erosionLevel % 3) {
            0 -> RegionType.ROCKY
            1 -> RegionType.WET
            else -> RegionType.NARROW
        }
    }

    /**
     * Calculates the total risk level for the rectangular region from (0,0) to the target.
     *
     * The risk level is determined by the region types in the cave system:
     * - Rocky regions: risk = 0
     * - Wet regions: risk = 1
     * - Narrow regions: risk = 2
     *
     * Algorithm:
     * 1. For each position (x, y) in the rectangle from (0,0) to target:
     *    a) Calculate the geologic index using the rules defined in calculateGeologicIndex
     *    b) Calculate the erosion level: (geologic index + depth) % 20183
     *    c) Store the erosion level for use in calculating neighboring cells
     * 2. For each position in the same rectangle:
     *    a) Determine the region type: erosion level % 3
     *    b) Add the region's risk value to the total
     * 3. Return the total risk level
     *
     * Note: We iterate twice because geologic index calculation for interior cells
     * depends on the erosion levels of their left and top neighbors. The first pass
     * ensures all erosion levels are calculated in the correct order (top-to-bottom,
     * left-to-right).
     */
    fun partOne(data: String): Int {
        val (depth, target) = parseInput(data)
        val erosionLevels = mutableMapOf<Point, Int>()

        // Calculate erosion levels for all points in the rectangle
        for (y in 0..target.y) {
            for (x in 0..target.x) {
                val geologicIndex = calculateGeologicIndex(x, y, target, erosionLevels)
                val erosionLevel = calculateErosionLevel(geologicIndex, depth)
                erosionLevels[Point(x, y)] = erosionLevel
            }
        }

        // Calculate total risk by summing each region's risk value
        var totalRisk = 0
        for (y in 0..target.y) {
            for (x in 0..target.x) {
                val erosionLevel = erosionLevels[Point(x, y)]!!
                val regionType = getRegionType(erosionLevel)
                totalRisk += regionType.risk
            }
        }

        return totalRisk
    }

    private fun getValidTools(regionType: RegionType): Set<Tool> {
        return when (regionType) {
            RegionType.ROCKY -> setOf(Tool.CLIMBING_GEAR, Tool.TORCH)
            RegionType.WET -> setOf(Tool.CLIMBING_GEAR, Tool.NEITHER)
            RegionType.NARROW -> setOf(Tool.TORCH, Tool.NEITHER)
        }
    }

    private fun buildErosionMap(depth: Int, target: Point, maxX: Int, maxY: Int): Map<Point, Int> {
        val erosionLevels = mutableMapOf<Point, Int>()

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                val geologicIndex = calculateGeologicIndex(x, y, target, erosionLevels)
                val erosionLevel = calculateErosionLevel(geologicIndex, depth)
                erosionLevels[Point(x, y)] = erosionLevel
            }
        }

        return erosionLevels
    }

    /**
     * Finds the shortest time to reach the target with the torch equipped.
     *
     * This is the shortest path problem with constraints:
     * - Start at (0,0) with torch equipped
     * - Must reach target with torch equipped
     * - Moving to adjacent cell takes 1 minute
     * - Switching tools takes 7 minutes
     * - Each region type restricts which tools can be used:
     *   - Rocky: climbing gear or torch only
     *   - Wet: climbing gear or neither only
     *   - Narrow: torch or neither only
     *
     * Solution uses Dijkstra's algorithm with states represented as (position, tool, time):
     * 1. Use a priority queue to always explore the state with minimum time first
     * 2. Track the best time to reach each (position, tool) combination
     * 3. From each state, try two types of moves:
     *    a) Switch to another valid tool for current region (adds 7 minutes)
     *    b) Move to adjacent cell if current tool is valid there (adds 1 minute)
     * 4. Only add states to queue if they improve on the best known time
     * 5. Return immediately when reaching target with torch
     *
     * The search area extends beyond the target (buffer zone) because the optimal
     * path might involve going around certain regions.
     */
    fun partTwo(data: String): Int {
        val (depth, target) = parseInput(data)

        // Build a map larger than target to allow for exploration beyond target
        val buffer = 100
        val maxX = target.x + buffer
        val maxY = target.y + buffer
        val erosionMap = buildErosionMap(depth, target, maxX, maxY)

        // State: (point, tool, time) - represents a position with a specific tool at a specific time
        data class SearchState(val point: Point, val tool: Tool, val time: Int) : Comparable<SearchState> {
            // Priority queue will prioritize states with minimum time
            override fun compareTo(other: SearchState): Int = this.time.compareTo(other.time)
        }

        val start = SearchState(Point(0, 0), Tool.TORCH, 0)

        // Track the best time to reach each (point, tool) state to avoid reprocessing worse paths
        val bestTime = mutableMapOf<Pair<Point, Tool>, Int>()
        bestTime[Point(0, 0) to Tool.TORCH] = 0

        val queue = java.util.PriorityQueue<SearchState>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val currentState = current.point to current.tool

            // Check if we reached the target with torch
            if (current.point == target && current.tool == Tool.TORCH) {
                return current.time
            }

            // Skip if we've already found a better path to this state
            if (bestTime[currentState]?.let { it < current.time } == true) {
                continue
            }

            val currentRegionType = getRegionType(erosionMap[current.point]!!)
            val validTools = getValidTools(currentRegionType)

            // Option 1: Switch tool (takes 7 minutes)
            for (newTool in validTools) {
                if (newTool != current.tool) {
                    val newTime = current.time + 7
                    val newState = current.point to newTool

                    if (bestTime[newState]?.let { it <= newTime } != true) {
                        bestTime[newState] = newTime
                        queue.add(SearchState(current.point, newTool, newTime))
                    }
                }
            }

            // Option 2: Move to adjacent regions (takes 1 minute)
            val directions = listOf(
                Point(current.point.x + 1, current.point.y),
                Point(current.point.x - 1, current.point.y),
                Point(current.point.x, current.point.y + 1),
                Point(current.point.x, current.point.y - 1)
            )

            for (nextPoint in directions) {
                // Check bounds
                if (nextPoint.x < 0 || nextPoint.y < 0 ||
                    nextPoint.x > maxX || nextPoint.y > maxY) {
                    continue
                }

                val nextErosion = erosionMap[nextPoint] ?: continue
                val nextRegionType = getRegionType(nextErosion)
                val nextValidTools = getValidTools(nextRegionType)

                // Can only move if current tool is valid in next region
                if (current.tool in nextValidTools) {
                    val newTime = current.time + 1
                    val newState = nextPoint to current.tool

                    if (bestTime[newState]?.let { it <= newTime } != true) {
                        bestTime[newState] = newTime
                        queue.add(SearchState(nextPoint, current.tool, newTime))
                    }
                }
            }
        }

        return -1 // Should never reach here
    }
}