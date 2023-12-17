package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day06Test {

    private val day = Day06(2018, 6, "Chronal Coordinates")
    private val data = """
        1, 1
        1, 6
        8, 3
        3, 4
        5, 5
        8, 9
    """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(17, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(16, day.partTwo(32, data))
    }
}