package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18Test {
    private val day = Day18(2015, 18)
    
    val test = """
        .#.#.#
        ...##.
        #....#
        ..#...
        #.#..#
        ####..
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(4, day.partOne(test, steps = 4))
    }

    @Test
    fun partTwo() {
        assertEquals(17, day.partTwo(test, steps = 5))
    }
}