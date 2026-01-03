package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.*
import kotlin.math.abs
import kotlin.math.max

// --- Day 9: Movie Theater ---
// https://adventofcode.com/2025/day/9
fun main() {
    val data = readFileDirectlyAsText("/year2025/day09/data.txt")
    val day = Day09(2025, 9)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day09(year: Int, day: Int, title: String = "Movie Theater") : AdventOfCodeDay(year, day, title) {

    /**
     * Part One: Find the largest rectangle using any two red tiles as opposite corners.
     *
     */
    fun partOne(data: String): Long {
        val redTiles = parseRedTilePositions(data)
        return findLargestRectangleArea(redTiles)
    }

    /**
     * Part Two: Find the largest rectangle that fits entirely inside the polygon made by red tiles.
     *
     * The problem: Red tiles connect in order to form a closed shape. We need to find the biggest
     * rectangle that stays completely within this shape (only touching red or green tiles inside).
     *
     * The grid is huge (millions of cells), so we can't check every point.
     *
     * Solution approach:
     * - Compress coordinates: Work only with the ~500 unique X and Y values where red tiles exist,
     *   reducing the grid from potentially millions to about 500×500 cells.
     *
     * - Mark valid regions: For each compressed cell, check if it's inside the polygon (using
     *   ray-casting on the cell's midpoint). Store this as a binary grid (1=inside, 0=outside).
     *
     * - Build a prefix sum array so we can (instantly) count how many cells in any rectangle are inside the polygon.
     *
     * - Try every pair of red tiles as opposite corners. Use the prefix sum to check if all cells between them are inside the polygon.
     *   Track the largest valid rectangle.
     *
     * This runs in O(n²) time instead of O(n² × W × H), where n ≈ 500 tiles and W,H could be huge.
     */
    fun partTwo(data: String): Long {
        val redTiles = parseRedTilePositions(data)
        return findLargestRectangleAreaWithinGreenRegion(redTiles)
    }

    private fun parseRedTilePositions(data: String): List<Pair<Int, Int>> {
        return data.lines()
            .filter { line -> line.isNotBlank() }
            .map { line ->
                val (x, y) = line.split(",").map { it.trim().toInt() }
                x to y
            }
    }

    /**
     * Brut force approach for Part One: Try every pair of red tiles as opposite corners
     * and calculate the area of the rectangle they form.
     */
    private fun findLargestRectangleArea(redTilePositions: List<Pair<Int, Int>>): Long {
        var largestArea = 0L

        // Try every pair of red tiles as opposite corners
        for (i in redTilePositions.indices) {
            for (j in i + 1 until redTilePositions.size) {
                val (x1, y1) = redTilePositions[i]
                val (x2, y2) = redTilePositions[j]

                // Calculate rectangle area using these two points as opposite corners
                // Count tiles inclusively (both endpoints included)
                val width = abs(x2 - x1).toLong() + 1
                val height = abs(y2 - y1).toLong() + 1
                val area = width * height

                largestArea = max(largestArea, area)
            }
        }

        return largestArea
    }

    /**
     * Coordinate compression: instead of working with the full grid (which is huge)
     * https://www.quora.com/What-is-coordinate-compression-and-what-is-it-used-for
     * we only work with the unique X and Y coordinates where red tiles exist.
     * This reduces the grid to ~500x500 cells.
     */
    private fun findLargestRectangleAreaWithinGreenRegion(redTilePositions: List<Pair<Int, Int>>): Long {
        val uniqueXCoordinates = redTilePositions.map { it.first }.distinct().sorted()
        val uniqueYCoordinates = redTilePositions.map { it.second }.distinct().sorted()

        // Create mappings from actual coordinates to compressed indices
        val xCoordToIndex = uniqueXCoordinates.withIndex().associate { it.value to it.index }
        val yCoordToIndex = uniqueYCoordinates.withIndex().associate { it.value to it.index }

        val greenRegionGrid = buildGreenRegionGrid(uniqueXCoordinates, uniqueYCoordinates, redTilePositions)

        val prefixSum = buildPrefixSumArray(greenRegionGrid)

        var largestArea = 0L

        // Try all pairs of red tiles as opposite corners of the rectangle
        for (i in redTilePositions.indices) {
            for (j in i + 1 until redTilePositions.size) {
                val (x1, y1) = redTilePositions[i]
                val (x2, y2) = redTilePositions[j]

                val minX = minOf(x1, x2)
                val maxX = maxOf(x1, x2)
                val minY = minOf(y1, y2)
                val maxY = maxOf(y1, y2)

                // Convert actual coordinates to compressed grid indices
                val compressedX1 = xCoordToIndex[minX]!!
                val compressedX2 = xCoordToIndex[maxX]!!
                val compressedY1 = yCoordToIndex[minY]!!
                val compressedY2 = yCoordToIndex[maxY]!!

                // Count how many compressed cells this rectangle spans
                val totalCellsInRectangle = (compressedX2 - compressedX1) * (compressedY2 - compressedY1)

                // Use prefix sum to count how many of those cells are inside the polygon
                // This is O(1) instead of checking every point individually
                val greenCellsInRectangle =
                    queryRectangleSum(prefixSum, compressedX1, compressedY1, compressedX2, compressedY2)

                // Rectangle is valid only if ALL cells are inside the polygon (green region)
                if (totalCellsInRectangle == greenCellsInRectangle) {
                    val width = (maxX - minX + 1).toLong()
                    val height = (maxY - minY + 1).toLong()
                    val area = width * height
                    largestArea = max(largestArea, area)
                }
            }
        }

        return largestArea
    }

    /**
     * Build a grid marking which cells are inside the green region (polygon formed by red tiles).
     * Each cell corresponds to the area between consecutive unique X and Y coordinates.
     * We check the midpoint of each cell to determine if it's inside the polygon.
     * Returns a 2D array where 1 = green tile (inside polygon), 0 = outside (neither red nor green).
     *
     */
    private fun buildGreenRegionGrid(
        uniqueXCoordinates: List<Int>,
        uniqueYCoordinates: List<Int>,
        redTileLoop: List<Pair<Int, Int>>
    ): Array<IntArray> {
        // Create a grid with (n-1) x (m-1) cells where n and m are the number of unique coordinates
        // Each cell represents the space between consecutive coordinate pairs
        val rows = uniqueXCoordinates.size - 1
        val cols = uniqueYCoordinates.size - 1
        val grid = Array(rows) { IntArray(cols) }

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                // Check the midpoint of each cell to determine if it's inside the polygon
                // The midpoint is guaranteed to be inside the cell's bounds
                val midX = (uniqueXCoordinates[i] + uniqueXCoordinates[i + 1]) / 2.0
                val midY = (uniqueYCoordinates[j] + uniqueYCoordinates[j + 1]) / 2.0

                // Mark cell as 1 if inside polygon (green tile), 0 otherwise
                grid[i][j] = if (isPointInsidePolygon(midX to midY, redTileLoop)) 1 else 0
            }
        }

        return grid
    }

    /**
     * Build a 2D prefix sum array from the given grid.
     * This allows O(1) queries for the sum of any rectangular sub-area.
     * The prefix sum at (i,j) contains the sum of all grid cells from (0,0) to (i-1,j-1).
     */
    private fun buildPrefixSumArray(grid: Array<IntArray>): Array<IntArray> {
        val rows = grid.size
        val cols = if (rows > 0) grid[0].size else 0

        // Create a prefix sum array with dimensions (rows+1) x (cols+1)
        // Extra row and column of zeros simplifies boundary handling
        val prefixSum = Array(rows + 1) { IntArray(cols + 1) }

        // Build 2D prefix sum using inclusion-exclusion principle:
        // prefixSum[i][j] = sum of all grid cells in rectangle from (0,0) to (i-1,j-1)
        for (i in 1..rows) {
            for (j in 1..cols) {
                prefixSum[i][j] = grid[i - 1][j - 1] +     // Current cell value
                        prefixSum[i - 1][j] +     // Sum above
                        prefixSum[i][j - 1] -     // Sum to the left
                        prefixSum[i - 1][j - 1]   // Subtract overlap (counted twice)
            }
        }

        return prefixSum
    }

    /**
     * Query the sum of cells in the rectangle defined by (x1,y1) to (x2-1,y2-1) using the prefix sum array.
     */
    private fun queryRectangleSum(prefixSum: Array<IntArray>, x1: Int, y1: Int, x2: Int, y2: Int): Int {
        // Total = bottom-right - top area - left area + overlap (counted twice in subtraction)
        return prefixSum[x2][y2] -      // Sum from (0,0) to (x2-1,y2-1)
                prefixSum[x1][y2] -      // Subtract sum from (0,0) to (x1-1,y2-1)
                prefixSum[x2][y1] +      // Subtract sum from (0,0) to (x2-1,y1-1)
                prefixSum[x1][y1]        // Add back overlap (subtracted twice above)
    }
}