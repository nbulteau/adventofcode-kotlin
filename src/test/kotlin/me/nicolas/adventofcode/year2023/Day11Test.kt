package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {

    private val day = Day11(2023, 11, "Cosmic Expansion")
    private val data = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()
    @Test
    fun `part one training`() {
        assertEquals(374, day.partOne(data))
    }
}