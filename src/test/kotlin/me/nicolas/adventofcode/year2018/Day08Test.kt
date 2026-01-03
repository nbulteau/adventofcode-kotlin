package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Test {

    private val data = """
        2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2
    """.trimIndent()
    private val day = Day08(2018, 8, "Memory Maneuver")

    @Test
    fun `part one training`() {
        assertEquals(138, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(66, day.partTwo(data))
    }
}