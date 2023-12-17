package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day20Test {
    private val day = Day20(2023, 20, "")


    @Test
    fun `part one training`() {
        val data = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
        """.trimIndent()

        assertEquals(32000000L, day.partOne(data))
    }

    @Test
    fun `part one training two`() {
        val data = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
        """.trimIndent()

        assertEquals(11687500L, day.partOne(data))
    }
}