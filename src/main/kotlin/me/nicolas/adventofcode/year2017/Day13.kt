package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 13: Packet Scanners ---
// https://adventofcode.com/2017/day/13
fun main() {
    val data = readFileDirectlyAsText("/year2017/day13/data.txt")
    val day = Day13()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day13(year: Int = 2017, day: Int = 13, title: String = "Packet Scanners") : AdventOfCodeDay(year, day, title) {
    private fun parseLayers(data: String): Map<Int, Int> {
        return data.lines()
            .mapNotNull { line -> line.trim().takeIf { line -> line.isNotEmpty() } }.associate { line ->
                val parts = line.split(":").map { p -> p.trim() }
                parts[0].toInt() to parts[1].toInt()
            }
    }

    private fun scannerPeriod(range: Int): Int = 2 * (range - 1)

    private fun isCaught(range: Int, time: Long): Boolean {
        if (range <= 1) return true // scanner always at top for range 1
        val period = scannerPeriod(range)
        return (time % period.toLong()) == 0L
    }

    /**
     * partOne calculates the severity of the trip through the firewall.
     * It sums the product of depth and range for each layer where the packet is caught.
     * A packet is caught if it arrives at a layer when the scanner is at the top (position 0).
     */
    fun partOne(data: String): Int {
        val layers = parseLayers(data)
        var severity = 0
        for ((depth, range) in layers) {
            if (isCaught(range, depth.toLong())) {
                severity += depth * range
            }
        }
        return severity
    }

    /**
     * partTwo finds the minimum delay needed to pass through the firewall without being caught.
     * It increments the delay until it finds a time when the packet can traverse all layers
     * without being caught by any scanner.
     *
     */
    fun partTwo(data: String): Int {
        val layers = parseLayers(data)
        var delay = 0
        while (true) {
            var caught = false
            for ((depth, range) in layers) {
                if (range == 1) { // always caught at this layer for any arrival time
                    caught = true
                    break // no need to check further => go to while loop
                }
                val period = scannerPeriod(range)
                if (((depth + delay) % period) == 0) {
                    caught = true
                    break // no need to check further => go to while loop
                }
            }
            if (!caught) return delay
            delay++
        }
    }
}