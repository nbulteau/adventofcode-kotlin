package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.Grid
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// https://adventofcode.com/2018/day/20
fun main() {
    val data = readFileDirectlyAsText("/year2018/day20/data.txt")
    val day = Day20()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day20(year: Int = 2018, day: Int = 20, title: String = "A Regular Map") : AdventOfCodeDay(year, day, title) {

    private val directions = mapOf(
        'N' to Pair(0, -1),
        'S' to Pair(0, 1),
        'E' to Pair(1, 0),
        'W' to Pair(-1, 0)
    )

    fun partOne(data: String): Int {
        val map = buildMap(data)
         // displayMap(map)

        return findFurthestRoom(map)
    }

    fun partTwo(data: String): Int {
        val map = buildMap(data)
        return findRoomsWithMinDoors(map, 1000)
    }

    private fun buildMap(regex: String): Grid<Int> {
        val map = Grid<Int>()
        val stack = mutableListOf<Pair<Int, Int>>()
        var x = 0
        var y = 0
        map[Pair(x, y)] = 0

        for (char in regex) {
            when (char) {
                '^', '$' -> continue
                'N', 'S', 'E', 'W' -> {
                    val (dx, dy) = directions[char]!!
                    x += dx
                    y += dy
                    map[Pair(x, y)] = map.getOrDefault(Pair(x, y), Int.MAX_VALUE).coerceAtMost(map[Pair(x - dx, y - dy)]!! + 1)
                }
                '(' -> stack.add(Pair(x, y))
                ')' -> {
                    val (prevX, prevY) = stack.removeAt(stack.size - 1)
                    x = prevX
                    y = prevY
                }
                '|' -> {
                    val (prevX, prevY) = stack.last()
                    x = prevX
                    y = prevY
                }
            }
        }

        return map
    }

    private fun displayMap(map: Grid<Int>) {
        val minX = map.minX
        val minY = map.minY
        val maxX = map.maxX
        val maxY = map.maxY

        // Print top border
        println("#".repeat((maxX - minX + 1) * 2 + 1))

        for (y in minY..maxY) {
            // Print left border
            print("#")
            for (x in minX..maxX) {
                if (x == 0 && y == 0) {
                    print('X')
                } else {
                    print(
                        when (map[Pair(x, y)]) {
                            null -> '#'
                            else -> '.'
                        }
                    )
                }
                if (x < maxX) {
                    print(
                        when {
                            map[Pair(x, y)] != null && map[Pair(x + 1, y)] != null -> '|'
                            else -> '#'
                        }
                    )
                }
            }
            // Print right border
            println("#")

            if (y < maxY) {
                // Print left border
                print("#")
                for (x in minX..maxX) {
                    print(
                        when {
                            map[Pair(x, y)] != null && map[Pair(x, y + 1)] != null -> '-'
                            else -> '#'
                        }
                    )
                    if (x < maxX) {
                        print('#')
                    }
                }
                // Print right border
                println("#")
            }
        }

        // Print bottom border
        println("#".repeat((maxX - minX + 1) * 2 + 1))
    }

    private fun findFurthestRoom(map: Grid<Int>): Int {
        return map.toMap().values.maxOrNull() ?: 0
    }

    private fun findRoomsWithMinDoors(map: Grid<Int>, minDoors: Int): Int {
        return map.toMap().values.count { it >= minDoors }
    }
}