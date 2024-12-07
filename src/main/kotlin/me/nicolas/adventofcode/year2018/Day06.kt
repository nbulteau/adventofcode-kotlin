package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs

// https://adventofcode.com/2018/day/6
fun main() {
    val data = readFileDirectlyAsText("/year2018/day06/data.txt")
    val day = Day06(2018, 6, "Chronal Coordinates")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(10000, data) }
}

class Day06(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val coords = buildCoords(data)

        // Width and height of the map
        val width = coords.maxOf { it.first }
        val height = coords.maxOf { it.second }
        // Map of points to the closest coordinate
        val map = mutableMapOf<Pair<Int, Int>, Int>()
        // Set of points that are bordering the map (infinite area)
        val bordering = mutableSetOf<Int>()

        for (x in 0..width) {
            for (y in 0..height) {
                val distances = coords.map { point ->
                    Pair(x, y).manhattanDistance(point)
                }
                // There is only one point with the minimum distance to the current point
                if (distances.groupBy { distance -> distance }.minBy { (key, _) -> key }.value.size == 1) {
                    val coordId = distances.indexOf(distances.min())
                    map[Pair(x, y)] = coordId
                    // If the point is on the border of the map, it has an infinite area
                    if (x == 0 || x == width || y == 0 || y == height) {
                        bordering.add(coordId)
                    }
                }
            }
        }

        /*
            // Create a list distinct colors using HSB color model
            val colors = List(coords.size) { Color.getHSBColor(it.toFloat() / 50, 1f, 1f) }
            val mappingFunction: (Int) -> Color = { value -> colors[value] }
            val grid = Grid(map)
            grid.generatePicture(mappingFunction, "grid-part1.png")
        */

        return map.values.filterNot { coordId -> bordering.contains(coordId) }
            .groupBy { coordId -> coordId }
            .map { it.value.size }
            .max()
    }

    fun partTwo(distanceMax: Int, data: String): Int {
        val coords = buildCoords(data)

        // Width and height of the map
        val width = coords.maxOf { it.first }
        val height = coords.maxOf { it.second }

        val safeRegions = mutableListOf<Int>()

        for (x in 0..width) {
            for (y in 0..height) {
                val distances = coords.map {
                    abs(x - it.first) + abs(y - it.second)
                }
                safeRegions.add( distances.sum())
            }
        }
        // Count the number of points that have a total distance to all coordinates of less than 10000
        return safeRegions.count { it < distanceMax }
    }

    private fun buildCoords(data: String): List<Pair<Int, Int>> {
        val coords = data.split("\n").map { line ->
            val parts = line.split(", ")
            Pair(parts.last().toInt(), parts.first().toInt())
        }
        return coords
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