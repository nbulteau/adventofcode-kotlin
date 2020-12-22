package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 20: Jurassic Jigsaw ---
// https://adventofcode.com/2020/day/20
@ExperimentalTime
fun main() {

    println("--- Day 20: Jurassic Jigsaw ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day20/training.txt")
    val data = readFileDirectlyAsText("/year2020/day20/data.txt")

    val sections = data.split("\n\n")
    val tiles = extractTiles(sections)

    // Part One
    Day20().partOne(tiles)

    // Part Two
    val duration = measureTime { Day20().partTwo(tiles.toMutableList()) }
    println("Part two duration : $duration")
}

data class Tile(val id: Int, var grid: List<String>) {

    var isCorner: Boolean = false

    // There are only eight. If you rotate twice, you get the same result as flipping in both directions.
    // i.e : all rotations and flip x
    // N, E, S, W, N reversed, E reversed, S reversed, W reversed
    lateinit var edges: List<String>

    init {
        updateEdges()
    }

    private fun updateEdges() {

        val height = grid.size
        val width = grid[0].length

        val north = StringBuilder()
        val east = StringBuilder()
        val south = StringBuilder()
        val west = StringBuilder()

        for (row in grid.indices) {
            north.append(grid[0][row])
            east.append(grid[row][width - 1])
            south.append(grid[height - 1][row])
            west.append(grid[row][0])
        }

        val list = mutableListOf<String>()

        list.add(north.toString())
        list.add(east.toString())
        list.add(south.toString())
        list.add(west.toString())
        list.add(north.reverse().toString())
        list.add(east.reverse().toString())
        list.add(south.reverse().toString())
        list.add(west.reverse().toString())

        edges = list
    }

    fun flipHorizontal() {
        grid = grid.map { str -> str.reversed() }
        updateEdges()
    }

    fun rotateClockwise() {
        val stringBuilders: MutableList<StringBuilder> = mutableListOf()

        for (i in grid[0].indices) {
            stringBuilders.add(StringBuilder())
        }

        for (i in grid.indices) {
            val row = grid[grid.size - 1 - i]
            for (j in stringBuilders.indices) {
                stringBuilders[j].append(row[j])
            }
        }
        grid = stringBuilders.map { sb: StringBuilder -> sb.toString() }
        updateEdges()
    }


    fun display() {
        println()
        println(id)
        grid.forEach { str -> println(str) }
    }
}

fun extractTiles(sections: List<String>): List<Tile> {

    return sections.map { section ->
        val parts = section.split("\n")
        val tileId = parts[0].substringAfter("Tile ").substringBefore(":").toInt()
        val tile = parts.drop(1)
        Tile(tileId, tile)
    }
}

class Day20 {

    enum class Edges {
        NORTH, EAST, SOUTH, WEST, REVERSED_NORTH, REVERSED_EAST, REVERSED_SOUTH, REVERSED_WEST
    }

    // we only need to find ids of corners, no need to match anything else
    fun partOne(tiles: List<Tile>) {

        val corners = findCorners(tiles)

        val result = corners.map { tile -> tile.id.toLong() }.reduce { acc: Long, l -> l * acc }

        println("Part two = $result")
    }

    fun partTwo(tiles: MutableList<Tile>) {

        // puzzle is a square
        val puzzleSize = sqrt(tiles.size.toDouble()).toInt()
        // tiles are square
        val tileSize = tiles[0].grid.size

        val puzzle = solvePuzzle(puzzleSize, tiles)
        var image = buildImage(puzzleSize, tileSize, puzzle)
        image = revealSeaMonsters(image)

        val result = image.map { line -> line.count { it == '#' } }.reduce { acc, i -> acc + i }

        println("Part two = $result")
    }


    private fun solvePuzzle(puzzleSize: Int, tiles: MutableList<Tile>): Array<Array<Tile>> {

        val corners = findCorners(tiles)

        val leftRightCorner = corners.first()
        // rotate / flip corner until north and west edge has not match
        findCorrectOrientationForFirstCorner(tiles, leftRightCorner)

        val puzzle = Array(puzzleSize) { arrayOfNulls<Tile>(puzzleSize) }
        puzzle[0][0] = leftRightCorner
        tiles.remove(leftRightCorner)

        // solve puzzle : get all other tiles
        for (row in 0 until puzzleSize) {
            for (column in 0 until puzzleSize) {
                if (puzzle[row][column] == null) { // skip top left corner
                    val nextTile: Tile = if (row == 0) {
                        getNextToEast(tiles, puzzle[0][column - 1]!!)
                    } else {
                        getNextToSouth(tiles, puzzle[row - 1][column]!!)
                    }
                    puzzle[row][column] = nextTile
                    tiles.remove(nextTile)
                }
            }
        }
        return puzzle as Array<Array<Tile>> // don't know how to figure this 
    }

    /**
     * match with south edge of tile to north
     */
    private fun getNextToSouth(tiles: MutableList<Tile>, northTile: Tile): Tile {
        val edgeToMatch: String = northTile.edges[Edges.SOUTH.ordinal] // South

        for (tile in tiles) {
            if (tile.edges.contains(edgeToMatch)) {
                when (tile.edges.indexOf(edgeToMatch)) {
                    Edges.NORTH.ordinal -> {
                        // Nothing to do
                    }
                    Edges.EAST.ordinal -> {
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                    }
                    Edges.SOUTH.ordinal -> {
                        tile.flipHorizontal()
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                    }
                    Edges.WEST.ordinal -> {
                        tile.rotateClockwise()
                        tile.flipHorizontal()
                    }
                    Edges.REVERSED_NORTH.ordinal -> {
                        tile.flipHorizontal()
                    }
                    Edges.REVERSED_EAST.ordinal -> {
                        tile.flipHorizontal()
                        tile.rotateClockwise()
                    }
                    Edges.REVERSED_SOUTH.ordinal -> {
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                    }
                    Edges.REVERSED_WEST.ordinal -> {
                        tile.rotateClockwise()
                    }
                }
                return tile
            }
        }
        throw RuntimeException()
    }

    /**
     * match with east edge of tile to west
     */
    private fun getNextToEast(tiles: MutableList<Tile>, eastTile: Tile): Tile {

        val edgeToMatch: String = eastTile.edges[Edges.EAST.ordinal] // East

        for (tile in tiles) {
            if (tile.edges.contains(edgeToMatch)) {
                when (tile.edges.indexOf(edgeToMatch)) {
                    Edges.NORTH.ordinal -> {
                        tile.flipHorizontal()
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                    }
                    Edges.EAST.ordinal -> {
                        tile.flipHorizontal()
                    }
                    Edges.SOUTH.ordinal -> {
                        tile.rotateClockwise()
                    }
                    Edges.WEST.ordinal -> {
                        // Nothing to do
                    }
                    Edges.REVERSED_NORTH.ordinal -> {
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                    }
                    Edges.REVERSED_EAST.ordinal -> {
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                    }
                    Edges.REVERSED_SOUTH.ordinal -> {
                        tile.flipHorizontal()
                        tile.rotateClockwise()
                    }
                    Edges.REVERSED_WEST.ordinal -> {
                        tile.flipHorizontal()
                        tile.rotateClockwise()
                        tile.rotateClockwise()
                    }
                }
                return tile
            }
        }
        throw RuntimeException()
    }


    /**
     * Find correct orientation for first corner
     * rotate top left corner piece until the two sides that don't match are on the outside
     */
    private fun findCorrectOrientationForFirstCorner(tiles: List<Tile>, firstCorner: Tile) {

        val edgeWithMatch = getEdgeWithMatch(tiles, firstCorner)

        if (!edgeWithMatch[Edges.NORTH.ordinal] && edgeWithMatch[Edges.EAST.ordinal] && edgeWithMatch[Edges.SOUTH.ordinal] && !edgeWithMatch[Edges.WEST.ordinal]) {
            // nothing to do
        } else if (edgeWithMatch[Edges.NORTH.ordinal] && edgeWithMatch[Edges.EAST.ordinal] && !edgeWithMatch[Edges.SOUTH.ordinal] && !edgeWithMatch[Edges.WEST.ordinal]) {
            firstCorner.rotateClockwise()
        } else if (edgeWithMatch[Edges.NORTH.ordinal] && !edgeWithMatch[Edges.EAST.ordinal] && !edgeWithMatch[Edges.SOUTH.ordinal] && edgeWithMatch[Edges.WEST.ordinal]) {
            firstCorner.rotateClockwise()
            firstCorner.rotateClockwise()
        } else if (!edgeWithMatch[Edges.NORTH.ordinal] && !edgeWithMatch[Edges.EAST.ordinal] && edgeWithMatch[Edges.SOUTH.ordinal] && edgeWithMatch[Edges.WEST.ordinal]) {
            firstCorner.rotateClockwise()
            firstCorner.rotateClockwise()
            firstCorner.rotateClockwise()
        }
    }

    private fun getEdgeWithMatch(tiles: List<Tile>, tile: Tile): BooleanArray {

        // look for edges without a match : N, E, S, W only
        val edgeHasAMatch = BooleanArray(4) { false }

        for (tileToTest in tiles) {
            if (tile != tileToTest) {
                // N, E, S, W
                tile.edges.take(4).forEachIndexed { index, edge ->
                    edgeHasAMatch[index] = edgeHasAMatch[index] || tileToTest.edges.contains(edge)
                }
            }
        }

        return edgeHasAMatch
    }

    private fun findCorners(tiles: List<Tile>): List<Tile> {

        for (tile in tiles) {
            // look for edges without a match : N, E, S, W only
            val edgeHasAMatch = BooleanArray(4) { false }

            for (tileToTest in tiles) {
                if (tile != tileToTest) {
                    tile.edges.take(4).forEachIndexed { index, edge ->
                        edgeHasAMatch[index] = edgeHasAMatch[index] || tileToTest.edges.contains(edge)
                    }
                }
            }
            tile.isCorner = edgeHasAMatch.count { it } == 2
        }

        return tiles.filter { tile -> tile.isCorner }
    }

    /**
     *  Create a 2D array containing all tiles without edges
     */
    private fun buildImage(puzzleSize: Int, tileSize: Int, puzzle: Array<Array<Tile>>): Array<CharArray> {
        val image = Array(puzzleSize * (tileSize - 2)) { CharArray(puzzleSize * (tileSize - 2)) }

        for (rowPuzzle in 0 until puzzleSize) {
            for (columnPuzzle in 0 until puzzleSize) {
                val tile = puzzle[rowPuzzle][columnPuzzle]
                val grid: Array<CharArray> = tile.grid.map { str -> str.toCharArray() }.toTypedArray()

                var rowImage = rowPuzzle * (tileSize - 2)
                var columnImage = columnPuzzle * (tileSize - 2)
                for (rowGrid in 1..grid.size - 2) {
                    for (columnGrid in 1..grid[0].size - 2) {
                        image[rowImage][columnImage] = grid[rowGrid][columnGrid]
                        columnImage++
                    }
                    rowImage++
                    columnImage = columnPuzzle * (tileSize - 2)
                }
            }
        }

        return image
    }

    fun rotateClockwise(grid: Array<CharArray>): Array<CharArray> {
        val copy = Array(grid.size) { CharArray(grid[0].size) }

        for (row in copy.indices) {
            for (column in copy[0].indices) {
                copy[row][column] = grid[grid.size - 1 - column][row]
            }
        }
        return copy
    }

    fun flipHorizontal(grid: Array<CharArray>): Array<CharArray> {
        val copy = Array(grid.size) { CharArray(grid[0].size) }

        for (r in copy.indices) {
            for (c in copy[0].indices) {
                copy[r][c] = grid[r][grid[0].size - 1 - c]
            }
        }
        return copy
    }

    private fun revealSeaMonsters(image: Array<CharArray>): Array<CharArray> {

        val seaMonster = """
                              # 
            #    ##    ##    ###
             #  #  #  #  #  #   
        """.trimIndent()
            .split("\n")
            .flatMapIndexed { row, str ->
                str.mapIndexedNotNull { column, c ->
                    if (c == '#') Pair(
                        row,
                        column
                    ) else null
                }
            }

        var image1 = image
        for (i in 0..3) {
            doRevealSeaMonsters(image1, seaMonster)
            image1 = rotateClockwise(image1)
        }
        image1 = flipHorizontal(image1)
        for (i in 0..3) {
            doRevealSeaMonsters(image1, seaMonster)
            image1 = rotateClockwise(image1)
        }
        return image1
    }

    fun doRevealSeaMonsters(image: Array<CharArray>, seaMonster: List<Pair<Int, Int>>) {
        val seaMonsterHeight = seaMonster.maxOf { pair -> pair.first }
        val seaMonsterWidth = seaMonster.maxOf { pair -> pair.second }

        for (row in image.indices) {
            for (column in image[0].indices) {
                if (column + seaMonsterWidth < image[0].size && row + seaMonsterHeight < image.size) {
                    if (seaMonster.all { pair -> image[row + pair.first][column + pair.second] == '#' }) {
                        seaMonster.forEach { pair -> image[row + pair.first][column + pair.second] = 'O' }
                    }
                }
            }
        }
    }
}

