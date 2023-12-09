package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09Test {

    private val day = Day09(2023, 9, "Mirage Maintenance")

    @Test
    fun `part one training`() {
        val data = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent()

        assertEquals(114, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        val data = """
            10 13 16 21 30 45
        """.trimIndent()

        assertEquals(5, day.partTwo(data))
    }
}