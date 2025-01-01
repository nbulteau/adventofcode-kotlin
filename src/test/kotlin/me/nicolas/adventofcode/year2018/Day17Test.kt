package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17Test {
    private val day = Day17(2018, 17)

    private val data = """
            x=495, y=2..7
            y=7, x=495..501
            x=501, y=3..7
            x=498, y=2..4
            x=506, y=1..2
            x=498, y=10..13
            x=504, y=10..13
            y=13, x=498..504
        """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(57, day.partOne(data))
    }

    @Test
    fun partTwo() {
        assertEquals(29, day.partTwo(data))
    }
}