package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
    private val day = Day12()

    val test = """
        0 <-> 2
        1 <-> 1
        2 <-> 0, 3, 4
        3 <-> 2, 4
        4 <-> 2, 3, 6
        5 <-> 6
        6 <-> 4, 5
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(6, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(2, day.partTwo(test))
    }
}