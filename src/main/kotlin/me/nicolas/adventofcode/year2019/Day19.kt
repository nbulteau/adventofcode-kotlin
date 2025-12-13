package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 19: Tractor Beam ---
// https://adventofcode.com/2019/day/19
fun main() {
    val data = readFileDirectlyAsText("/year2019/day19/data.txt")
    val day = Day19()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day19(year: Int = 2019, day: Int = 19, title: String = "Tractor Beam") : AdventOfCodeDay(year, day, title) {
    private fun parseProgram(data: String): List<Long> {
        return data.trim().split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
    }

    private fun probe(program: List<Long>, x: Int, y: Int, cache: MutableMap<Pair<Int, Int>, Int>): Int {
        val key = Pair(x, y)
        return cache.getOrPut(key) {
            val ic = IntCodeProgram(program)
            val outputs = ic.execute(mutableListOf(x.toLong(), y.toLong()))
            if (outputs.isEmpty()) 0 else outputs[0].toInt()
        }
    }

    /**
     * PART ONE
     *
     * Count how many points are affected by the tractor beam in the 50x50 area
     * with X and Y in the range [0, 49].
     *
     * Approach:
     * - Parse the IntCode program once into a list of Long values.
     * - Use `probe(...)` to run the IntCode program for each coordinate (x,y).
     *   `probe` creates a fresh `IntCodeProgram` per call but caches results so
     *   repeated probes don't re-run the IntCode.
     * - Iterate over y=0..49 and x=0..49 and count how many probes return 1.
     *
     * Notes / edge cases:
     * - This function performs up to 2500 probes; performance is acceptable for part one.
     */
    fun partOne(data: String): Int {
        val program = parseProgram(data)
        val cache = mutableMapOf<Pair<Int, Int>, Int>()

        var count = 0
        for (y in 0 until 50) {
            for (x in 0 until 50) {
                if (probe(program, x, y, cache) == 1) count++
            }
        }
        return count
    }

    /**
     * PART TWO
     *
     * Find the coordinates of the top-left corner of the first square of size
     * 100x100 that fits entirely inside the tractor beam, and return x*10000 + y.
     *
     * Approach:
     * - We scan rows starting from y = squareSize - 1 (the first row where a full
     *   100-high square could fit with its bottom on that row).
     * - Maintain `xStart` which is the leftmost x known to be within the beam on
     *   the current row. For each row, advance `xStart` until probe(xStart, y) == 1.
     * - For that `xStart` we check the top-right corner of the candidate square:
     *   (xStart + squareSize - 1, y - (squareSize - 1)). If that corner is inside
     *   the beam as well, the entire 100x100 square fits.
     *
     * Rationale and assumptions:
     * - The tractor beam is contiguous horizontally on each row and gradually
     *   widens as y increases. Keeping a moving `xStart` exploits that property
     *   and avoids restarting x from 0 each row.
     * - Caching probe results avoids re-running expensive IntCode executions for
     *   already-checked coordinates.
     */
    fun partTwo(data: String): Int {
        val program = parseProgram(data)
        val cache = mutableMapOf<Pair<Int, Int>, Int>()

        val squareSize = 100
        var xStart = 0
        var y = squareSize - 1 // first row where a 100-high square could fit

        while (true) {
            // move xStart right until we hit the beam on this row
            while (probe(program, xStart, y, cache) == 0) {
                xStart++
            }

            // check if the top-right corner of the square is in the beam
            val topY = y - (squareSize - 1)
            val rightX = xStart + (squareSize - 1)
            if (topY >= 0 && probe(program, rightX, topY, cache) == 1) {
                // top-left coordinate is (xStart, topY)
                return xStart * 10000 + topY
            }

            y++
        }
    }
}