package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day24Test {
    private val day = Day24(2023, 24, "Never Tell Me The Odds")

    val data = """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(2, day.partOne(data, Pair(7, 27)))
    }


    @Test
    fun `part two training`() {
        assertEquals(47, day.partTwo(data))
    }
}