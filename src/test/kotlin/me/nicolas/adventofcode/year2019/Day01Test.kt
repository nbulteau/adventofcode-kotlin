package me.nicolas.adventofcode.year2019

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day01Test {
    private val data = """
            12
            14
            1969
            100756
        """.trimIndent()
    private val day = Day01(2019, 1, "The Tyranny of the Rocket Equation", data)


    @Test
    fun `part one training`() {
        assertEquals(34241, day.partOne())
    }

    @Test
    fun `part two training`() {
        assertEquals(51316, day.partTwo())
    }
}