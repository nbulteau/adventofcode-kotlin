package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day06Test {

    private val data = """
        abc

        a
        b
        c

        ab
        ac

        a
        a
        a
        a

        b
    """.trimIndent()

    private val day = Day06(2020, 6, "Custom Customs")

    @Test
    fun `part one`() {
        assertEquals(11, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(6, day.partTwo(data))
    }
}

