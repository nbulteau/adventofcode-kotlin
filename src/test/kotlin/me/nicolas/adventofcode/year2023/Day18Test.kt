package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day18Test {
    private val day = Day18(2023, 18, "")
    private val data = """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(62, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(952408144115, day.partTwo(data))
    }
}