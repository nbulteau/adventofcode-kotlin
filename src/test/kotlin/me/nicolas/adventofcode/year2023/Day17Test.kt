package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day17Test {

    private val day = Day17(2023, 17, "")

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