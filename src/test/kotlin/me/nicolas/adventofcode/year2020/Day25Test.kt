package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day25Test {

    private val data = "17807724\n5764801"

    private val day = Day25(2020, 25, "Combo Breaker")

    @Test
    fun `part one`() {
        assertEquals(14897079, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals("Merry Christmas!", day.partTwo(data))
    }
}

