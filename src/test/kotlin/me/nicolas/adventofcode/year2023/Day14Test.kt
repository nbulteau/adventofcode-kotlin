package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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
    fun partOne() {
        assertEquals(136, day.partOne(data))
    }

    @Test
    fun partTwo() {
        assertEquals(64, day.partTwo(data))
    }
}