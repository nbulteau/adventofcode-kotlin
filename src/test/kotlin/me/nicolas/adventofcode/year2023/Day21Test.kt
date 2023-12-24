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
        assertEquals(50, day.partTwo(data, 10))
        assertEquals(1594, day.partTwo(data, 50))
        assertEquals(6536, day.partTwo(data, 100))
        assertEquals(167004, day.partTwo(data, 500))
        assertEquals(668697, day.partTwo(data, 1000))
        assertEquals(16733044, day.partTwo(data, 5000))
    }
}