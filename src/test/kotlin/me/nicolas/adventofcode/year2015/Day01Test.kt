package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {

    private val day = Day01(2015, 1, "Not Quite Lisp")

    @Test
    fun `part one training`() {
        val data = """
            ))(((((
        """.trimIndent()
        assertEquals(3, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        val data = """
            ()())
        """.trimIndent()
        assertEquals(5, day.partTwo(data))
    }
}