package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Test {

    private val data = """
        mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
        mem[8] = 11
        mem[7] = 101
        mem[8] = 0
    """.trimIndent()

    private val dataPartTwo = """
        mask = 000000000000000000000000000000X1001X
        mem[42] = 100
        mask = 00000000000000000000000000000000X0XX
        mem[26] = 1
    """.trimIndent()

    private val day = Day14(2020, 14, "Docking Data")

    @Test
    fun `part one`() {
        assertEquals(165L, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(208L, day.partTwo(dataPartTwo))
    }
}
