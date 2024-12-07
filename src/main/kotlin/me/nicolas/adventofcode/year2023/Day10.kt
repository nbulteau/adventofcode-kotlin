package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val data = readFileDirectlyAsText("/year2023/day10/data.txt")
    val day = Day10(2023, 10, "")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day10(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val grid = Grid.of(data)
        val startPoint = grid['S']!!
        val path = getPath(startPoint, grid)

        // grid.cleanGridAndDisplay(path)

        return path.size / 2
    }

    /**
     * The idea is to find the points that are inside the pipe. To do that, we can use the non-zero rule.
     * We start from the point, then we look left until we reach the edge of the grid.
     * If we cross the pipe with a vertical orientation, we increase the number of windings.
     * If the number of windings is odd, we are in the pipe.
     *
     * https://en.wikipedia.org/wiki/Nonzero-rule
     */
    fun partTwo(data: String): Int {
        val grid = Grid.of(data)
        val startPoint = grid['S']!!
        val path = getPath(startPoint, grid)

        val inPoints = grid.indices
            .filter { point -> point !in path }
            .filter { point ->
                // we are in the pipe if we have an odd number of windings
                var windings = 0
                // Look Left until we reach to edge of the grid
                for (y in point.second downTo 0) {
                    val pointToTest = Pair(point.first, y)
                    // Increase windings if we cross the pipe with 'vertical' orientation
                    if (grid[pointToTest]!! in "|JL" && pointToTest in path) {
                        windings++
                    }
                }
                // If the number of windings is odd, we are in the pipe
                windings % 2 == 1
            }
            .toSet()

        /*
        // Debug
        grid.cleanGridAndDisplay(path, inPoints)
        val mappingFunction: (Char) -> Color = {
            when (it) {
                'I' -> Color.RED
                '.' -> Color.GREEN
                else -> Color.BLACK
            }
        }
        grid.generatePicture(mappingFunction, "2023-day10-part2.png")
        */

        return inPoints.size
    }

    private fun getPath(startPoint: Pair<Int, Int>, grid: Grid<Char>): MutableSet<Pair<Int, Int>> {
        // Initialize the visited points with the start point
        val visited = mutableSetOf(startPoint)
        // Initialize the points to explore with the start point and a distance of 0
        val toExplore = ArrayDeque<Pair<Pair<Int, Int>, Int>>()
        toExplore.add(startPoint to 0)
        // While there are points to explore
        while (toExplore.isNotEmpty()) {
            val (currentPoint, distance) = toExplore.removeFirst()
            // For each neighbor of the current point
            for (neighbor in grid.getCardinalNeighbors(currentPoint)) {
                // If the neighbor is already in the points to explore, continue to the next neighbor
                if (neighbor in toExplore.map { it.first }) {
                    continue
                }
                // If the current point is connecting with the neighbor and the neighbor is not visited
                if (grid.isConnecting(currentPoint, neighbor) && neighbor !in visited) {
                    visited.add(neighbor)
                    toExplore.add(neighbor to distance + 1)
                }
            }
        }

        return visited
    }

    /**
     * The idea is to check if the current character can connect to the neighbor character in the given direction.
     */
    private fun Grid<Char>.isConnecting(firstPoint: Pair<Int, Int>, secondPoint: Pair<Int, Int>): Boolean {
        // Get the current character value and the neighbor character value from the grid
        val current = this[firstPoint]!!
        val neighbor = this[secondPoint]!!
        // Get the delta between the two points
        val dx = secondPoint.first - firstPoint.first
        val dy = secondPoint.second - firstPoint.second
        // up, down, left, right
        val up = dx == -1 && dy == 0
        val down = dx == 1 && dy == 0
        val left = dx == 0 && dy == -1
        val right = dx == 0 && dy == 1

        // Return whether the current character can connect to the neighbor character in the given direction
        return when (current) {
            //
            'S' -> (neighbor in "|7F" && up) || (neighbor in "|LJ" && down) || (neighbor in "-J7" && right) || (neighbor in "-FL" && left)
            // '|' can connect to '|', 'L' and 'J' in the down direction and to '|', 'F' and '7' in the up direction
            '|' -> (neighbor in "|LJ" && down) || (neighbor in "|F7" && up)
            // '-' can connect to '-', 'F' and 'L' in the left direction and to '-', 'J' and '7' in the right direction
            '-' -> (neighbor in "-FL" && left) || (neighbor in "-J7" && right)
            // 'J' can connect to '|', '7' and 'F' in the up direction and to '-', 'L' and 'F' in the left direction
            'J' -> (neighbor in "|7F" && up) || (neighbor in "-LF" && left)
            // 'F' can connect to '|', 'J' and 'L' in the down direction and to '-', '7' and 'J' in the right direction
            'F' -> (neighbor in "|JL" && down) || (neighbor in "-7J" && right)
            // '7' can connect to '|', 'J' and 'L' in the down direction and to '-', 'L' and 'F' in the left direction
            '7' -> (neighbor in "|JL" && down) || (neighbor in "-FL" && left)
            // 'L' can connect to '|', '7' and 'F' in the up direction and to '-', 'J' and '7' in the right direction
            'L' -> (neighbor in "|7F" && up) || (neighbor in "-J7" && right)
            // else, the current character can't connect to the neighbor character in the given direction
            else -> false
        }
    }

    // Debug function to clean the grid and display it
    private fun Grid<Char>.cleanGridAndDisplay(path: Set<Pair<Int, Int>>, inPoints: Set<Pair<Int, Int>> = emptySet()) {
        this.indices.forEach { point ->
            when (point) {
                in inPoints -> {
                    this[point] = 'I'
                }

                !in path -> {
                    this[point] = '.'
                }

                else -> {
                    when (this[point]) {
                        'S' -> this[point] = 'S'
                        'J' -> this[point] = '┘'
                        'F' -> this[point] = '┌'
                        '7' -> this[point] = '┐'
                        'L' -> this[point] = '└'
                    }
                }
            }
        }
        this.display()
    }

    /**
     * Generate a picture from the grid using the given mapping function.
     * This function takes a mapping function that maps each value in the grid to a color.
     * It creates a BufferedImage and sets the color of each pixel according to the mapping function.
     * Finally, it writes the image to a file.
     */
    private fun Grid<Char>.generatePicture(mappingFunction: (Char) -> Color, filename: String) {
        val width = maxX - minX + 1
        val height = maxY - minY + 1
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        for ((point, value) in this.toMap()) {
            val color = mappingFunction(value)
            image.setRGB(point.first - minX, point.second - minY, color.rgb)
        }

        ImageIO.write(image, "png", File(filename))
    }
}



