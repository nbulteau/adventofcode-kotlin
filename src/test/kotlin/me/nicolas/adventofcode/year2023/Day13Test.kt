package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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
    fun partOne() {
        assertEquals(405, day.partOne(data))
    }

    @Test
    fun partTwo() {
        assertEquals(400, day.partTwo(data))
    }
}