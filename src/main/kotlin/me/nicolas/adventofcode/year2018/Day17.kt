package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.*


// https://adventofcode.com/2018/day/17
fun main() {
    val data = readFileDirectlyAsText("/year2018/day17/data.txt")
    val day = Day17(2018, 17)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day17(year: Int, day: Int, title: String = "Reservoir Research") : AdventOfCodeDay(year, day, title) {

    private val nonDigits = """[xy=,]""".toRegex()

    private val wallOrStill = setOf('#', '~')

    private val flowOrStill = setOf('|', '~')

    private val fountain: Point = Point(500, 0)

    fun partOne(data: String): Int {
        val spots = claySpotsFromInput(data.lines())
        val minY = spots.minBy { point -> point.y }.y
        val grid = buildGrid(spots)

        grid.flow(fountain)

        return grid.filterIndexed { idx, _ -> idx >= minY }.sumOf { row ->
            row.count { char -> char in flowOrStill }
        }
    }

    fun partTwo(data: String): Int {
        val spots = claySpotsFromInput(data.lines())
        val minY = spots.minBy { point -> point.y }.y
        val grid = buildGrid(spots)

        grid.flow(fountain)

        return grid.filterIndexed { idx, _ -> idx >= minY }.sumOf { row ->
            row.count { char -> char == '~' }
        }
    }

    private fun buildGrid(spots: List<Point>): Array<CharArray> {
        val maxX = spots.maxBy { point -> point.x }.x
        val maxY = spots.maxBy { point -> point.y }.y

        val grid: Array<CharArray> = (0..maxY).map {
            CharArray(maxX + 2).apply { fill('.') }
        }.toTypedArray()

        // Add all clay spots to the grid
        spots.forEach { spot ->
            grid[spot.y][spot.x] = '#'
        }
        // Add the fountain
        grid[fountain.y][fountain.x] = '+'

        return grid
    }

    private fun claySpotsFromInput(input: List<String>): List<Point> =
        input.flatMap { row ->
            val digits = row.replace(nonDigits, "").replace("..", " ").split(" ").map { it.toInt() }.toIntArray()
            if (row.startsWith("y")) {
                (digits[1]..digits[2]).map { y -> Point(y, digits[0]) }
            } else {
                (digits[1]..digits[2]).map { x -> Point(digits[0], x) }
            }
        }

    // Grid extension functions
    private fun Array<CharArray>.flow(source: Point) {
        if (source.down.y !in this.indices) {
            return
        }
        if (this[source.down.y][source.down.x] == '.') {
            this[source.down.y][source.down.x] = '|'
            flow(source.down)
        }
        if (wallOrStill.contains(this[source.down.y][source.down.x]) && source.right.y in this.indices && this[source.right.y][source.right.x] == '.') {
            this[source.right.y][source.right.x] = '|'
            this.flow(source.right)
        }
        if (wallOrStill.contains(this[source.down.y][source.down.x]) && source.left.y in this.indices && this[source.left.y][source.left.x] == '.') {
            this[source.left.y][source.left.x] = '|'
            this.flow(source.left)
        }
        if (this.hasWalls(source)) {
            this.fillLeftAndRight(source)
        }
    }

    private fun Array<CharArray>.hasWalls(source: Point): Boolean =
        this.hasWall(source, Point::right) && this.hasWall(source, Point::left)

    private fun Array<CharArray>.hasWall(source: Point, nextPoint: (Point) -> Point): Boolean {
        var point = source
        while (point.y in this.indices && point.x in this[point.y].indices) {
            when (this[point.y][point.x]) {
                '#' -> return true
                '.' -> return false
                else -> point = nextPoint(point)
            }
        }

        return false
    }

    private fun Array<CharArray>.fillLeftAndRight(source: Point) {
        this.fillUntilWall(source, Point::right)
        this.fillUntilWall(source, Point::left)
    }

    private fun Array<CharArray>.fillUntilWall(source: Point, nextPoint: (Point) -> Point) {
        var point = source
        while (this[point.y][point.x] != '#') {
            this[point.y][point.x] = '~'
            point = nextPoint(point)
        }
    }
}

