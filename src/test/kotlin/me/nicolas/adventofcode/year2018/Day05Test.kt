package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05Test {

    private val day = Day04(2018, 4, "Repose Record")
    private val data = """
        dabAcCaCBAcCcaDA
    """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(10, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(42, day.partTwo(data))
    }
}