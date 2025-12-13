package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.ArrayDeque

// --- Day 20: Donut Maze ---
// https://adventofcode.com/2019/day/20
fun main() {
    val data = readFileDirectlyAsText("/year2019/day20/data.txt")
    val day = Day20(2019, 20)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day20(year: Int, day: Int, title: String = "Donut Maze") : AdventOfCodeDay(year, day, title) {
    /**
     * Part 1: find the shortest path in a flat maze where portals instantly teleport
     * between matching labels. Approach:
     *  - Parse the grid and identify portal entrances (the '.' tile adjacent to a two-letter label).
     *  - Build a map of portal entrances to their counterpart entrance.
     *  - Use a standard BFS (unweighted shortest path) from the AA entrance to the ZZ entrance.
     *    - From each open tile, we can move to adjacent open tiles (up/down/left/right).
     *    - If the current tile is a portal entrance (not AA/ZZ), we can also take a single step
     *      to the matching portal entrance elsewhere on the grid.
     *  - Return the distance in steps when we first reach ZZ.
     */
    fun partOne(data: String): Int {
        if (data.trim().isEmpty()) return 0
        val maze = Maze.parse(data)
        return maze.shortestStepsFlat()
    }

    /**
     * Part 2: the maze is recursive. Portals on the outer edge move you outward one level,
     * portals on the inner ring move you inward one level. AA and ZZ only operate at the
     * outermost level (level 0). Approach:
     *  - Reuse the parsed maze and portal mapping from Part 1.
     *  - Perform a BFS overstates (position, level) rather than only position.
     *    - Moving to adjacent tiles preserves the level.
     *    - Taking an inner portal increases the level by 1 (go deeper).
     *    - Taking an outer portal decreases the level by 1 (go outward) but only if level > 0.
     *    - AA and ZZ are treated as start/end only at level 0; they are not used as portals.
     *  - The search finishes when we reach the ZZ tile at level 0.
     */
    fun partTwo(data: String): Int {
        if (data.trim().isEmpty()) return 0
        val maze = Maze.parse(data)
        return maze.shortestStepsRecursive()
    }

    /**
     * Debug helper: return a textual description of detected portals and portalMap.
     * Useful in tests to inspect how portals were parsed and classified (outer vs inner).
     */
    fun debugMaze(data: String): String {
        val maze = Maze.parse(data)
        val sb = StringBuilder()
        sb.append("Portals (label -> entrances):\n")
        for ((label, pts) in maze.portals) {
            sb.append("  $label -> ${pts.joinToString { "(${it.x},${it.y})" }}\n")
        }
        // compute corridor bounding box for debug
        val dots = mutableListOf<Point>()
        for (y in maze.grid.indices) for (x in maze.grid[0].indices) if (maze.grid[y][x] == '.') dots.add(Point(x, y))
        val minDotX = dots.minOfOrNull { it.x } ?: 0
        val maxDotX = dots.maxOfOrNull { it.x } ?: 0
        val minDotY = dots.minOfOrNull { it.y } ?: 0
        val maxDotY = dots.maxOfOrNull { it.y } ?: 0
        sb.append("Corridor bbox: x=[$minDotX..$maxDotX], y=[$minDotY..$maxDotY]\n")
        sb.append("PortalMap (entrance -> (other, isOuter)):\n")
        for ((entr, pair) in maze.portalMap) {
            val (other, isOuter) = pair
            sb.append("  (${entr.x},${entr.y}) -> (${other.x},${other.y}), outer=$isOuter\n")
        }
        sb.append("Start: (${maze.start.x},${maze.start.y})\n")
        sb.append("End: (${maze.end.x},${maze.end.y})\n")
        return sb.toString()
    }

    private data class Point(val x: Int, val y: Int)

    private class Maze(
        val grid: Array<CharArray>,
        val portals: Map<String, MutableList<Point>>,
        val portalMap: Map<Point, Pair<Point, Boolean>>, // maps entrance -> (otherEntrance, isOuter)
        val start: Point,
        val end: Point
    ) {
        companion object {
            fun parse(input: String): Maze {
                val lines = input.split('\n')
                val width = lines.maxOfOrNull { it.length } ?: 0
                val height = lines.size
                val grid = Array(height) { y ->
                    val line = lines[y]
                    val arr = CharArray(width) { ' ' }
                    for (x in line.indices) arr[x] = line[x]
                    arr
                }

                // portals: map label -> list of entrance points ('.' tiles)
                val portals = mutableMapOf<String, MutableList<Point>>()
                // entranceToLetter: map entrance point -> the coordinate of the nearest label letter (for outer/inner detection)
                val entranceToLetter = mutableMapOf<Point, Point>()

                fun isLetter(c: Char) = c in 'A'..'Z'

                // For each letter, check right and down to form labels
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val c = grid[y][x]
                        if (!isLetter(c)) continue

                        // horizontal label
                        // only process horizontal pair if this char is the first of the pair
                        if (x + 1 < width && isLetter(grid[y][x + 1]) && (x - 1 < 0 || !isLetter(grid[y][x - 1]))) {
                            val name = "${c}${grid[y][x + 1]}"
                            // check positions left or right for '.'
                            val pos: Point? = when {
                                x - 1 >= 0 && grid[y][x - 1] == '.' -> Point(x - 1, y)
                                x + 2 < width && grid[y][x + 2] == '.' -> Point(x + 2, y)
                                else -> null
                            }
                            if (pos != null) {
                                portals.computeIfAbsent(name) { mutableListOf() }.add(pos)
                                // map entrance to the nearest letter coordinate: if entrance is left, nearest is (x,y), else (x+1,y)
                                val letterPos = if (pos.x < x) Point(x, y) else Point(x + 1, y)
                                entranceToLetter[pos] = letterPos
                            }
                        }

                        // vertical label
                        // only process vertical pair if this char is the first of the pair
                        if (y + 1 < height && isLetter(grid[y + 1][x]) && (y - 1 < 0 || !isLetter(grid[y - 1][x]))) {
                            val name = "${c}${grid[y + 1][x]}"
                            val pos: Point? = when {
                                y - 1 >= 0 && grid[y - 1][x] == '.' -> Point(x, y - 1)
                                y + 2 < height && grid[y + 2][x] == '.' -> Point(x, y + 2)
                                else -> null
                            }
                            if (pos != null) {
                                portals.computeIfAbsent(name) { mutableListOf() }.add(pos)
                                // map entrance to nearest letter coordinate: if entrance is above, nearest is (x,y), else (x,y+1)
                                val letterPos = if (pos.y < y) Point(x, y) else Point(x, y + 1)
                                entranceToLetter[pos] = letterPos
                            }
                        }
                    }
                }

                val portalMap = mutableMapOf<Point, Pair<Point, Boolean>>()
                var start: Point? = null
                var end: Point? = null

                val maxX = width - 1
                val maxY = height - 1

                // compute bounding box of letter positions to detect outer ring of labels
                val letters = entranceToLetter.values.toList()
                val minLetterX = letters.minOfOrNull { it.x } ?: 0
                val maxLetterX = letters.maxOfOrNull { it.x } ?: 0
                val minLetterY = letters.minOfOrNull { it.y } ?: 0
                val maxLetterY = letters.maxOfOrNull { it.y } ?: 0

                fun isOuterByLetter(letterPos: Point): Boolean {
                    return letterPos.x == minLetterX || letterPos.x == maxLetterX || letterPos.y == minLetterY || letterPos.y == maxLetterY
                }

                // fallback: entrance-based edge heuristic
                fun isOuterByEntrance(entrance: Point): Boolean {
                    return entrance.x <= 2 || entrance.y <= 2 || entrance.x >= maxX - 2 || entrance.y >= maxY - 2
                }

                for ((label, pts) in portals) {
                    if (label == "AA") {
                        if (pts.size != 1) throw IllegalStateException("AA should have one position")
                        start = pts[0]
                        continue
                    }
                    if (label == "ZZ") {
                        if (pts.size != 1) throw IllegalStateException("ZZ should have one position")
                        end = pts[0]
                        continue
                    }
                    if (pts.size != 2) continue // some malformed maps might have something else
                    val a = pts[0]
                    val b = pts[1]
                    // determine outer/inner using the entrance position relative to corridor bounding box
                    val aIsOuter = entranceToLetter[a]?.let { isOuterByLetter(it) } ?: isOuterByEntrance(a)
                    val bIsOuter = entranceToLetter[b]?.let { isOuterByLetter(it) } ?: isOuterByEntrance(b)
                    portalMap[a] = Pair(b, aIsOuter)
                    portalMap[b] = Pair(a, bIsOuter)
                }

                if (start == null || end == null) throw IllegalStateException("Start or end not found")

                return Maze(grid, portals, portalMap, start, end)
            }
        }

        private fun neighbors(p: Point): Sequence<Point> = sequence {
            val dirs = arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(0, -1))
            for (d in dirs) {
                val nx = p.x + d[0]
                val ny = p.y + d[1]
                if (ny >= 0 && ny < grid.size && nx >= 0 && nx < grid[0].size && grid[ny][nx] == '.') {
                    yield(Point(nx, ny))
                }
            }
        }

        /**
         * BFS for part 1 (flat maze).
         *
         * Standard BFS over positions only. We keep a visited set of positions to avoid cycles.
         * From each position we push neighbor open tiles with distance+1. If the position is a
         * portal entrance (present in portalMap) we may also enqueue the paired portal entrance
         * with distance+1. AA and ZZ are only used as start/end and not as teleports.
         */
        fun shortestStepsFlat(): Int {
            val startP = start
            val endP = end
            val q = ArrayDeque<Pair<Point, Int>>()
            val visited = mutableSetOf<Point>()
            q.add(Pair(startP, 0))
            visited.add(startP)

            while (q.isNotEmpty()) {
                val (cur, dist) = q.removeFirst()
                if (cur == endP) return dist
                // walk
                for (n in neighbors(cur)) {
                    if (n !in visited) {
                        visited.add(n)
                        q.add(Pair(n, dist + 1))
                    }
                }
                // portal
                val portal = portalMap[cur]
                if (portal != null) {
                    val (other, _) = portal
                    if (other !in visited) {
                        visited.add(other)
                        q.add(Pair(other, dist + 1))
                    }
                }
            }
            return -1
        }

        /**
         * BFS for part 2 (recursive maze).
         *
         * We perform BFS over a state (position, level). The queue stores (state, distance).
         * Moving to adjacent tiles keeps the same level. If we are on a portal entrance, we check
         * whether it is an outer or inner portal (portalMap stores that boolean). Inner portals
         * increase level by 1; outer portals decrease level by 1 (only allowed when current level > 0).
         * AA and ZZ are only meaningful at level 0; they are not treated as portal teleporters.
         * We maintain a visited set of states to avoid revisiting the same (position, level). The
         * search ends when we reach the end position at level 0.
         */
        fun shortestStepsRecursive(): Int {
            val startP = start
            val endP = end
            data class State(val p: Point, val level: Int)
            val q = ArrayDeque<Pair<State, Int>>()
            val visited = mutableSetOf<State>()
            val startState = State(startP, 0)
            q.add(Pair(startState, 0))
            visited.add(startState)

            while (q.isNotEmpty()) {
                val (curState, dist) = q.removeFirst()
                val (cur, level) = curState
                if (cur == endP && level == 0) return dist

                // walk
                for (n in neighbors(cur)) {
                    val ns = State(n, level)
                    if (ns !in visited) {
                        visited.add(ns)
                        q.add(Pair(ns, dist + 1))
                    }
                }

                // portal (recursive rules)
                val portal = portalMap[cur]
                if (portal != null) {
                    // label AA and ZZ are not portals for recursion; ensured by portalMap not containing them
                    val (other, isOuter) = portal
                    if (isOuter) {
                        // outer: go up a level
                        if (level > 0) {
                            val ns = State(other, level - 1)
                            if (ns !in visited) {
                                visited.add(ns)
                                q.add(Pair(ns, dist + 1))
                            }
                        }
                    } else {
                        // inner: go deeper
                        val ns = State(other, level + 1)
                        if (ns !in visited) {
                            visited.add(ns)
                            q.add(Pair(ns, dist + 1))
                        }
                    }
                }
            }
            return -1
        }
    }
}