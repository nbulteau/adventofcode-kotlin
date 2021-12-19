package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.math.abs
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


// https://adventofcode.com/2021/day/19
@ExperimentalTime
fun main() {

    val training = readFileDirectlyAsText("/year2021/day19/training.txt")
    val data = readFileDirectlyAsText("/year2021/day19/data.txt")

    val inputs = data.split("\n\n")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day19Bis().partOne(inputs) })

    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day19Bis().partTwo(inputs) })
}

private class Day19Bis {
    fun partOne(inputs: List<String>): Int {

        val reports = parseReports(inputs)

        return solve(reports).flatMap { it.beacons }.toSet().size
    }

    fun partTwo(inputs: List<String>): Int {
        val reports = parseReports(inputs)
        val positions = solve(reports).map { it.position }

        var second = positions.toList()
        val result = positions.flatMap { pos1 ->
            second = second.drop(1)
            second.map { pos2 ->
                pos1.manhattanDistance(pos2)
            }
        }.maxOf { it }

        return result
    }

    private fun solve(reports: List<Report>): List<Scanner> {
        // scanner 0 is Point3D(0, 0, 0)
        val scanner0 = Scanner(Point3D(0, 0, 0), reports.first().beacons(mode = 0))

        val solvedScanners = mutableListOf(scanner0)
        val reportsToProcess = reports.drop(1).toMutableList()

        while (reportsToProcess.isNotEmpty()) {
            val currentReport = reportsToProcess.removeAt(0)

            val matchingReports = solvedScanners.mapNotNull { scanner ->
                scanner.findMatchingReports(currentReport)
            }

            if (matchingReports.isEmpty()) {
                reportsToProcess.add(currentReport)
            } else {
                solvedScanners.add(matchingReports.first())
            }
        }

        return solvedScanners
    }


    data class Scanner(val position: Point3D, val beacons: Set<Point3D>) {

        fun findMatchingReports(testedReport: Report): Scanner? {
            // 8 * 8 = 48 distinct modes
            for (mode in 0..47) {
                val testedReportBeacons: Set<Point3D> = testedReport.beacons(mode)
                for (reportPoint in testedReportBeacons) {
                    for (scanner0Point in this.beacons) {
                        val pointRelativeToScanner0: Point3D = scanner0Point - reportPoint
                        val beacons: Set<Point3D> = testedReportBeacons.map { point ->
                            point + pointRelativeToScanner0
                        }.toSet()
                        // count overlapping beacons
                        if (beacons.count { point -> point in this.beacons } >= 12) {
                            return Scanner(pointRelativeToScanner0, beacons)
                        }
                    }
                }
            }
            return null
        }
    }

    data class Point3D(val x: Int, val y: Int, val z: Int) {

        operator fun minus(other: Point3D): Point3D = Point3D(x - other.x, y - other.y, z - other.z)

        operator fun plus(other: Point3D): Point3D = Point3D(x + other.x, y + other.y, z + other.z)

        fun manhattanDistance(other: Point3D): Int = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    class Report(private var beacons: Set<Point3D>) {

        fun beacons(mode: Int): Set<Point3D> {
            return beacons.map { it.swapAxes(mode) }
                .map {
                    when (mode % 8) {
                        0 -> Point3D(x = it.x, y = it.y, z = it.z)
                        1 -> Point3D(x = it.x, y = it.y * -1, z = it.z)
                        2 -> Point3D(x = it.x, y = it.y * -1, z = it.z * -1)
                        3 -> Point3D(x = it.x, y = it.y, z = it.z * -1)
                        4 -> Point3D(x = it.x * -1, y = it.y, z = it.z)
                        5 -> Point3D(x = it.x * -1, y = it.y * -1, z = it.z)
                        6 -> Point3D(x = it.x * -1, y = it.y * -1, z = it.z * -1)
                        7 -> Point3D(x = it.x * -1, y = it.y, z = it.z * -1)
                        else -> throw RuntimeException("Unknown mode $mode")
                    }
                }.toSet()
        }

        private fun Point3D.swapAxes(mode: Int): Point3D {
            return when (mode / 8) {
                0 -> Point3D(x, y, z)
                1 -> Point3D(x, z, y)
                2 -> Point3D(y, x, z)
                3 -> Point3D(y, z, x)
                4 -> Point3D(z, x, y)
                5 -> Point3D(z, y, x)
                else -> throw RuntimeException("Unknown mode $mode")
            }
        }
    }

    private fun parseReports(inputs: List<String>): List<Report> {

        return inputs.map { line ->
            Report(line.split("\n").drop(1).map {
                it.split(",").map(String::toInt).let { (x, y, z) ->
                    Point3D(x, y, z)
                }
            }.toSet())
        }
    }
}