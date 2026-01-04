package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {
    private val day = Day13()
    
    val test = """
        0: 3
        1: 2
        4: 4
        6: 4
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(24, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(10, day.partTwo(test))
    }
}