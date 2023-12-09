package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {
    private val day = Day03(2018, 3, "No Matter How You Slice It")

    private val data = """
            #1 @ 1,3: 4x4
            #2 @ 3,1: 4x4
            #3 @ 5,5: 2x2
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(4, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(3, day.partTwo(data))
    }
}