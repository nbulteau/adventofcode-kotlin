package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {
    private val day = Day11(2015, 11)
    
    val test = """
        
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(0, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(0, day.partTwo(test))
    }
}