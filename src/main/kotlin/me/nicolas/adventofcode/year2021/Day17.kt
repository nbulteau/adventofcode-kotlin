package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import kotlin.math.absoluteValue
import kotlin.math.max

// https://adventofcode.com/2021/day/17
fun main() {
    val data = "target area: x=60..94, y=-171..-136"
    val day = Day17(2021, 17, "Trick Shot")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day17(year: Int, day: Int, title: String = "Trick Shot") : AdventOfCodeDay(year, day, title) {

    fun partOne(input: String): Int {
        val (rangeX: IntRange, rangeY: IntRange) = parseInput(input)
        var bestElevation = 0

        for (dx in (0..rangeX.last)) {
            for (dy in (rangeY.first..rangeY.first.absoluteValue)) {
                var position = Position(0, 0)
                var velocity = Velocity(dx, dy)
                val fire = mutableListOf(position)

                while (!position.isInTarget(rangeX, rangeY)) {
                    val (newPosition, newVelocity) = step(position, velocity)
                    if (newPosition.x > rangeX.last || newPosition.y < rangeY.first) {
                        break  // has overshot the target
                    }
                    position = newPosition
                    velocity = newVelocity
                    fire += position
                }
                if (position.isInTarget(rangeX, rangeY)) {
                    bestElevation = max(bestElevation, fire.maxOf { it.y })
                }
            }
        }

        return bestElevation
    }

    fun partTwo(input: String): Int {
        val (rangeX: IntRange, rangeY: IntRange) = parseInput(input)
        var count = 0

        for (dx in (0..rangeX.last)) {
            for (dy in (rangeY.first..rangeY.first.absoluteValue)) {
                var position = Position(0, 0)
                var velocity = Velocity(dx, dy)

                while (!position.isInTarget(rangeX, rangeY)) {
                    val (newPosition, newVelocity) = step(position, velocity)
                    if (newPosition.x > rangeX.last || newPosition.y < rangeY.first) {
                        break  // has overshot the target
                    }
                    position = newPosition
                    velocity = newVelocity
                }
                if (position.isInTarget(rangeX, rangeY)) {
                    count++
                }
            }
        }
        return count
    }

    data class Position(val x: Int, val y: Int) {
        fun isInTarget(rangeX: IntRange, rangeY: IntRange) = x in rangeX && y in rangeY
    }

    data class Velocity(val dx: Int, val dy: Int)

    private fun parseInput(input: String): Pair<IntRange, IntRange> {
        val rangeX = input.substringAfter("=").substringBefore(".").toInt()..
                input.substringAfter("..").substringBefore(",").toInt()
        val rangeY = input.substringAfterLast("=").substringBefore(".").toInt()..
                input.substringAfterLast(".").toInt()

        return Pair(rangeX, rangeY)
    }

    private fun step(position: Position, velocity: Velocity): Pair<Position, Velocity> {
        val nextPosition = Position(position.x + velocity.dx, position.y + velocity.dy)

        val xVelocity =
            if (velocity.dx > 0) velocity.dx - 1
            else if (velocity.dx < 0) velocity.dx + 1
            else 0
        val nextVelocity = Velocity(xVelocity, velocity.dy - 1)

        return Pair(nextPosition, nextVelocity)
    }
}
