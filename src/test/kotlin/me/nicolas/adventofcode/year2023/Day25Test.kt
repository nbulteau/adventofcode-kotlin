package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day25Test {
    private val day = Day25(2023, 25, "")
    private val data = """
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(42, day.partOne(data))
    }
}