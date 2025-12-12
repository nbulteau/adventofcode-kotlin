package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {

    private val data = """
        #.##.##.##
        #######.##
        #.#.#..#..
        ####.##.##
        #.##.##.##
        #.#####.##
        ..#.#.....
        ##########
        #.######.#
        #.#####.##
    """.trimIndent()

    private val day = Day11(2020, 11, "Seating System")

    @Test
    fun `part one`() {
        assertEquals(37, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(26, day.partTwo(data))
    }
}