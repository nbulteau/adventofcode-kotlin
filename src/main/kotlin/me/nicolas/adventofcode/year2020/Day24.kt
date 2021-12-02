package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 24:  Lobby Layout ---
// https://adventofcode.com/2020/day/24
@ExperimentalTime
fun main() {

    println("--- Day 24: Lobby Layout ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day24/training.txt")
    val data = readFileDirectlyAsText("/year2020/day24/data.txt")

    val lines = data.split("\n")

    // Part One
    Day24().partOne(lines)

    // Part Two (168 s)
    //val duration = measureTime { Day24().partTwo(lines) }
    //println("Part two duration : $duration")

    // Part Two optimized (234 ms)
    val durationOptimized = measureTime { Day24().partTwoOptimized(lines) }
    println("Part two duration : $durationOptimized")
}

// https://www.redblobgames.com/grids/hexagons/
class Day24 {

    enum class Direction(val label: String, val dx: Int, val dy: Int) {

        EAST("e", 1, 0),
        SOUTH_EAST("se", 1, 1),
        SOUTH_WEST("sw", 0, 1),
        WEST("w", -1, 0),
        NORTH_WEST("nw", -1, -1),
        NORTH_EAST("ne", 0, -1);

        companion object {
            fun byLabel(label: String) = values().first { direction -> label == direction.label }
        }
    }

    data class Coord(var x: Int, var y: Int) {

        fun move(direction: Direction) {
            this.x += direction.dx
            this.y += direction.dy
        }
    }

    /**
     * @param color false = white true = black
     */
    private data class Tile(var coord: Coord, var color: Boolean = false)

    private data class Lobby(var tiles: MutableSet<Tile> = mutableSetOf()) {

        fun flip(instructions: List<List<Direction>>) {

            instructions.forEach { instruction ->
                val coord = Coord(0, 0)
                instruction.forEach { direction -> coord.move(direction) }

                val tile = tiles.find { tile -> tile.coord == coord }
                if (tile != null) {
                    tile.coord = coord
                    tile.color = !tile.color
                } else {
                    tiles.add(Tile(coord, true)) // set black
                }
            }
        }

        fun countBlackAdjacentTiles(coord: Coord): Int {

            return Direction.values()
                .flatMap { direction ->
                    tiles.filter { tile -> tile.coord == Coord(coord.x + direction.dx, coord.y + direction.dy) }
                }
                .count { tile -> tile.color }
        }

        fun dallyFlip() {
            val newDayTiles = mutableSetOf<Tile>()

            tiles.forEach { tile ->
                // deal with black
                var nbBlackAdjacentTiles = countBlackAdjacentTiles(tile.coord)
                if (tile.color) { // is black
                    if (nbBlackAdjacentTiles == 1 || nbBlackAdjacentTiles == 2) {
                        newDayTiles.add(Tile(tile.coord, true))
                    }
                }
                // now look around for adjacent whites
                Direction.values().forEach { direction ->
                    val coord = Coord(tile.coord.x + direction.dx, tile.coord.y + direction.dy)
                    val adjacentTile = tiles.find { tile -> tile.coord == coord }
                    // adjacentTile is white
                    if (adjacentTile == null) {
                        nbBlackAdjacentTiles = countBlackAdjacentTiles(coord)
                        if (nbBlackAdjacentTiles == 2) {
                            newDayTiles.add(Tile(coord, true))
                        }
                    }
                }
            }
            tiles = newDayTiles
        }

        fun removeWhiteTiles() {
            tiles = tiles.filter { tile -> tile.color }.toMutableSet()
        }
    }

    fun extractDirections(line: String): List<Direction> {
        val directions = mutableListOf<Direction>()
        var index = 0
        while (index < line.length) {
            val move = when (val char = line[index]) {
                'e', 'w' -> Direction.byLabel(char.toString())
                's', 'n' -> {
                    Direction.byLabel((char.toString() + line[++index]))
                }
                else -> throw RuntimeException()
            }
            index++
            directions.add(move)
        }
        return directions
    }

    fun partOne(lines: List<String>) {

        val instructions = lines.map { str -> extractDirections(str) }

        val lobby = Lobby()
        lobby.flip(instructions)
        val result = lobby.tiles.filter { tile -> tile.color }.size

        println("Part one = $result")
    }


    fun partTwo(lines: List<String>) {

        val instructions = lines.map { str -> extractDirections(str) }

        // init
        val lobby = Lobby()
        lobby.flip(instructions)
        lobby.removeWhiteTiles()

        // 100 days
        for (index in 1..100) {
            lobby.dallyFlip()
            println("Day $index: ${lobby.tiles.filter { tile -> tile.color }.size}")
        }
        val result = lobby.tiles.filter { tile -> tile.color }.size

        println("Part two = $result")
    }

    /**
     * Lobby -> a Set of black tiles to avoid costly "find"
     */
    fun partTwoOptimized(lines: List<String>) {

        val instructions = lines.map { str -> extractDirections(str) }

        // init
        val lobby = Lobby()
        lobby.flip(instructions)
        lobby.removeWhiteTiles()

        var blackTileCoords = lobby.tiles.map { tile -> tile.coord }.toSet()

        // 100 days
        for (index in 1..100) {
            blackTileCoords = dallyFlip(blackTileCoords)
            println("Day $index: ${blackTileCoords.size}")
        }

        println("Part two = ${blackTileCoords.size}")
    }

    fun countBlackAdjacentTiles(blackTileCoords: Set<Coord>, coord: Coord): Int {

        return Direction.values()
            .map { direction -> blackTileCoords.contains(Coord(coord.x + direction.dx, coord.y + direction.dy)) }
            .count { it }
    }

    fun dallyFlip(blackTileCoords: Set<Coord>): Set<Coord> {
        val newDayCoords = mutableSetOf<Coord>()

        blackTileCoords.forEach { coord ->
            // deal with black
            var nbBlackAdjacentTiles = countBlackAdjacentTiles(blackTileCoords, coord)
            // is black
            if (nbBlackAdjacentTiles == 1 || nbBlackAdjacentTiles == 2) {
                newDayCoords.add(coord)
            }

            // now look around for adjacent whites
            Direction.values().forEach { direction ->
                val aroundCord = Coord(coord.x + direction.dx, coord.y + direction.dy)
                // adjacentTile is white
                if (!blackTileCoords.contains(aroundCord)) {
                    nbBlackAdjacentTiles = countBlackAdjacentTiles(blackTileCoords, aroundCord)
                    if (nbBlackAdjacentTiles == 2) {
                        newDayCoords.add(aroundCord)
                    }
                }
            }
        }
        return newDayCoords
    }
}