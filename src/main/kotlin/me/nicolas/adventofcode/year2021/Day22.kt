package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2021/day/22
fun main() {
    val data = readFileDirectlyAsText("/year2021/day22/data.txt")
    val day = Day22(2021, 22, "Reactor Reboot")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day22(year: Int, day: Int, title: String = "Reactor Reboot") : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Int {
        val input = data.split("\n")

        val steps = parseInput(input)

        return GridPartOne().reboot(steps)
    }

    fun partTwo(data: String): Long {
        val input = data.split("\n").filter { it.isNotEmpty() }

        val steps = parseInput(input)

        return GridPartTwo().reboot(steps)
    }

    data class Point3D(val x: Int, val y: Int, val z: Int)

    data class Cube(val value: Int, val rangeX: IntRange, val rangeY: IntRange, val rangeZ: IntRange)

    class GridPartOne(private val boundaries: IntRange = -50..50) {

        fun reboot(rebootSteps: List<Cube>): Int {
            val points = mutableSetOf<Point3D>()

            for (cube in rebootSteps) {
                val rangeX = cube.rangeX.coerce(boundaries)
                val rangeY = cube.rangeY.coerce(boundaries)
                val rangeZ = cube.rangeZ.coerce(boundaries)

                rangeX.forEach { x ->
                    rangeY.forEach { y ->
                        rangeZ.forEach { z ->
                            val point = Point3D(x, y, z)
                            if (cube.value == 1) {
                                points.add(point)
                            } else {
                                points.remove(point)
                            }
                        }
                    }
                }
            }

            return points.size
        }

        private fun IntRange.coerce(boundaries: IntRange): IntRange {
            val low = boundaries.first
            val high = boundaries.last
            return this.first.coerceIn(low, high + 1)..this.last.coerceIn(low - 1, high)
        }
    }

    class GridPartTwo {

        fun reboot(cubesToSet: List<Cube>): Long {
            val setCubes = mutableListOf<Cube>()

            for (step in cubesToSet) {
                val intersectionCubes = getIntersectionCubes(setCubes, step)
                // add all intersections cubes
                setCubes.addAll(intersectionCubes)
                if (step.value > 0) {
                    setCubes.add(step)
                }
            }

            return setCubes.sumOf { cube ->
                cube.value.toLong() *
                        (cube.rangeX.last - cube.rangeX.first + 1) *
                        (cube.rangeY.last - cube.rangeY.first + 1) *
                        (cube.rangeZ.last - cube.rangeZ.first + 1)
            }
        }

        private fun getIntersectionCubes(setCubes: MutableList<Cube>, step: Cube): MutableList<Cube> {
            val intersectCubes = mutableListOf<Cube>()

            setCubes.forEach { cube ->
                // find all intersections cube
                val minX = max(step.rangeX.first, cube.rangeX.first)
                val maxX = min(step.rangeX.last, cube.rangeX.last)
                val minY = max(step.rangeY.first, cube.rangeY.first)
                val maxY = min(step.rangeY.last, cube.rangeY.last)
                val minZ = max(step.rangeZ.first, cube.rangeZ.first)
                val maxZ = min(step.rangeZ.last, cube.rangeZ.last)

                if (minX <= maxX && minY <= maxY && minZ <= maxZ) {
                    // -cube.value to not set cube on twice
                    intersectCubes.add(Cube(-cube.value, minX..maxX, minY..maxY, minZ..maxZ))
                }
            }

            return intersectCubes
        }
    }

    private fun parseInput(inputs: List<String>): List<Cube> {
        val lineRegex = Regex("(on|off) x=(-?\\d+)\\.\\.(-?\\d+),y=(-?\\d+)\\.\\.(-?\\d+),z=(-?\\d+)\\.\\.(-?\\d+)")

        val cubes = inputs.map { line ->
            val (onOff, xStart, xEnd, yStart, yEnd, zStart, zEnd) = lineRegex.matchEntire(line)!!.destructured
            val rangeX = xStart.toInt()..xEnd.toInt()
            val rangeY = yStart.toInt()..yEnd.toInt()
            val rangeZ = zStart.toInt()..zEnd.toInt()

            Cube(if (onOff == "on") 1 else 0, rangeX, rangeY, rangeZ)
        }

        return cubes
    }
}
