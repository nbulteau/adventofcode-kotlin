package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day22/data.txt")
    val day = Day22(2023, 22, "Sand Slabs")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day22(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val bricks = parseData(data)

        // first step : all the bricks are settled
        val (settledBricks, _) = process(bricks)

        // which bricks are safe to disintegrate
        val t = settledBricks.map { brick ->
            val n = settledBricks.minusElement(brick)
            process(n).second
        }

        // count the number of bricks that are safe to disintegrate i.e. the number of bricks that have not moved
        return t.count { it == 0 }
    }

    fun partTwo(data: String): Int {
        val bricks = parseData(data)

        // first step : all the bricks are settled
        val (settledBricks, _) = process(bricks)

        val t = settledBricks.map { brick ->
            val settledBricksMinusOne = settledBricks.minusElement(brick)
            process(settledBricksMinusOne).second
        }

        return t.sum()
    }

    // returns the new list of bricks and the number of fallen bricks
    private fun process(bricks: List<Set<Triple<Int, Int, Int>>>): Pair<List<Set<Triple<Int, Int, Int>>>, Int> {
        // bricks after they have settled (fallen)
        val bricksAfterSettled = mutableListOf<Set<Triple<Int, Int, Int>>>()
        // All the points of the fallen bricks
        val fallen = hashSetOf<Triple<Int, Int, Int>>()

        var nbBricksThatMoved = 0
        // sort the bricks by the lowest z coordinate
        val sortedBricks = bricks.sortedBy { b -> b.minOf { it.third } }

        for (brick in sortedBricks) {
            var brickToCheck = brick
            while (true) {
                // down the brick one step (z - 1)
                val down = brickToCheck.map { point -> Triple(point.first, point.second, point.third - 1) }.toSet()

                // if any of the points in the brick is in the fallen set or the z coordinate is 0 we have a fallen brick
                if (down.any { it in fallen || it.third <= 0 }) {
                    bricksAfterSettled += brickToCheck
                    fallen += brickToCheck
                    // if the brick moved down we count it
                    if (brickToCheck != brick) {
                        nbBricksThatMoved++
                    }
                    break
                }
                brickToCheck = down
            }
        }

        return bricksAfterSettled to nbBricksThatMoved
    }

    private fun parseData(data: String): List<Set<Triple<Int, Int, Int>>> {
        val bricks = data.lines().map { line ->
            val points = line.split("~").map { point ->
                val (x, y, z) = point.split(",").map{ it.toInt()}
                Triple(x, y, z)
            }
            points.first() to points.last()
        }

        // Build a set of all the points in the brick
        return bricks.map { (point1, point2) ->
            val ps = mutableSetOf<Triple<Int, Int, Int>>()

            for (x in point1.first..point2.first) {
                for (y in point1.second..point2.second) {
                    for (z in point1.third..point2.third) {
                        ps += Triple(x, y, z)
                    }
                }
            }
            ps
        }
    }
}

