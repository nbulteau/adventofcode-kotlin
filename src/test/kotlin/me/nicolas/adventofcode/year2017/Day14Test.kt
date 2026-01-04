package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14Test {
    private val day = Day14()
    
    val test = """
        flqrgnkx
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(8108, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(1242, day.partTwo(test))
    }
}