package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.*
import java.util.*

// --- Day 18: Many-Worlds Interpretation ---
// https://adventofcode.com/2019/day/18
fun main() {
    val data = readFileDirectlyAsText("/year2019/day18/data.txt")
    val day = Day18(2019, 18)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day18(year: Int, day: Int, title: String = "Many-Worlds Interpretation") : AdventOfCodeDay(year, day, title) {


    /**
     * Part One:
     *
     * Algorithm overview:
     * 1) Parse the input grid and locate the start position and keys.
     * 2) Build a reduced graph where nodes are the start(s) and each key.
     *    For each node we BFS the grid to find reachable keys; record for each
     *    reachable key the distance and a bitmask of doors (required keys) encountered.
     *    This compresses long open-path corridors into weighted edges with requirements.
     * 3) Perform a Dijkstra-like search over states: (current node, collectedKeysMask).
     *    The search uses a priority queue ordered by total steps. From a state we
     *    consider moving to any reachable key for which we have the required keys.
     *    When the collected keys mask equals the goal mask (all keys), we return
     *    the accumulated distance (shortest path).
     * This approach drastically reduces the state space compared to exploring the
     * full grid for every step.
     */
    fun partOne(data: String): Int {
        val grid = SimpleGrid.of(data.trimEnd())
        val starts = grid.findAll('@') // should be only one
        if (starts.isEmpty()) return 0
        val solver = KeySolver(grid, starts)

        return solver.solve()
    }

    /**
     * Part two:
     *
     * The maze is modified so the single starting position is replaced by four
     * robots positioned on the four diagonal cells around the original start.
     * After applying the transformation to the grid we reuse the same solver logic,
     * which naturally supports multiple start positions: the reduced graph contains
     * all starts plus all keys, and the state encodes the position of each robot.
     * The rest of the algorithm (BFS-to-build-edges + Dijkstra over bitmask states)
     * is unchanged.
     */
    fun partTwo(data: String): Int {
        val grid = SimpleGrid.of(data.trimEnd())
        val starts = grid.findAll('@') // should be only one
        if (starts.isEmpty()) return 0
        // Modify grid for part 2: replace single @ with 4 robots
        val newStarts = grid.updateMap(starts)
        val solver = KeySolver(grid, newStarts)

        return solver.solve()
    }

    /**
     * Modify the grid for part two by replacing the single starting position
     * with four robots positioned on the diagonals around it.
     */
    private fun SimpleGrid<Char>.updateMap(starts: List<Point>): List<Point> {
        val (r, c) = starts[0]
        this[Point(r, c)] = '#'
        this[Point(r - 1, c)] = '#'
        this[Point(r + 1, c)] = '#'
        this[Point(r, c - 1)] = '#'
        this[Point(r, c + 1)] = '#'
        // new starts at diagonals
        val newStarts = listOf(
            Point(r - 1, c - 1), Point(r - 1, c + 1), Point(r + 1, c - 1), Point(r + 1, c + 1)
        )
        for (p in newStarts) this[p] = '@'
        return newStarts
    }

    /**
     * Solver class.
     *
     * Implementation notes:
     * - nodePositions holds start(s) first, then keys. Each node is at a grid coord.
     * - For each node we run a BFS over the grid to discover all reachable keys.
     *   The BFS returns for every reachable key: (distance, requiredMask) where
     *   requiredMask encodes which keys are needed to pass doors on the path.
     * - edges[fromNode] lists Edge(toNode, dist, requiredMask) for all reachable keys.
     * - solve() runs a Dijkstra-like search over full states: (positions of robots,
     *   collectedKeysMask). Positions are indices into nodePositions.
     * - The search only allows transitions to keys whose requiredMask is satisfied
     *   by the current collectedKeysMask. When all keys are collected we return the
     *   best distance found.
     */
    private class KeySolver(private val grid: SimpleGrid<Char>, starts: List<Point>) {
        private val keyToIndex = IntArray(26) { -1 }
        private val indexToKey = mutableListOf<Char>()
        private val nodePositions = mutableListOf<Point>() // starts then keys
        private val startCount = starts.size

        init {
            // add starts
            nodePositions.addAll(starts)
            // find keys and map them
            val keyPoints = grid.findAll { ch -> ch in 'a'..'z' }
            for (keyPoint in keyPoints) {
                val ch = grid[keyPoint]
                // k is 0..25 for 'a'..'z'
                val k = ch - 'a'
                if (keyToIndex[k] == -1) {
                    keyToIndex[k] = nodePositions.size
                    indexToKey.add(ch)
                    nodePositions.add(keyPoint)
                }
            }
        }

        private data class Edge(val toNode: Int, val dist: Int, val requiredMask: Int)

        private data class State(val positions: List<Int>, val collected: Int, val steps: Int)

        fun solve(): Int {
            val totalKeys = indexToKey.size
            if (totalKeys == 0) return 0
            // build edges from each start/key node to reachable keys
            val edges = Array(nodePositions.size) { mutableListOf<Edge>() }
            for (i in nodePositions.indices) {
                val p = nodePositions[i]
                val found = bfsFrom(p)
                for ((kIdx, pair) in found) {
                    val (dist, req) = pair
                    edges[i].add(Edge(kIdx, dist, req))
                }
            }

            val goalMask = if (totalKeys >= 31) -1 else ((1 shl totalKeys) - 1)


            val pq = PriorityQueue<State>(compareBy { it.steps })
            val startPositions = nodePositions.indices.take(startCount).toList()
            pq.add(State(startPositions, 0, 0))
            val seen = mutableMapOf<String, Int>()

            while (pq.isNotEmpty()) {
                val cur = pq.poll()
                val key = stateKey(cur.positions, cur.collected)
                if (seen.getOrDefault(key, Int.MAX_VALUE) <= cur.steps) continue
                seen[key] = cur.steps
                if (cur.collected == goalMask) return cur.steps

                // for each robot, try to move to an uncollected key
                for (robot in 0 until cur.positions.size) {
                    val posNode = cur.positions[robot]
                    for (e in edges[posNode]) {
                        val keyChar = nodeToKeyChar(e.toNode) ?: continue

                        val bit = 1 shl (keyChar - 'a')
                        if (cur.collected and bit != 0) continue // already collected
                        if (e.requiredMask and (cur.collected.inv()) != 0) continue // missing required keys
                        val newCollected = cur.collected or bit
                        val newPositions = cur.positions.toMutableList()
                        newPositions[robot] = e.toNode
                        val newSteps = cur.steps + e.dist
                        val newKey = stateKey(newPositions, newCollected)
                        if (seen.getOrDefault(newKey, Int.MAX_VALUE) <= newSteps) continue
                        pq.add(State(newPositions, newCollected, newSteps))
                    }
                }
            }
            return -1
        }

        private fun nodeToKeyChar(nodeIdx: Int): Char? {
            val keysStart = startCount
            if (nodeIdx >= keysStart && nodeIdx < nodePositions.size) return indexToKey[nodeIdx - keysStart]
            return null
        }

        /**
         * BFS from point, returns map of nodeIndex -> Pair(dist, requiredMask)
         *
         * This BFS walks the grid from the given start point and records every
         * key it can reach. For each reachable key it returns the shortest
         * distance and a bitmask of doors encountered along that shortest path
         * (the bitmask indicates which keys are required to pass those doors).
         */
        private fun bfsFrom(start: Point): Map<Int, Pair<Int, Int>> {
            val visited = mutableSetOf<Point>()
            val q = ArrayDeque<Pair<Point, Int>>() // point, requiredMask
            val dist = mutableMapOf<Point, Int>()
            q.add(Pair(start, 0))
            visited.add(start)
            dist[start] = 0
            val found = mutableMapOf<Int, Pair<Int, Int>>()

            while (q.isNotEmpty()) {
                val (p, req) = q.removeFirst()
                val d = dist[p] ?: 0
                val ch = grid[p]
                // if it's a key (and not the starting cell), record
                if (ch in 'a'..'z') {
                    val keyIdx = keyToIndex[ch - 'a']
                    if (keyIdx != -1) {
                        found[keyIdx] = Pair(d, req)
                    }
                }
                for (n in p.cardinalNeighbors()) {
                    if (!grid.contains(n)) continue
                    if (n in visited) continue
                    val ch2 = grid[n]
                    if (ch2 == '#') continue
                    var newReq = req
                    if (ch2 in 'A'..'Z') {
                        // door requires corresponding key
                        newReq = req or (1 shl (ch2 - 'A'))
                    }
                    visited.add(n)
                    dist[n] = d + 1
                    q.add(Pair(n, newReq))
                }
            }
            return found
        }

        private fun stateKey(positions: List<Int>, collected: Int): String {
            return positions.joinToString(",") + "|" + collected
        }
    }
}