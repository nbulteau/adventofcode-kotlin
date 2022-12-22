package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day22/training.txt")
    val data = readFileDirectlyAsText("/year2022/day22/data.txt")

    val (mapOfTheBoard, pathYouMustFollow) = data.split("\n\n")

    val day =
        Day22("--- Day 22: Monkey Map ---", "https://adventofcode.com/2222/day/22", mapOfTheBoard, pathYouMustFollow)
    prettyPrintPartOne { day.partOne() }
    prettyPrintPartTwo { day.partTwo() }
}

private class Day22(title: String, adventOfCodeLink: String, mapOfTheBoard: String, val pathYouMustFollow: String) :
    AdventOfCodeDay(title, adventOfCodeLink) {

    private val map: Map<Pair<Int, Int>, Char>

    init {
        this.map = mapOfTheBoard.split('\n').flatMapIndexed { column, string ->
            string.asIterable()
                .mapIndexedNotNull { row, char -> if (char == ' ') null else Pair(Pair(row, column), char) }
        }.toMap()
    }

    private enum class Direction {
        RIGHT, DOWN, LEFT, UP;

        fun turn(char: Char): Direction {
            val turn = ordinal + if (char == 'R') 1 else -1
            val newDirection = turn % Direction.values().size
            val index = if (newDirection < 0) Direction.values().size -1 else newDirection
            return Direction.values()[index]
        }

        fun moveOneStep(currentLocation: Pair<Int, Int>): Pair<Int, Int> {
            return when (this) {
                RIGHT -> Pair(currentLocation.first + 1, currentLocation.second)
                DOWN -> Pair(currentLocation.first, currentLocation.second + 1)
                LEFT -> Pair(currentLocation.first - 1, currentLocation.second)
                UP -> Pair(currentLocation.first, currentLocation.second - 1)
            }
        }
    }

    fun partOne(): Long {
        var currentLoc: Pair<Int, Int> = map.keys.find { point -> map[point] == '.' && point.second == 0 }!!
        var facing: Direction = Direction.RIGHT

        var index = 0
        while (index < pathYouMustFollow.length) {
            if (pathYouMustFollow[index] == 'L' || pathYouMustFollow[index] == 'R') {
                // turn instruction
                facing = facing.turn(pathYouMustFollow[index])
                index++
            } else {
                // steps instruction
                val start = index
                while (index < pathYouMustFollow.length && pathYouMustFollow[index].isDigit()) {
                    index++
                }
                val steps: Int = pathYouMustFollow.substring(start, index).toInt()

                repeat(steps) {
                    val newPosition = facing.moveOneStep(currentLoc)

                    if (map[newPosition] == null) {
                        val wrappedPosition = wrapPartOne(currentLoc, facing)
                        if (map[wrappedPosition] == '.') {
                            currentLoc = wrappedPosition
                        }
                    } else if (map[newPosition] == '.') {
                        currentLoc = newPosition
                    }
                    // else map[newLoc] == '#'
                }
            }
        }

        return 1000L * (currentLoc.second + 1) + 4 * (currentLoc.first + 1) + facing.ordinal
    }

    fun partTwo(): Long {
        var currentLoc: Pair<Int, Int> = map.keys.find { point -> map[point] == '.' && point.second == 0 }!!
        var facing: Direction = Direction.RIGHT

        var index = 0
        while (index < pathYouMustFollow.length) {
            if (pathYouMustFollow[index] == 'L' || pathYouMustFollow[index] == 'R') {
                // turn instruction
                facing = facing.turn(pathYouMustFollow[index])
                index++
            } else {
                // steps instruction
                val start = index
                while (index < pathYouMustFollow.length && pathYouMustFollow[index].isDigit()) {
                    index++
                }
                val steps: Int = pathYouMustFollow.substring(start, index).toInt()

                repeat(steps) {
                    val newPosition = facing.moveOneStep(currentLoc)

                    if (map[newPosition] == null) {
                        val (wrappedPosition, wrappedDirection) = wrapPartTwo(currentLoc, facing)
                        if (map[wrappedPosition] == '.') {
                            currentLoc = wrappedPosition
                            facing = wrappedDirection
                        }
                    } else if (map[newPosition] == '.') {
                        currentLoc = newPosition
                    }
                    // else map[newLoc] == '#'
                }
            }
        }

        return 1000L * (currentLoc.second + 1) + 4 * (currentLoc.first + 1) + facing.ordinal
    }

    private fun wrapPartOne(position: Pair<Int, Int>, facing: Direction) = when (facing) {
        Direction.RIGHT -> Pair(map.keys.first { point -> point.second == position.second }.first, position.second)
        Direction.DOWN -> Pair(position.first, map.keys.first { point -> point.first == position.first }.second)
        Direction.LEFT -> Pair(map.keys.last { point -> point.second == position.second }.first, position.second)
        Direction.UP -> Pair(position.first, map.keys.last { point -> point.first == position.first }.second)
    }

    private fun wrapPartTwo(position: Pair<Int, Int>, facing: Direction): Pair<Pair<Int, Int>, Direction> {
        val positionX = position.first
        val positionY = position.second

        var wrapPosition: Pair<Int, Int>? = null
        var wrapDirection: Direction? = null

        when (facing) {
            Direction.RIGHT -> {
                if (positionX == 149 && positionY in 0..49) {
                    wrapPosition = Pair(99, 100 + (49 - positionY))
                    wrapDirection = Direction.LEFT
                } else if (positionX == 99 && positionY in 50..99) {
                    wrapPosition = Pair(100 + (positionY - 50), 49)
                    wrapDirection = Direction.UP
                } else if (positionX == 99 && positionY in 100..149) {
                    wrapPosition = Pair(149, 0 + (149 - positionY))
                    wrapDirection = Direction.LEFT
                } else if (positionX == 49 && positionY in 150..199) {
                    wrapPosition = Pair(50 + (positionY - 150), 149)
                    wrapDirection = Direction.UP
                }
            }
            Direction.DOWN -> {
                if (positionX in 0..49 && positionY == 199) {
                    wrapPosition = Pair(100 + positionX, 0)
                    wrapDirection = Direction.DOWN
                } else if (positionX in 50..99 && positionY == 149) {
                    wrapPosition = Pair(49, 150 + (positionX - 50))
                    wrapDirection = Direction.LEFT
                } else if (positionX in 100..149 && positionY == 49) {
                    wrapPosition = Pair(99, 50 + (positionX - 100))
                    wrapDirection = Direction.LEFT
                }
            }
            Direction.LEFT -> {
                if (positionX == 50 && positionY in 0..49) {
                    wrapPosition = Pair(0, 100 + (49 - positionY))
                    wrapDirection = Direction.RIGHT
                } else if (positionX == 50 && positionY in 50..99) {
                    wrapPosition = Pair(0 + (positionY - 50), 100)
                    wrapDirection = Direction.DOWN
                } else if (positionX == 0 && positionY in 100..149) {
                    wrapPosition = Pair(50, 0 + (149 - positionY))
                    wrapDirection = Direction.RIGHT
                } else if (positionX == 0 && positionY in 150..199) {
                    wrapPosition = Pair(50 + (positionY - 150), 0)
                    wrapDirection = Direction.DOWN
                }
            }
            Direction.UP -> {
                if (positionX in 0..49 && positionY == 100) {
                    wrapPosition = Pair(50, 50 + positionX)
                    wrapDirection = Direction.RIGHT
                } else if (positionX in 50..99 && positionY == 0) {
                    wrapPosition = Pair(0, 150 + (positionX - 50))
                    wrapDirection = Direction.RIGHT
                } else if (positionX in 100..149 && positionY == 0) {
                    wrapPosition = Pair(positionX - 100, 199)
                    wrapDirection = Direction.UP
                }
            }
        }

        return Pair(wrapPosition!!, wrapDirection!!)
    }
}
 
