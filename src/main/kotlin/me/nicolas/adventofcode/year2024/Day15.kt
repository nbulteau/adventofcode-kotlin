package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.*

//--- Day 15: Warehouse Woes ---
// https://adventofcode.com/2024/day/15
fun main() {
    val data = readFileDirectlyAsText("/year2024/day15/data.txt")
    val day = Day15(2024, 15)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

/**
 *  It simulates the movement of a robot in a warehouse, pushing boxes around according to a sequence of instructions.
 */
class Day15(year: Int, day: Int, title: String = "Warehouse Woes") : AdventOfCodeDay(year, day, title) {

    private val directions = mapOf(
        '^' to Pair(-1, 0),
        'v' to Pair(1, 0),
        '<' to Pair(0, -1),
        '>' to Pair(0, 1)
    )

    fun partOne(data: String): Int {
        val (map, instructions) = parseInput(data)

        var robot = findRobot(map)

        for (instruction in instructions) {
            val direction = directions[instruction]!!
            if (move(map, robot, direction)) {
                robot = Point(robot.x + direction.first, robot.y + direction.second)
            }
        }

        return gpsCoordinate(map)
    }

    fun partTwo(data: String): Int {
        val (map, instructions) = parseInput(data)

        val extendedMap = map.expandMap()

        var robot = findRobot(extendedMap)

        for (instruction in instructions) {
            val direction = directions[instruction]!!

            // If the direction is horizontal
            if (direction.first == 0) {
                if (move(extendedMap, robot, direction)) {
                    robot = Point(robot.x, robot.y + direction.second)
                }
            } else {
                if (isVerticalMoveAllowed(extendedMap, robot, direction.first)) {
                    verticalMove(extendedMap, robot, direction.first)
                    robot = Point(robot.x + direction.first, robot.y)
                }
            }
        }

        return gpsCoordinate(extendedMap)
    }

    private fun List<CharArray>.expandMap() = this.map { line ->
        line.joinToString("") { char ->
            when (char) {
                '#' -> "##"
                '.' -> ".."
                'O' -> "[]"
                '@' -> "@."
                else -> throw IllegalStateException("Unexpected character $char")
            }
        }.toCharArray()
    }

    /**
     * Move the robot in the given direction. If the robot can move, return true, otherwise return false.
     * This is a recursive function that will move the robot except if the next cell is a wall.
     */
    private fun move(map: List<CharArray>, point: Point, direction: Pair<Int, Int>): Boolean {
        val (dx, dy) = direction
        val (newX, newY) = point.x + dx to point.y + dy
        when (map[newX][newY]) {
            '#' -> return false
            '.' -> {
                map[newX][newY] = map[point.x][point.y]
                map[point.x][point.y] = '.'

                return true
            }

            else -> {
                if (move(map, Point(newX, newY), direction)) {
                    map[newX][newY] = map[point.x][point.y]
                    map[point.x][point.y] = '.'
                    return true
                }

                return false
            }
        }
    }

    private fun parseInput(data: String): Pair<List<CharArray>, CharArray> {
        val input = data.lines()
        val split = input.indexOf("") // Find the first empty line

        val map = input.subList(0, split).map { line ->
            line.toCharArray()
        }
        val instructions = input.drop(split + 1).joinToString("").toCharArray()

        return Pair(map, instructions)
    }

    /**
     * Check if the robot can move vertically in the given direction.
     * This is a recursive function that will check if the robot can move vertically.
     */
    private fun isVerticalMoveAllowed(map: List<CharArray>, point: Point, dx: Int): Boolean {
        return when (map[point.x + dx][point.y]) {
            '#' -> false
            '.' -> true
            '[' -> isVerticalMoveAllowed(map, Point(point.x + dx, point.y), dx)
                    && (map[point.x + dx * 2][point.y] == '[' || isVerticalMoveAllowed(map, Point(point.x + dx, point.y + 1), dx))

            ']' -> isVerticalMoveAllowed(map, Point(point.x + dx, point.y - 1), dx)
                    && (map[point.x + dx * 2][point.y - 1] == '[' || isVerticalMoveAllowed(map, Point(point.x + dx, point.y), dx))

            else -> throw IllegalStateException("Unexpected character in map: ${map[point.x + dx][point.y]}")
        }
    }

    private operator fun List<CharArray>.get(point: Point): Char = this[point.x][point.y]

    private operator fun List<CharArray>.set(point: Point, value: Char) {
        this[point.x][point.y] = value
    }

    /**
     * Move the robot vertically in the given direction.
     * This is a recursive function that will move the robot vertically.
     */
    private fun verticalMove(map: List<CharArray>, point: Point, dx: Int) {
        when (map[point.x][point.y]) {
            '.' -> return
            '@' -> {
                verticalMove(map, Point(point.x + dx, point.y), dx)
                map[point.x][point.y] = '.'
                map[point.x + dx][point.y] = '@'
            }

            '[' -> {
                verticalMove(map, Point(point.x + dx, point.y), dx)
                verticalMove(map, Point(point.x + dx, point.y + 1), dx)
                map[point] = '.'
                map[point.x][point.y + 1] = '.'
                map[point.x + dx][point.y] = '['
                map[point.x + dx][point.y + 1] = ']'
            }

            ']' -> {
                verticalMove(map, Point(point.x + dx, point.y - 1), dx)
                verticalMove(map, Point(point.x + dx, point.y), dx)
                map[point.x][point.y - 1] = '.'
                map[point.x][point.y] = '.'
                map[point.x + dx][point.y - 1] = '['
                map[point.x + dx][point.y] = ']'
            }

            else -> throw IllegalStateException("Unexpected character in map: ${map[point.x][point.y]}")
        }
    }

    private fun gpsCoordinate(map: List<CharArray>): Int {
        var acc = 0
        for (x in map.indices) {
            for (y in map[x].indices) {
                if (map[x][y] in "O[") {
                    acc += x * 100 + y
                }
            }
        }

        return acc
    }

    private fun findRobot(map: List<CharArray>): Point {
        for (x in map.indices) {
            for (y in map[x].indices) {
                if (map[x][y] == '@') {
                    return Point(x, y)
                }
            }
        }
        throw IllegalStateException("Unable to find robot")
    }


}