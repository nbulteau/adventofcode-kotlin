package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Test {

    private val day = Day14(2023, 14, "Parabolic Reflector Dish")

    private val data = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(136, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(64, day.partTwo(data))
    }
}