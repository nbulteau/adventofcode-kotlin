package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {
    private val day = Day08()
    
    val test = """
        b inc 5 if a > 1
        a inc 1 if b < 5
        c dec -10 if a >= 1
        c inc -20 if c == 10
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(10, day.partTwo(test))
    }
}