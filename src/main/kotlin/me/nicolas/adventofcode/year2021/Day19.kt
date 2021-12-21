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
        val solvedScanners = solve(reports)

        return solvedScanners.flatMap { it.beacons }.toSet().size
    }

    fun partTwo(inputs: List<String>): Int {
        val reports = parseReports(inputs)
        val solvedScanners = solve(reports)
        val positions = solvedScanners.map { it.position }

        return positions.flatMap { pos1 -> positions.map { pos2 -> pos1.manhattanDistance(pos2) } }.maxOf { it }
    }

    private fun solve(reports: List<Report>): List<Scanner> {
        // scanner 0 is Point3D(0, 0, 0)
        val scanner0 = Scanner(Point3D(0, 0, 0), reports.first().beacons(face = Face.Front, rotate = Rotate.R0))

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
            for (face in Face.values()) {
                for (rotate in Rotate.values()) {
                    val testedReportBeacons: Set<Point3D> = testedReport.beacons(face, rotate)
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
            }
            return null
        }
    }

    data class Point3D(val x: Int, val y: Int, val z: Int) {

        operator fun minus(other: Point3D): Point3D = Point3D(x - other.x, y - other.y, z - other.z)

        operator fun plus(other: Point3D): Point3D = Point3D(x + other.x, y + other.y, z + other.z)

        fun manhattanDistance(other: Point3D): Int = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    enum class Face { Front, Bottom, Top, Right, Left, Back }

    enum class Rotate { R0, R90, R180, R270 }

    class Report(private var beacons: Set<Point3D>) {

        fun beacons(face: Face, rotate: Rotate): Set<Point3D> {
            return beacons.map { it.facing(face).rotating(rotate) }.toSet()
        }

        private fun Point3D.facing(face: Face): Point3D {
            return when (face) {
                Face.Front -> Point3D(x = x, y = y, z = z)    // front
                Face.Bottom -> Point3D(x = x, y = -y, z = -z) // bottom
                Face.Top -> Point3D(x = x, y = -z, z = y)     // top
                Face.Right -> Point3D(x = -y, y = -z, z = x)  // right
                Face.Left -> Point3D(x = y, y = -z, z = -x)   // left
                Face.Back -> Point3D(x = -x, y = -z, z = -y)  // back
            }
        }

        private fun Point3D.rotating(rotate: Rotate): Point3D {
            return when (rotate) {
                Rotate.R0 -> Point3D(x = x, y = y, z = z)     // rotate clockwise * 0
                Rotate.R90 -> Point3D(x = -y, y = x, z = z)   // rotate clockwise * 1
                Rotate.R180 -> Point3D(x = -x, y = -y, z = z) // rotate clockwise * 2
                Rotate.R270 -> Point3D(x = y, y = -x, z = z)  // rotate clockwise * 3
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