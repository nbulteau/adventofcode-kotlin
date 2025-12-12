package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {

    private val data = """
        1721
        979
        366
        299
        675
        1456
    """.trimIndent()

    private val day = Day01()

    @Test
    fun `part one`() {
        assertEquals(514579, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(241861950, day.partTwo(data))
    }
}

