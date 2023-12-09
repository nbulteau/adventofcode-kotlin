package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {

    private val day = Day01(2018, 1, "Chronal Calibration")
    private val data = """
            +1
            -2
            +3
            +1      
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(3, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(2, day.partTwo(data))
    }
}