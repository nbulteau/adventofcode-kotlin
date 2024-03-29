package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04Test {

    private val day = Day04(2018, 4, "Repose Record")
    private val data = """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:50] wakes up
            [1518-11-03 00:05] Guard #10 begins shift
            [1518-11-03 00:24] falls asleep
            [1518-11-03 00:29] wakes up
            [1518-11-04 00:02] Guard #99 begins shift
            [1518-11-04 00:36] falls asleep
            [1518-11-04 00:46] wakes up
            [1518-11-05 00:03] Guard #99 begins shift
            [1518-11-05 00:45] falls asleep
            [1518-11-05 00:55] wakes up
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(240, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(4455, day.partTwo(data))
    }
}