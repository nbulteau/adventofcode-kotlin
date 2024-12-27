package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 14: Reindeer Olympics ---
// https://adventofcode.com/2015/day/14
fun main() {
    val data = readFileDirectlyAsText("/year2015/day14/data.txt")
    val day = Day14(2015, 14)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}


class Day14(year: Int, day: Int, title: String = "Reindeer Olympics") : AdventOfCodeDay(year, day, title) {
    private data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int)

    fun partOne(data: String, raceTime: Int = 2503): Int {
        val reindeers = parseReindeers(data)
        return reindeers.maxOf { reindeer -> calculateDistance(reindeer, raceTime) }
    }

    fun partTwo(data: String, raceTime: Int = 2503): Int {
        val reindeers = parseReindeers(data)
        val scores = mutableMapOf<String, Int>().withDefault { 0 }
        for (time in 1..raceTime) {
            val distances = reindeers.associateWith { calculateDistance(it, time) }
            val maxDistance = distances.values.maxOrNull()
            distances.filterValues { distance -> distance == maxDistance }
                .keys.forEach { reindeer ->
                    scores[reindeer.name] = scores.getValue(reindeer.name) + 1
                }
        }
        return scores.values.maxOrNull() ?: 0
    }

    private val regex =
        """(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()

    private fun parseReindeers(data: String): List<Reindeer> {
        return data.lines().mapNotNull { line ->
            regex.matchEntire(line)?.destructured?.let { (name, speed, flyTime, restTime) ->
                Reindeer(name, speed.toInt(), flyTime.toInt(), restTime.toInt())
            }
        }
    }

    private fun calculateDistance(reindeer: Reindeer, totalTime: Int): Int {
        val cycleTime = reindeer.flyTime + reindeer.restTime
        val fullCycles = totalTime / cycleTime
        val remainingTime = totalTime % cycleTime
        val flyTime = fullCycles * reindeer.flyTime + minOf(remainingTime, reindeer.flyTime)

        return flyTime * reindeer.speed
    }
}