package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 15: Oxygen System ---
// https://adventofcode.com/2019/day/15
fun main() {
    val data = readFileDirectlyAsText("/year2019/day15/data.txt")
    val day = Day15(2019, 15, "Oxygen System")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

/**
 * Oxygen System
 *
 * Problem: Control a repair droid using an IntCode program to explore an unknown grid.
 * The droid can move in 4 directions (N/S/E/W) and reports back status codes.
 *
 * Part 1: Find the shortest path from start (0,0) to the oxygen system.
 * Part 2: Calculate how long it takes for oxygen to fill all accessible spaces.
 *
 * Solution approach:
 * 1. Use DFS to explore and map the entire accessible area
 * 2. Use BFS to find the shortest paths (guaranteed shortest in unweighted grid)
 */
class Day15(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {

    // Represents a position in the 2D grid
    data class Point(val x: Int, val y: Int)

    /**
     * Movement directions with their IntCode command codes and delta coordinates.
     * The droid accepts commands 1-4 to move in these directions.
     */
    enum class Direction(val code: Long, val dx: Int, val dy: Int) {
        NORTH(1, 0, -1),
        SOUTH(2, 0, 1),
        WEST(3, -1, 0),
        EAST(4, 1, 0);

        /**
         * Returns the opposite direction for backtracking during exploration.
         * Essential for DFS to return to previous positions after exploring a branch.
         */
        fun reverse(): Direction = when(this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            WEST -> EAST
            EAST -> WEST
        }
    }

    /**
     * Status codes returned by the repair droid after a movement command.
     * - WALL (0): Movement blocked, position unchanged
     * - MOVED (1): Successfully moved to new position
     * - OXYGEN (2): Moved to oxygen system location (goal found!)
     */
    enum class Status(val code: Long) {
        WALL(0),
        MOVED(1),
        OXYGEN(2);

        companion object {
            fun fromCode(code: Long): Status = entries.firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("Unknown status code: $code")
        }
    }

    /**
     * Find the shortest path from start to oxygen system.
     *
     * Algorithm:
     * 1. Explore the entire grid using DFS to build a complete map
     * 2. Use BFS on the known map to find shortest path
     *
     * Time complexity: O(N) where N is number of accessible cells
     */
    fun partOne(data: String): Int {
        val program = data.trim().split(",").map { it.toLong() }
        val (map, oxygenPosition) = exploreMap(program)

        // BFS guarantees shortest path in unweighted grid
        return findShortestPath(map, Point(0, 0), oxygenPosition)
    }

    /**
     * Calculate time for oxygen to fill all accessible spaces.
     *
     * Simulates oxygen spreading from the oxygen system to all reachable cells.
     * Oxygen spreads to adjacent cells each minute (like a flood fill).
     *
     * Time complexity: O(N) where N is number of accessible cells
     */
    fun partTwo(data: String): Int {
        val program = data.trim().split(",").map { it.toLong() }
        val (map, oxygenPosition) = exploreMap(program)

        // BFS to find maximum distance (time) from oxygen source
        return fillWithOxygen(map, oxygenPosition)
    }

    /**
     * Explore the unknown grid using Depth-First Search (DFS) with backtracking.
     *
     * The IntCode program maintains the droid's actual position, so we must:
     * 1. Try moving in a direction (send command to IntCode)
     * 2. Check the status response (wall, moved, or oxygen)
     * 3. If moved successfully, recursively explore from new position
     * 4. Backtrack by moving in reverse direction to return to previous position
     *
     * This builds a complete map of all accessible cells and finds the oxygen system.
     *
     * @param program The IntCode program that controls the repair droid
     * @return Pair of (complete map, oxygen system position)
     */
    private fun exploreMap(program: List<Long>): Pair<MutableMap<Point, Status>, Point> {
        val intCode = IntCodeProgram(program)
        val map = mutableMapOf<Point, Status>()
        var currentPos = Point(0, 0)
        var oxygenPosition: Point? = null

        // Mark starting position as accessible
        map[currentPos] = Status.MOVED

        /**
         * Recursive DFS exploration function.
         * Explores all four directions from current position, backtracking after each.
         */
        fun explore(pos: Point) {
            // Try all four directions
            for (direction in Direction.entries) {
                val nextPos = Point(pos.x + direction.dx, pos.y + direction.dy)

                // Skip if already explored (prevents infinite loops)
                if (nextPos in map) continue

                // Send movement command to droid and get status response
                val output = intCode.executeUntilOutput(mutableListOf(direction.code))
                requireNotNull(output) { "IntCode program terminated without producing status output when moving $direction from $pos to $nextPos" }
                val status = Status.fromCode(output)
                map[nextPos] = status

                when (status) {
                    Status.WALL -> {
                        // Hit a wall - droid didn't move, no need to backtrack
                        // Just record the wall location and continue
                    }
                    Status.MOVED, Status.OXYGEN -> {
                        // Successfully moved to new position
                        currentPos = nextPos

                        // Record oxygen system location if found
                        if (status == Status.OXYGEN) {
                            oxygenPosition = nextPos
                        }

                        // Recursively explore from this new position
                        explore(nextPos)

                        // CRITICAL: Backtrack to previous position
                        // The IntCode program tracks actual droid position, so we must
                        // physically move back to continue exploring other branches
                        val back = intCode.executeUntilOutput(mutableListOf(direction.reverse().code))
                        requireNotNull(back) { "IntCode program terminated while backtracking from $nextPos to $pos" }
                        currentPos = pos
                    }
                }
            }
        }

        // Start exploration from origin
        explore(currentPos)
        return Pair(map, requireNotNull(oxygenPosition) { "Oxygen system not found during exploration" })
    }

    /**
     * Find the shortest path between two points using Breadth-First Search (BFS).
     *
     * BFS guarantees the shortest path in an unweighted grid because it explores
     * all cells at distance N before exploring any cells at distance N+1.
     *
     * Algorithm:
     * 1. Start with origin in queue at distance 0
     * 2. For each position, try all 4 directions
     * 3. Add unvisited, non-wall neighbors to queue with distance+1
     * 4. First time we reach target is guaranteed to be shortest path
     *
     * @param map The complete map of the grid with cell statuses
     * @param start Starting position
     * @param end Target position (oxygen system)
     * @return Minimum number of steps from start to end
     */
    private fun findShortestPath(map: Map<Point, Status>, start: Point, end: Point): Int {
        // Queue stores (position, distance from start)
        val queue = ArrayDeque<Pair<Point, Int>>()
        val visited = mutableSetOf<Point>()

        // Initialize with starting position at distance 0
        queue.add(Pair(start, 0))
        visited.add(start)

        while (queue.isNotEmpty()) {
            val (pos, distance) = queue.removeFirst()

            // Found the target! Return distance
            if (pos == end) {
                return distance
            }

            // Explore all 4 adjacent cells
            for (direction in Direction.entries) {
                val nextPos = Point(pos.x + direction.dx, pos.y + direction.dy)

                // Add to queue if: not visited, exists in map, and not a wall
                if (nextPos !in visited && nextPos in map && map[nextPos] != Status.WALL) {
                    visited.add(nextPos)
                    queue.add(Pair(nextPos, distance + 1))
                }
            }
        }

        // Should never reach here if map is valid
        return -1
    }

    /**
     * Simulate oxygen filling the entire accessible area using BFS.
     *
     * This is essentially a flood fill algorithm. Oxygen starts at the oxygen system
     * and spreads to all adjacent non-wall cells each minute. We track the maximum
     * time it takes to reach any cell.
     *
     * Algorithm:
     * 1. Start with oxygen system at time 0
     * 2. Each minute, oxygen spreads to all adjacent unfilled cells
     * 3. Track the maximum time encountered
     * 4. Return max time when all reachable cells are filled
     *
     * @param map The complete map of the grid
     * @param oxygenStart Position of the oxygen system
     * @return Time in minutes for oxygen to fill all accessible spaces
     */
    private fun fillWithOxygen(map: Map<Point, Status>, oxygenStart: Point): Int {
        // Queue stores (position, time when oxygen reaches this position)
        val queue = ArrayDeque<Pair<Point, Int>>()
        val filled = mutableSetOf<Point>()

        // Start with oxygen system at time 0
        queue.add(Pair(oxygenStart, 0))
        filled.add(oxygenStart)

        var maxTime = 0

        while (queue.isNotEmpty()) {
            val (pos, time) = queue.removeFirst()
            // Track the maximum time to reach any cell
            maxTime = maxOf(maxTime, time)

            // Oxygen spreads to all 4 adjacent cells
            for (direction in Direction.entries) {
                val nextPos = Point(pos.x + direction.dx, pos.y + direction.dy)

                // Spread to unfilled, exists in map, non-wall cells
                if (nextPos !in filled && nextPos in map && map[nextPos] != Status.WALL) {
                    filled.add(nextPos)
                    // Oxygen reaches this cell at time + 1
                    queue.add(Pair(nextPos, time + 1))
                }
            }
        }

        // Maximum time is the time to fill the farthest cell
        return maxTime
    }
}