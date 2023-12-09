package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 17: Conway Cubes ---
// https://adventofcode.com/2020/day/17
@ExperimentalTime
fun main() {

    println("--- Day 17: Conway Cubes ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day17/training.txt")
    val data = readFileDirectlyAsText("/year2020/day17/data.txt")

    val layout = data.split("\n")

    // Part One
    Day17PartOne().partOne(layout)

    // Part Two
    val duration = measureTime { Day17PartTwo().partTwo(layout) }
    println("Part two duration : $duration")
}

private class Day17PartOne {

    fun partOne(layout: List<String>) {

        // init pocket
        var pocket: MutableMap<CubeCoord, Char> = initPocket(layout)

        for (cycle in 1..6) {
            //pocket.display()

            val nextPocket = pocket.buildListOfCoords().associateWith { coord ->
                val nbNeighborsActiveCube =
                    coord.getNeighborsList().count { pocket.getOrDefault(it, '.') == '#' }

                val cube = pocket.getOrDefault(coord, '.') // inactive if not found
                when (cube) {
                    '#' -> if (nbNeighborsActiveCube == 2 || nbNeighborsActiveCube == 3) '#' else '.'
                    else -> if (nbNeighborsActiveCube == 3) '#' else '.'
                }
            }
            pocket = nextPocket.toMutableMap()
        }

        println("Part one = ${pocket.values.count { cube -> cube == '#' }}")
    }

    data class CubeCoord(val x: Int, val y: Int, val z: Int) {

        // look around
        fun getNeighborsList(): List<CubeCoord> {
            val neighbors = mutableListOf<CubeCoord>()
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        // don't add the coord itself
                        if (!(dx == 0 && dy == 0 && dz == 0)) {
                            neighbors.add(CubeCoord(dx + this.x, dy + this.y, dz + this.z))
                        }
                    }
                }
            }
            return neighbors
        }
    }

    private fun Map<CubeCoord, Char>.buildListOfCoords(): List<CubeCoord> {
        val (minX, maxX) = getXMinMax()
        val (minY, maxY) = getYMinMax()
        val (minZ, maxZ) = getZMinMax()

        val list = mutableListOf<CubeCoord>()
        for (x in minX - 1..maxX + 1) {
            for (y in minY - 1..maxY + 1) {
                for (z in minZ - 1..maxZ + 1) {
                    list.add(CubeCoord(x, y, z))
                }
            }
        }
        return list
    }

    private fun Map<CubeCoord, Char>.display() {
        val (minX, maxX) = getXMinMax()
        val (minY, maxY) = getYMinMax()
        val (minZ, maxZ) = getZMinMax()

        for (z in minZ..maxZ) {
            println("z=$z")
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    print(this[CubeCoord(x, y, z)])
                }
                println()
            }
            println()
        }
    }

    private fun Map<CubeCoord, Char>.getZMinMax(): Pair<Int, Int> {
        val minZ = this.keys.minOf { it.z }
        val maxZ = 0 - minZ
        return Pair(minZ, maxZ)
    }

    private fun Map<CubeCoord, Char>.getYMinMax(): Pair<Int, Int> {
        val minY = this.keys.minOf { it.y }
        val maxY = 0 - minY
        return Pair(minY, maxY)
    }

    private fun Map<CubeCoord, Char>.getXMinMax(): Pair<Int, Int> {
        val minX = this.keys.minOf { it.x }
        val maxX = 0 - minX
        return Pair(minX, maxX)
    }

    private fun initPocket(layout: List<String>): MutableMap<CubeCoord, Char> {

        val offset = layout.size / 2

        return layout.flatMapIndexed { y, row ->
            row.mapIndexed { x, col ->
                CubeCoord(x = y - offset, y = x - offset, z = 0) to col
            }
        }
            .toMap()
            .toMutableMap()
    }
}

private class Day17PartTwo {

    fun partTwo(layout: List<String>) {

        // init pocket
        var pocket: MutableMap<CubeCoord, Char> = initPocket(layout)

        for (cycle in 1..6) {
            val nextPocket = pocket.buildListOfCoords().associateWith { coord ->
                val nbNeighborsActiveCube =
                    coord.getNeighborsCubeCoordList().count { pocket.getOrDefault(it, '.') == '#' }

                val cube = pocket.getOrDefault(coord, '.') // inactive if not found
                when (cube) {
                    '#' -> if (nbNeighborsActiveCube == 2 || nbNeighborsActiveCube == 3) '#' else '.'
                    else -> if (nbNeighborsActiveCube == 3) '#' else '.'
                }
            }
            pocket = nextPocket.toMutableMap()
        }

        println("Part two = ${pocket.values.count { cube -> cube == '#' }}")
    }

    data class CubeCoord(val x: Int, val y: Int, val z: Int, val w: Int) {

        // look around
        fun getNeighborsCubeCoordList(): List<CubeCoord> {
            val neighbors = mutableListOf<CubeCoord>()
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        for (dw in -1..1) {
                            if (!(dx == 0 && dy == 0 && dz == 0 && dw == 0)) {
                                neighbors.add(CubeCoord(dx + this.x, dy + this.y, dz + this.z, dw + this.w))
                            }
                        }
                    }
                }
            }
            return neighbors
        }
    }

    private fun Map<CubeCoord, Char>.buildListOfCoords(): List<CubeCoord> {
        val (minX, maxX) = getXMinMax()
        val (minY, maxY) = getYMinMax()
        val (minZ, maxZ) = getZMinMax()
        val (minW, maxW) = getWMinMax()

        val list = mutableListOf<CubeCoord>()
        for (x in minX - 1..maxX + 1) {
            for (y in minY - 1..maxY + 1) {
                for (z in minZ - 1..maxZ + 1) {
                    for (w in minW - 1..maxW + 1) {
                        list.add(CubeCoord(x, y, z, w))
                    }
                }
            }
        }
        return list
    }

    private fun Map<CubeCoord, Char>.getWMinMax(): Pair<Int, Int> {
        val minW = this.keys.minOf { it.w }
        val maxW = 0 - minW
        return Pair(minW, maxW)
    }

    private fun Map<CubeCoord, Char>.getZMinMax(): Pair<Int, Int> {
        val minZ = this.keys.minOf { it.z }
        val maxZ = 0 - minZ
        return Pair(minZ, maxZ)
    }

    private fun Map<CubeCoord, Char>.getYMinMax(): Pair<Int, Int> {
        val minY = this.keys.minOf { it.y }
        val maxY = 0 - minY
        return Pair(minY, maxY)
    }

    private fun Map<CubeCoord, Char>.getXMinMax(): Pair<Int, Int> {
        val minX = this.keys.minOf { it.x }
        val maxX = 0 - minX
        return Pair(minX, maxX)
    }

    private fun initPocket(layout: List<String>): MutableMap<CubeCoord, Char> {
        val offset = layout.size / 2

        return layout.flatMapIndexed { y, row ->
            row.mapIndexed { x, col ->
                CubeCoord(x = y - offset, y = x - offset, z = 0, w = 0) to col
            }
        }
            .toMap()
            .toMutableMap()
    }
}


