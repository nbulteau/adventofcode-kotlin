package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22Test {

    private val data = """
        Player 1:
        9
        2
        6
        3
        1

        Player 2:
        5
        8
        4
        7
        10
    """.trimIndent()

    private val day = Day22(2020, 22, "Crab Combat")

    @Test
    fun `part one`() {
        assertEquals(306, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(291, day.partTwo(data))
    }
}

