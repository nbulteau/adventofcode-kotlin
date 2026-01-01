package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {
    private val day = Day05()
    
    val test = """
        0
        3
        0
        1
        -3
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(5, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(10, day.partTwo(test))
    }
}