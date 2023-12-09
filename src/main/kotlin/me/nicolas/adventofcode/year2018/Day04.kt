package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2018/day/4
fun main() {
    val data = readFileDirectlyAsText("/year2018/day04/data.txt")
    val day = Day04(2018, 4, "Repose Record")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day04(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    private val guardPattern: Regex     // Extract guard id
        get() = """^.+ #(\d+) .+$""".toRegex()
    private val timePattern: Regex   // Extract minute
        get() = """^\[.+:(\d\d)] .+$""".toRegex()

    fun partOne(data: String): Int {
        val sleepMinutesPerGuard = extractSleepsList(data)

        // Find the guard that has the most minutes asleep.
        // The sleepiest guard : the maxBy { it.value.size }
        val sleepiestGuard = sleepMinutesPerGuard.maxBy { entry -> entry.value.size }
        // What minute does that guard spend asleep the most?
        return sleepiestGuard.run { key * value.groupBy { it }.maxBy { entry -> entry.value.size }.key }
    }

    fun partTwo(data: String): Int {
        val sleepMinutesPerGuard = extractSleepsList(data)

        // Of all guards, which guard is most frequently asleep on the same minute?
        val guardMostFrequentlyAsleep = sleepMinutesPerGuard.flatMap { entry ->
            entry.value.map { minute ->
                entry.key to minute // Guard to Minute
            }
        }.groupBy { it }.maxBy { entry -> entry.value.size }

        return guardMostFrequentlyAsleep.run { key.first * key.second }
    }

    /**
     * If we want them in order by time, we can just do a natural sort.
     * We can assume that guards begins shift 'awake'. This means if a guard begins shift early at 23:59, we don’t have to worry about that.
     * If a guard falls asleep or wakes up, we just need the minute that it happened.
     * If a guard begins shift, we just need the guard number.
     */
    private fun extractSleepsList(data: String): Map<Int, List<Int>> {
        val sleeps = mutableMapOf<Int, List<Int>>()
        var guard = 0
        var sleepStart = 0

        data.lines().sorted().forEach { row ->
            when {
                row.contains("Guard") -> guard = guardPattern.find(row)!!.destructured.component1().toInt()
                row.contains("asleep") -> sleepStart = timePattern.find(row)!!.destructured.component1().toInt()
                else -> {
                    val sleepMinutes =
                        (sleepStart until timePattern.find(row)!!.destructured.component1().toInt()).toList()
                    sleeps.merge(guard, sleepMinutes) { list1, list2 -> list1 + list2 }
                }
            }
        }
        return sleeps
    }
}

