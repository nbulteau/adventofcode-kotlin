package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day24Test {
    private val day = Day24(2015, 24)
    
    val test = """
        1
        2
        3
        4
        5
        7
        8
        9
        10
        11
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(99, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(44, day.partTwo(test))
    }
}