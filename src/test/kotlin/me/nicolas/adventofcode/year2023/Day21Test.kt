package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21Test {
    private val day = Day21(2023, 21, "")
    private val data = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(16, day.partOne(data, 6))
    }

    @Test
    fun `part two training`() {
        assertEquals(16, day.partTwo(data, 6))
        assertEquals(50, day.partTwo(data, 10))
    }
}