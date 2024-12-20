package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day20Test {
    private val day = Day20(2024, 20)
    
    val test = """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1, day.partOne(test, 64 ))
    }

    @Test
    fun partTwo() {
        assertEquals(3, day.partTwo(test, 76))
    }
}