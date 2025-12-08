package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 8: Playground ---
// https://adventofcode.com/2025/day/8
fun main() {
    val data = readFileDirectlyAsText("/year2025/day08/data.txt")
    val day = Day08(2025, 8)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day08(year: Int, day: Int, title: String = "Playground") : AdventOfCodeDay(year, day, title) {

    /**
     * Represents a junction box at a specific 3D position in the playground.
     */
    private data class JunctionBox(val x: Long, val y: Long, val z: Long) {

        /**
         * Calculate the squared Euclidean distance to another junction box.
         * We use squared distance to avoid costly sqrt operations when comparing distances.
         */
        fun squaredDistanceTo(other: JunctionBox): Long {
            val dx = x - other.x
            val dy = y - other.y
            val dz = z - other.z
            return dx * dx + dy * dy + dz * dz
        }
    }

    /**
     * Represents a potential light string connection between two junction boxes.
     * @param firstBox Index of the first junction box
     * @param secondBox Index of the second junction box
     * @param squaredDistance Squared Euclidean distance between the boxes
     */
    private data class Connection(val firstBox: Int, val secondBox: Int, val squaredDistance: Long)

    /**
     * Tracks which junction boxes are connected into the same electrical circuit.
     * Uses Union-Find (Disjoint Set Union) data structure with path compression and union by rank.
     *
     * When two junction boxes are connected by a light string, they can share electricity
     * and become part of the same circuit.
     */
    private class CircuitTracker(totalBoxes: Int) {
        // Parent array to track the root of each circuit (initially each box is its own circuit)
        private val parent = IntArray(totalBoxes) { it }

        // Rank array to keep the tree flat during unions
        private val rank = IntArray(totalBoxes)

        // Size array to track the size of each circuit (number of boxes in it)
        private val circuitSize = IntArray(totalBoxes) { 1 }

        /**
         * Find which circuit a junction box belongs to (with path compression).
         * @param box Index of the junction box
         * @return The root index representing the circuit
         */
        fun findCircuit(box: Int): Int {
            if (parent[box] != box) {
                parent[box] = findCircuit(parent[box])
            }
            return parent[box]
        }

        /**
         * Connect two junction boxes, merging their circuits into one.
         * @param box1 Index of the first junction box
         * @param box2 Index of the second junction box
         */
        fun connectBoxes(box1: Int, box2: Int) {
            val circuit1 = findCircuit(box1)
            val circuit2 = findCircuit(box2)

            // Already in the same circuit
            if (circuit1 == circuit2) {
                return
            }

            // Union by rank: attach smaller tree under larger tree
            val (smallerCircuit, largerCircuit) = if (rank[circuit1] < rank[circuit2]) {
                circuit1 to circuit2
            } else {
                circuit2 to circuit1
            }

            parent[smallerCircuit] = largerCircuit
            circuitSize[largerCircuit] += circuitSize[smallerCircuit]

            // If ranks are equal, increment the rank of the new root
            if (rank[circuit1] == rank[circuit2]) {
                rank[largerCircuit]++
            }
        }
    }

    /**
     * Part One: Connect the N closest pairs of junction boxes and calculate circuit statistics.
     *
     * The Elves want to know how big the circuits will be after connecting the closest
     * junction boxes together with light strings.
     *
     * Algorithm:
     * 1. Parse all junction box positions from input data
     * 2. Calculate all pairwise distances (using squared Euclidean distance to avoid sqrt)
     * 3. Sort pairs by distance and select the N shortest connections
     * 4. Use CircuitTracker to group junction boxes into electrical circuits
     * 5. Calculate the sizes of all resulting circuits
     * 6. Return the product of the three largest circuit sizes
     */
    fun partOne(data: String, numberOfConnections: Int = 1000): Long {
        val junctionBoxes = parseJunctionBoxes(data)
        val totalBoxes = junctionBoxes.size

        // Build sorted list of all possible light string connections by distance
        val allPossibleConnections = junctionBoxes.buildAllPossibleConnections()
        val shortestConnections = allPossibleConnections.take(numberOfConnections)

        // Connect the junction boxes with light strings
        val circuits = CircuitTracker(totalBoxes)
        for ((box1, box2, _) in shortestConnections) {
            circuits.connectBoxes(box1, box2)
        }

        // Group junction boxes by their circuit to find circuit sizes
        val circuitSizes = (0 until totalBoxes)
            .groupBy { box -> circuits.findCircuit(box) }
            .map { it.value.size }
            .sortedDescending()

        // Multiply the three largest circuit sizes
        require(circuitSizes.size >= 3) {
            "Not enough separate circuits: ${circuitSizes.size}, sizes: $circuitSizes"
        }
        return circuitSizes[0].toLong() * circuitSizes[1].toLong() * circuitSizes[2].toLong()
    }

    /**
     * Part Two: Find the final connection needed to unify all junction boxes into one circuit.
     *
     * The Elves need to connect junction boxes until they're all in a single electrical circuit.
     * This function identifies which two junction boxes form the final light string connection
     * that creates a fully unified network.
     *
     * The result helps determine how far those junction boxes are from the wall, so the Elves
     * can pick the right extension cable length.
     *
     * Algorithm:
     * 1. Parse all junction box positions from input data
     * 2. Calculate all pairwise distances (sorted by increasing distance)
     * 3. Use CircuitTracker to progressively connect closest pairs
     * 4. Track the number of separate circuits
     * 5. When the number of circuits drops to 1, we've found the final connection
     * 6. Return the product of the X coordinates of those two junction boxes
     */
    fun partTwo(data: String): Long {
        val junctionBoxes = parseJunctionBoxes(data)
        val totalBoxes = junctionBoxes.size

        // Build all possible light string connections sorted by distance
        val allPossibleConnections = junctionBoxes.buildAllPossibleConnections()

        // Track which boxes are in the same circuit
        val circuits = CircuitTracker(totalBoxes)
        var separateCircuits = totalBoxes

        // Connect boxes in order of increasing distance until all are unified
        for (connection in allPossibleConnections) {
            val box1 = connection.firstBox
            val box2 = connection.secondBox

            // Check if these boxes are already in the same circuit
            if (circuits.findCircuit(box1) != circuits.findCircuit(box2)) {
                circuits.connectBoxes(box1, box2)
                separateCircuits--

                // When we reach 1 circuit, all boxes share electricity
                if (separateCircuits == 1) {
                    // The final connection that unified the entire playground
                    val x1 = junctionBoxes[box1].x
                    val x2 = junctionBoxes[box2].x
                    return x1 * x2
                }
            }
        }

        // Shouldn't happen for valid input, but return 0 if never unified
        return 0L
    }

    /**
     * Parse the input data to extract junction box positions.
     * @param data Input string with one junction box per line (format: "x,y,z")
     * @return List of junction boxes with their 3D coordinates
     */
    private fun parseJunctionBoxes(data: String): List<JunctionBox> {
        return data.trim().split("\n").map { line ->
            val (x, y, z) = line.split(",").map { it.trim().toLong() }
            JunctionBox(x, y, z)
        }
    }

    /**
     * Builds a sorted list of all possible light string connections between junction boxes.
     * Each connection contains the indices of two boxes and their squared Euclidean distance.
     *
     * @return List of possible connections sorted by increasing squared distance
     */
    private fun List<JunctionBox>.buildAllPossibleConnections(): List<Connection> {
        return this.indices.flatMap { i ->
            (i + 1 until this.size).map { j ->
                Connection(i, j, this[i].squaredDistanceTo(this[j]))
            }
        }.sortedBy { it.squaredDistance }
    }
}

