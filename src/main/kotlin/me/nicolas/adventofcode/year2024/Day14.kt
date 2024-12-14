package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.*

// --- Day 14: Restroom Redoubt ---
// https://adventofcode.com/2024/day/14
fun main() {
    val data = readFileDirectlyAsText("/year2024/day14/data.txt")
    val day = Day14(2024, 14)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day14(year: Int, day: Int, title: String = "Restroom Redoubt", val width: Int = 101, val height: Int = 103) :
    AdventOfCodeDay(year, day, title) {

    data class Robot(var position: Point, val velocity: Pair<Int, Int>)

    private fun parseInput(input: String): List<Robot> {
        return input.lines().map { line ->
            val (posStr, velStr) = line.split(" ")
            val posParts = posStr.removePrefix("p=").split(",")
            val velParts = velStr.removePrefix("v=").split(",")

            val x = posParts[0].toInt()
            val y = posParts[1].toInt()
            val vx = velParts[0].toInt()
            val vy = velParts[1].toInt()

            Robot(Point(x, y), Pair(vx, vy))
        }
    }

    private fun moveRobot(robot: Robot, iterations: Int): Point {
        var x = (robot.position.x + (robot.velocity.first * iterations)) % width
        var y = (robot.position.y + (robot.velocity.second * iterations)) % height

        if (x < 0) {
            x += width
        }
        if (y < 0) {
            y += height
        }

        return Point(x, y)
    }

    private fun countRobotsInQuadrants(positions: List<Point>): List<Int> {

        val middleVertical = height / 2
        val middleHorizontal = width / 2

        val quad1 = positions.count { it.y < middleVertical && it.x < middleHorizontal }
        val quad2 = positions.count { it.y > middleVertical && it.x < middleHorizontal }
        val quad3 = positions.count { it.y < middleVertical && it.x > middleHorizontal }
        val quad4 = positions.count { it.y > middleVertical && it.x > middleHorizontal }

        return listOf(quad1, quad2, quad3, quad4)
    }

    private fun calculateSafetyFactor(quadrantCounts: List<Int>): Int {
        return quadrantCounts.fold(1) { product, count -> product * count }
    }

    private fun buildGrid(robots: List<Point>): List<String> {
        val grid = Array(height) { Array<Int>(width) { 0 } }
        for ((x, y) in robots) {
            grid[y][x]++
        }

        return grid.map { row ->
            row.joinToString("") { cell ->
                if (cell > 0) {
                    "#"
                } else {
                    " "
                }
            }
        }
    }

    fun partOne(data: String): Int {
        val robots = parseInput(data)
        val positionsAfter100Seconds = robots.map { moveRobot(it, 100) }
        val quadrantCounts = countRobotsInQuadrants(positionsAfter100Seconds)
        val safetyFactor = calculateSafetyFactor(quadrantCounts)

        // println(displayRobots(positionsAfter100Seconds))

        return safetyFactor
    }

    fun partTwo(data: String): Int {
        var robots = parseInput(data)

        val nbRobots = robots.size

        var seconds = 1
        while (true) {

            robots = robots.map { robot ->
                Robot(moveRobot(robot, 1), robot.velocity)
            }

            // find if at least 8 robots are on the same line
            // val grid = buildGrid(robots.map { robot -> robot.position })
            // if(grid.any { line -> line.contains("########") }) {
            //    println("Seconds: $seconds")
            //    grid.forEach { line -> println(line) }
            //     return counter
            // }

            // If all robots are in different positions, return the counter (display the picture of a Christmas tree)
            if(robots.map { robot -> robot.position }.toSet().size == nbRobots) {
                buildGrid(robots.map { robot -> robot.position })
                println("Seconds: $seconds")
                val grid = buildGrid(robots.map { robot -> robot.position })
                grid.forEach { line -> println(line) }

                return seconds
            }
            seconds++
        }

        return seconds
    }
}