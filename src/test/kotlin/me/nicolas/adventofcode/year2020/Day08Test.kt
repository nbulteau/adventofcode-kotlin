package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Test {

    private val data = """
        nop +0
        acc +1
        jmp +4
        acc +3
        jmp -3
        acc -99
        acc +1
        jmp -4
        acc +6
    """.trimIndent()

    private val day = Day08(2020, 8, "Handheld Halting")

    @Test
    fun `part one`() {
        assertEquals(5, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(8, day.partTwo(data))
    }
}

