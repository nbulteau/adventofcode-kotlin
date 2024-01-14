package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day16Test {
    private val day = Day16(2018, 16, "Chronal Classification")

    @Test
    fun `part one training`() {
        val data = """
            Before: [3, 2, 1, 1]
            9 2 1 2
            After:  [3, 2, 2, 1]
        """.trimIndent()
        assertEquals(1, day.partOne(data))
    }
}