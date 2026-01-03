package me.nicolas.adventofcode.year2025

import me.nicolas.adventofcode.utils.*

// --- Day 12: Christmas Tree Farm ---
// https://adventofcode.com/2025/day/12
fun main() {
    val data = readFileDirectlyAsText("/year2025/day12/data.txt")
    val day = Day12()
    prettyPrintPartOne { day.partOne(data) }
    // No part two for this day
}

class Day12(year: Int = 2025, day: Int = 12, title: String = "Christmas Tree Farm") :
    AdventOfCodeDay(year, day, title) {

    private data class Shape(val id: Int, val points: List<Point>, val width: Int, val height: Int)

    private data class Region(val width: Int, val height: Int, val presentCounts: Map<Int, Int>)

    private data class Present(val shapeIndex: Int, val cellCount: Int)


    /**
     * Part 1: Count how many regions can fit all their required presents
     *
     * **Algorithm**: Constraint Satisfaction with Backtracking
     * - For each region, attempt to place all required presents using backtracking
     * - Presents can be rotated/flipped (up to 8 orientations per shape)
     * - Shapes cannot overlap, but empty cells are allowed in the region
     * - Use optimizations to prune the search space early
     *
     * **Optimizations**:
     * 1. Early rejection: If total present area > region area, impossible
     * 2. Lexicographic ordering: When placing multiple identical shapes, enforce
     *    placement order to avoid exploring symmetric duplicate solutions
     * 3. SimpleGrid: Use 2D grid abstraction for cleaner coordinate handling
     *
     * Time Complexity: O(R × P! × V × W × H) worst case, heavily pruned in practice
     *   where R = regions, P = presents per region, V = variants (≤8), W/H = dimensions
     * Space Complexity: O(W × H) for grid state
     */
    fun partOne(data: String): Int {
        val (baseShapes, regions) = parseInput(data)

        // Precompute all rotation/flip variants for each shape
        val allShapeVariants = baseShapes.map { shape ->
            generateOrientations(shape.points).map { points ->
                val maxX = points.maxOfOrNull { it.x } ?: 0
                val maxY = points.maxOfOrNull { it.y } ?: 0
                Shape(shape.id, points, maxX + 1, maxY + 1)
            }
        }

        return regions.count { region ->
            canFitAllPresents(region, allShapeVariants)
        }
    }


    /**
     * Check if all presents can fit in the given region using backtracking
     * with optimizations:
     * - Early rejection based on area
     * - Lexicographic ordering for identical shapes
     */
    private fun canFitAllPresents(region: Region, shapeVariants: List<List<Shape>>): Boolean {
        // Expand present counts into individual presents to place
        val presentsToPlace = mutableListOf<Present>()
        var totalCellsNeeded = 0

        region.presentCounts.forEach { (shapeIndex, count) ->
            if (shapeIndex < shapeVariants.size) {
                val cellCount = shapeVariants[shapeIndex][0].points.size
                repeat(count) {
                    presentsToPlace.add(Present(shapeIndex, cellCount))
                    totalCellsNeeded += cellCount
                }
            }
        }

        // Early termination: impossible if presents need more space than available
        val availableArea = region.width * region.height
        if (totalCellsNeeded > availableArea) {
            return false
        }

        // Empty region is always valid
        if (presentsToPlace.isEmpty()) {
            return true
        }

        // Sort presents by shape index to group identical shapes together
        // This enables lexicographic ordering optimization in backtracking
        presentsToPlace.sortBy { it.shapeIndex }

        // Use SimpleGrid with Boolean values (false = empty, true = occupied)
        val matrix = Array(region.height) { Array(region.width) { false } }
        val grid = SimpleGrid(matrix)

        return tryPlacingPresents(
            grid = grid,
            presentsToPlace = presentsToPlace.toList(),
            currentPresentIndex = 0,
            shapeVariants = shapeVariants,
            minPlacementX = 0,
            minPlacementY = 0
        )
    }

    /**
     * Recursive backtracking to place presents one at a time
     *
     * Key Optimization - Lexicographic Ordering: When placing multiple identical shapes, we enforce that each subsequent
     * identical shape must be placed at a position >= the previous one (in row-major order). This prevents exploring symmetric duplicate solutions.
     */
    private fun tryPlacingPresents(
        grid: SimpleGrid<Boolean>,
        presentsToPlace: List<Present>,
        currentPresentIndex: Int,
        shapeVariants: List<List<Shape>>,
        minPlacementX: Int,
        minPlacementY: Int
    ): Boolean {
        // Base case: all presents successfully placed
        if (currentPresentIndex == presentsToPlace.size) {
            return true
        }

        val currentShapeIndex = presentsToPlace[currentPresentIndex].shapeIndex
        val variants = shapeVariants[currentShapeIndex]

        // Try each orientation of the current shape
        for (variant in variants) {
            // Try placing at each valid position in row-major order
            for (y in minPlacementY until grid.height) {
                val xStart = if (y == minPlacementY) minPlacementX else 0

                for (x in xStart until grid.width) {
                    if (canPlaceShape(variant, x, y, grid)) {
                        // Place the shape on the grid
                        placeShape(variant, x, y, grid, true)

                        // Determine minimum position for next present
                        val nextMinX: Int
                        val nextMinY: Int

                        if (currentPresentIndex + 1 < presentsToPlace.size &&
                            presentsToPlace[currentPresentIndex + 1].shapeIndex == currentShapeIndex
                        ) {
                            // Next present is same shape - enforce lexicographic ordering
                            nextMinX = x
                            nextMinY = y
                        } else {
                            // Next present is different shape - can place anywhere
                            nextMinX = 0
                            nextMinY = 0
                        }

                        // Recursively try to place remaining presents
                        if (tryPlacingPresents(
                                grid = grid,
                                presentsToPlace = presentsToPlace,
                                currentPresentIndex = currentPresentIndex + 1,
                                shapeVariants = shapeVariants,
                                minPlacementX = nextMinX,
                                minPlacementY = nextMinY
                            )
                        ) {
                            return true // Found a valid solution
                        }

                        // Backtrack: remove the shape and try next position
                        placeShape(variant, x, y, grid, false)
                    }
                }
            }
        }

        return false // No valid placement found for this present
    }

    private fun parseInput(data: String): Pair<List<Shape>, List<Region>> {
        val lines = data.lines()

        // Find the line where regions start (contains 'x' like "4x4:")
        val regionStartIndex = lines.indexOfFirst { line -> line.contains(Regex("\\d+x\\d+:")) }

        val shapesStr = lines.subList(0, regionStartIndex).joinToString("\n")
        val regionsStr = lines.subList(regionStartIndex, lines.size).joinToString("\n")

        val shapes = parseShapes(shapesStr)
        val regions = parseRegions(regionsStr)

        return shapes to regions
    }

    private fun parseShapes(shapesStr: String): List<Shape> {
        val shapes = mutableListOf<Shape>()
        val lines = shapesStr.lines().filter { it.isNotBlank() }

        var i = 0
        while (i < lines.size) {
            if (lines[i].contains(':')) {
                val id = lines[i].substringBefore(':').toInt()
                val points = mutableListOf<Point>()
                i++
                var y = 0
                while (i < lines.size && !lines[i].contains(':')) {
                    lines[i].forEachIndexed { x, char ->
                        if (char == '#') {
                            points.add(Point(x, y))
                        }
                    }
                    y++
                    i++
                }
                val maxX = points.maxOfOrNull { point -> point.x } ?: 0
                val maxY = points.maxOfOrNull { point -> point.y } ?: 0
                shapes.add(Shape(id, points, maxX + 1, maxY + 1))
            } else {
                i++
            }
        }
        return shapes
    }

    private fun parseRegions(regionsStr: String): List<Region> {
        return regionsStr.lines().filter { line -> line.isNotBlank() }
            .map { line ->
                val parts = line.split(": ")
                val (width, height) = parts[0].split("x").map { it.toInt() }
                val counts = parts[1].split(" ").map { it.toInt() }
                val presentCounts =
                    counts.mapIndexed { index, count -> index to count }.filter { it.second > 0 }.toMap()

                Region(width, height, presentCounts)
            }
    }

    /**
     * Generate all unique orientations (rotations and flips) of a shape (up to 8 variants).
     */
    private fun generateOrientations(points: List<Point>): Set<List<Point>> {
        val orientations = mutableSetOf<List<Point>>()
        var currentPoints = points

        // 2 flips × 4 rotations = 8 orientations
        repeat(2) {
            // 4 rotations (0°, 90°, 180°, 270°)
            repeat(4) {
                orientations.add(currentPoints)
                currentPoints = currentPoints.map { point -> Point(point.y, -point.x) } // Rotate 90 degrees
                val minX = currentPoints.minOfOrNull { point -> point.x } ?: 0
                val minY = currentPoints.minOfOrNull { point -> point.y } ?: 0
                currentPoints = currentPoints.map { point -> Point(point.x - minX, point.y - minY) }
            }
            currentPoints = points.map { point -> Point(-point.x, point.y) } // Flip
            val minX = currentPoints.minOfOrNull { point -> point.x } ?: 0
            val minY = currentPoints.minOfOrNull { point -> point.y } ?: 0
            currentPoints = currentPoints.map { point -> Point(point.x - minX, point.y - minY) }
        }

        return orientations
    }

    /**
     * Check if a shape can be placed at position (placementX, placementY)
     * Returns false if any cell would be out of bounds or overlap existing shapes
     */
    private fun canPlaceShape(shape: Shape, placementX: Int, placementY: Int, grid: SimpleGrid<Boolean>): Boolean {
        for (point in shape.points) {
            val point = Point(placementX + point.x, placementY + point.y)
            if (!grid.contains(point) || grid[point]) {
                return false
            }
        }
        return true
    }

    /**
     * Place or remove a shape on the grid
     * - occupied: true to mark cells as occupied, false to clear them (backtracking)
     */
    private fun placeShape(
        shape: Shape,
        placementX: Int,
        placementY: Int,
        grid: SimpleGrid<Boolean>,
        occupied: Boolean
    ) {
        for (point in shape.points) {
            val point = Point(placementX + point.x, placementY + point.y)
            grid[point] = occupied
        }
    }
}

