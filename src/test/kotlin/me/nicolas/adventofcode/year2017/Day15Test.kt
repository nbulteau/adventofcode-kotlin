package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day15Test {
    private val day = Day15()
    
    val test = """
        Generator A starts with 65
        Generator B starts with 8921
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(588, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(309, day.partTwo(test))
    }
}