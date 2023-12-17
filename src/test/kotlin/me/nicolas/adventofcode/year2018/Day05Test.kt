package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05Test {

    private val day = Day05(2018, 5, "Alchemical Reduction")
    private val data = """
        dabAcCaCBAcCcaDA
    """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(10, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(4, day.partTwo(data))
    }
}