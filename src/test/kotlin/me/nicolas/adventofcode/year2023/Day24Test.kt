package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day24Test {
    private val day = Day24(2023, 24, "")
    private val data = """
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(42, day.partOne(data))
    }


    @Test
    fun `part two training`() {
        assertEquals(42, day.partTwo(data))
    }
}