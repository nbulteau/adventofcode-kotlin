package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05Test {

    private val data = """
        FBFBBFFRLR
        BFFFBBFRRR
        FFFBBBFRRR
        BBFFBBFRLL
    """.trimIndent()

    private val day = Day05(2020, 5, "Binary Boarding")

    @Test
    fun `part one`() {
        assertEquals(820, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(120, day.partTwo(data))
    }
}

