package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*

fun main() {
    val data = readFileDirectlyAsText("/year2023/day16/data.txt")
    val day = Day16(2023, 16, "The Floor Will Be Lava")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day16(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val contraption = Grid.of(data)
        val topLeftRight = Pair(Pair(0, 0), Direction.EAST)
        val path = getPath(topLeftRight, contraption)

        return path.map { step -> step.first }.toSet().size
    }

    fun partTwo(data: String): Int {
        val contraption = Grid.of(data)
        // North edges
        val norths = contraption.map()
            .filter { (key, _) -> key.first == 0 }.keys
            .map { point -> point to Direction.SOUTH }
            .toList()
        // East edges
        val easts = contraption.map()
            .filter { (key, _) -> key.second == contraption.columns - 1 }.keys
            .map { point -> point to Direction.WEST }
            .toList()
        // South edges
        val souths = contraption.map()
            .filter { (key, _) -> key.first == contraption.rows - 1 }.keys
            .map { point -> point to Direction.NORTH }
            .toList()
        // West edges
        val wests = contraption.map().filter { (key, _) -> key.second == 0 }.keys
            .map { point -> point to Direction.EAST }
            .toList()

        // All start points are the edges of the contraption (north, east, south, west)
        val startPoints = norths + easts + souths + wests
        // For each start point, get the path and return the max number of unique points
        val paths = startPoints.map { getPath(it, contraption) }

        return paths.maxOf { path -> path.map { step -> step.first }.toSet().size }
    }

    // Return the path of the beam from the start point in the grid contraption
    private fun getPath(
        startPoint: Pair<Pair<Int, Int>, Direction>,
        grid: Grid<Char>,
    ): MutableSet<Pair<Pair<Int, Int>, Direction>> {
        // Initialize the visited points with the start point
        val visited = mutableSetOf(startPoint)
        // Initialize the points to explore with the start point and a distance of 0
        val toExplore = ArrayDeque<Pair<Pair<Int, Int>, Direction>>()
        toExplore.add(startPoint)
        // While there are points to explore
        while (toExplore.isNotEmpty()) {
            val (currentPoint, direction) = toExplore.removeFirst()
            val nextPoints = grid.nextPoints(currentPoint, direction).filter { point -> grid[point.first] != null }

            // println("currentPoint: $currentPoint, direction: $direction, nextPoints: $nextPoints")

            nextPoints.filter { !visited.contains(it) }.forEach { (point, direction) ->
                visited.add(point to direction)
                toExplore.add(Pair(point, direction))
            }
        }

        return visited
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

        fun move(point: Pair<Int, Int>): Pair<Int, Int> {
            return Pair(point.first + dx, point.second + dy)
        }
    }

    // Return the next points to explore from the current point and direction in the grid contraption
    private fun Grid<Char>.nextPoints(
        currentPoint: Pair<Int, Int>,
        direction: Direction,
    ): List<Pair<Pair<Int, Int>, Direction>> {
        val nextPoints = mutableListOf<Pair<Pair<Int, Int>, Direction>>()

        if (this[currentPoint] == '|') {
            // If the beam encounters the flat side of a splitter the beam is split into two beams
            if (direction == Direction.EAST || direction == Direction.WEST) {
                nextPoints.add(Direction.NORTH.move(currentPoint) to Direction.NORTH)
                nextPoints.add(Direction.SOUTH.move(currentPoint) to Direction.SOUTH)
            } else {
                // If the beam encounters the pointy end of a splitter (|), the beam passes through
                nextPoints.add(direction.move(currentPoint) to direction)
            }
        } else if (this[currentPoint] == '-') {
            // If the beam encounters the flat side of a splitter the beam is split into two beams
            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                nextPoints.add(Direction.EAST.move(currentPoint) to Direction.EAST)
                nextPoints.add(Direction.WEST.move(currentPoint) to Direction.WEST)
            } else {
                // If the beam encounters the pointy end of a splitter (-), the beam passes through
                nextPoints.add(direction.move(currentPoint) to direction)
            }
        } else if (this[currentPoint] == '\\') {
            // If the beam encounters a mirror (\), the beam is reflected 90 degrees
            when (direction) {
                Direction.NORTH -> nextPoints.add(Direction.WEST.move(currentPoint) to Direction.WEST)
                Direction.EAST -> nextPoints.add(Direction.SOUTH.move(currentPoint) to Direction.SOUTH)
                Direction.SOUTH -> nextPoints.add(Direction.EAST.move(currentPoint) to Direction.EAST)
                Direction.WEST -> nextPoints.add(Direction.NORTH.move(currentPoint) to Direction.NORTH)
            }
        } else if (this[currentPoint] == '/') {
            // If the beam encounters a mirror (/), the beam is reflected 90 degrees
            when (direction) {
                Direction.NORTH -> nextPoints.add(Direction.EAST.move(currentPoint) to Direction.EAST)
                Direction.EAST -> nextPoints.add(Direction.NORTH.move(currentPoint) to Direction.NORTH)
                Direction.SOUTH -> nextPoints.add(Direction.WEST.move(currentPoint) to Direction.WEST)
                Direction.WEST -> nextPoints.add(Direction.SOUTH.move(currentPoint) to Direction.SOUTH)
            }
        } else {
            // If the beam encounters empty space (.), it continues in the same direction.
            nextPoints.add(direction.move(currentPoint) to direction)
        }

        return nextPoints
    }
}



