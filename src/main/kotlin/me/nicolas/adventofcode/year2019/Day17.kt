package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 17: Set and Forget ---
// https://adventofcode.com/2019/day/17
fun main() {
    val data = readFileDirectlyAsText("/year2019/day17/data.txt")
    val day = Day17(2019, 17, "Set and Forget")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day17(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {
    /**
     * Part One: Calibrate the cameras by calculating alignment parameters.
     *
     * The ASCII program outputs a view of the scaffold. We need to:
     * 1. Execute the IntCode program to get ASCII output
     * 2. Build a grid from the ASCII characters
     * 3. Find all scaffold intersections (positions with scaffolds on all 4 sides)
     * 4. Calculate alignment parameters (x * y) for each intersection
     * 5. Return the sum of all alignment parameters
     */
    fun partOne(data: String): Int {
        val program = parseProgram(data)
        val intCode = IntCodeProgram(program)
        val outputs = intCode.execute(mutableListOf())

        // Convert output to ASCII characters and build grid
        val grid = buildGrid(outputs)

        // Find intersections and calculate alignment parameters
        return findIntersections(grid).sumOf { (x, y) -> x * y }
    }

    /**
     * Part Two: Wake up the vacuum robot and make it visit the entire scaffold.
     *
     * The robot needs to visit every part of the scaffold to notify other robots.
     * We need to:
     * 1. Get the scaffold grid from the camera output
     * 2. Find the complete path the robot must follow (sequence of L/R turns and forward steps)
     * 3. Compress the path into a main routine and 3 movement functions (A, B, C)
     *    - Each function can be at most 20 characters long
     *    - The main routine calls these functions in sequence
     * 4. Wake up the robot by changing address 0 from 1 to 2
     * 5. Provide the movement instructions as ASCII input
     * 6. Execute the program and return the dust collected (last output value)
     */
    fun partTwo(data: String): Long {
        // Get the grid to find the path
        val initialIntCode = IntCodeProgram(parseProgram(data))
        val outputs = initialIntCode.execute(mutableListOf())
        val grid = buildGrid(outputs)

        // Find the complete path the robot needs to follow
        val path = findPath(grid)

        // Compress the path into main routine and movement functions
        val (mainRoutine, functionA, functionB, functionC) = compressPath(path)

        // Convert to ASCII input
        val input = buildInput(mainRoutine, functionA, functionB, functionC)

        // Wake up the robot by setting address 0 to 2
        val program = parseProgram(data).toMutableList()
        program[0] = 2

        // Execute the program with the movement instructions
        val intCode = IntCodeProgram(program)
        val result = intCode.execute(input)

        // Return the last output (dust collected)
        return result.last()
    }

    private fun parseProgram(data: String): List<Long> {
        return data.trim().split(",").map { it.toLong() }
    }

    private fun buildGrid(outputs: List<Long>): List<String> {
        val ascii = outputs.map { it.toInt().toChar() }.joinToString("")
        return ascii.lines().filter { it.isNotEmpty() }
    }

    private fun findIntersections(grid: List<String>): List<Pair<Int, Int>> {
        val intersections = mutableListOf<Pair<Int, Int>>()

        for (y in 1 until grid.size - 1) {
            for (x in 1 until grid[y].length - 1) {
                if (isIntersection(grid, x, y)) {
                    intersections.add(Pair(x, y))
                }
            }
        }

        return intersections
    }

    private fun isIntersection(grid: List<String>, x: Int, y: Int): Boolean {
        // Check if current position is a scaffold
        if (grid[y][x] != '#') return false

        // Check if all four neighbors are scaffolds
        return grid[y - 1][x] == '#' &&
               grid[y + 1][x] == '#' &&
               grid[y][x - 1] == '#' &&
               grid[y][x + 1] == '#'
    }

    private fun findPath(grid: List<String>): String {
        // Find the robot's starting position and direction
        var robotPos = Pair(0, 0)
        var robotDir = '^'

        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] in "^v<>") {
                    robotPos = Pair(x, y)
                    robotDir = grid[y][x]
                }
            }
        }

        // Direction vectors
        val directions = mapOf(
            '^' to Pair(0, -1),
            'v' to Pair(0, 1),
            '<' to Pair(-1, 0),
            '>' to Pair(1, 0)
        )

        val path = mutableListOf<String>()
        var currentPos = robotPos
        var currentDir = robotDir

        while (true) {
            val (dx, dy) = directions[currentDir]!!
            var steps = 0

            // Count steps forward
            while (true) {
                val nextX = currentPos.first + dx * (steps + 1)
                val nextY = currentPos.second + dy * (steps + 1)

                if (nextY in grid.indices && nextX in grid[nextY].indices && grid[nextY][nextX] == '#') {
                    steps++
                } else {
                    break
                }
            }

            if (steps > 0) {
                path.add(steps.toString())
                currentPos = Pair(currentPos.first + dx * steps, currentPos.second + dy * steps)
            }

            // Try to turn
            val leftDir = when (currentDir) {
                '^' -> '<'
                '<' -> 'v'
                'v' -> '>'
                '>' -> '^'
                else -> currentDir
            }

            val rightDir = when (currentDir) {
                '^' -> '>'
                '>' -> 'v'
                'v' -> '<'
                '<' -> '^'
                else -> currentDir
            }

            val (leftDx, leftDy) = directions[leftDir]!!
            val (rightDx, rightDy) = directions[rightDir]!!

            val leftX = currentPos.first + leftDx
            val leftY = currentPos.second + leftDy
            val rightX = currentPos.first + rightDx
            val rightY = currentPos.second + rightDy

            when {
                leftY in grid.indices && leftX in grid[leftY].indices && grid[leftY][leftX] == '#' -> {
                    path.add("L")
                    currentDir = leftDir
                }
                rightY in grid.indices && rightX in grid[rightY].indices && grid[rightY][rightX] == '#' -> {
                    path.add("R")
                    currentDir = rightDir
                }
                else -> break
            }
        }

        return path.joinToString(",")
    }

    private fun compressPath(path: String): List<String> {
        // Try to find repeating patterns that can be functions A, B, C
        // Each function can be at most 20 characters

        val pathParts = path.split(",")

        // Try different combinations of function lengths
        for (aLen in 1..10) {
            val functionA = pathParts.take(aLen).joinToString(",")
            if (functionA.length > 20) continue

            val afterA = path.replace(functionA, "A")

            for (bLen in 1..10) {
                val bStart = afterA.indexOfFirst { it != 'A' && it != ',' }
                if (bStart == -1) continue

                val remainingParts = afterA.substring(bStart).split(",").filter { it.isNotEmpty() }
                if (remainingParts.isEmpty()) continue

                val functionB = remainingParts.take(bLen).joinToString(",")
                if (functionB.length > 20 || functionB.contains("A")) continue

                val afterB = afterA.replace(functionB, "B")

                for (cLen in 1..10) {
                    val cStart = afterB.indexOfFirst { it != 'A' && it != 'B' && it != ',' }
                    if (cStart == -1) {
                        // Check if we only have A and B
                        if (afterB.all { it in "AB," }) {
                            val mainRoutine = afterB.replace(Regex(",+"), ",").trim(',')
                            if (mainRoutine.length <= 20) {
                                return listOf(mainRoutine, functionA, functionB, "")
                            }
                        }
                        continue
                    }

                    val remainingPartsC = afterB.substring(cStart).split(",").filter { it.isNotEmpty() }
                    if (remainingPartsC.isEmpty()) continue

                    val functionC = remainingPartsC.take(cLen).joinToString(",")
                    if (functionC.length > 20 || functionC.contains("A") || functionC.contains("B")) continue

                    val afterC = afterB.replace(functionC, "C")

                    if (afterC.all { it in "ABC," }) {
                        val mainRoutine = afterC.replace(Regex(",+"), ",").trim(',')
                        if (mainRoutine.length <= 20) {
                            return listOf(mainRoutine, functionA, functionB, functionC)
                        }
                    }
                }
            }
        }

        throw IllegalStateException("Could not compress path")
    }

    private fun buildInput(mainRoutine: String, functionA: String, functionB: String, functionC: String): MutableList<Long> {
        val input = mutableListOf<Long>()

        // Main routine
        mainRoutine.forEach { input.add(it.code.toLong()) }
        input.add(10) // newline

        // Function A
        functionA.forEach { input.add(it.code.toLong()) }
        input.add(10)

        // Function B
        functionB.forEach { input.add(it.code.toLong()) }
        input.add(10)

        // Function C
        functionC.forEach { input.add(it.code.toLong()) }
        input.add(10)

        // Video feed (no continuous video)
        input.add('n'.code.toLong())
        input.add(10)

        return input
    }
}