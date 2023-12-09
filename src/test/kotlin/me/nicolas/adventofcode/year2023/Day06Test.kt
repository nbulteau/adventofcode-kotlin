package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day06Test {

    private val day = Day06(2023, 6, "Wait For It")
    private val data = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(288, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(71503, day.partTwo(data))
    }
}