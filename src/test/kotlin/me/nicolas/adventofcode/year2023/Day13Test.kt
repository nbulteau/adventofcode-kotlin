package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Test {
    private val day = Day13(2023, 13, "Point of Incidence")

    private val data = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(405, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(400, day.partTwo(data))
    }
}