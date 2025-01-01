package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day20Test {
    private val day = Day20(2015, 20)
    
    val test = """
        
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1, day.partOne(10))
        assertEquals(2, day.partOne(30))
        assertEquals(3, day.partOne(40))
        assertEquals(4, day.partOne(70))
        assertEquals(4, day.partOne(60))
        assertEquals(6, day.partOne(120))
        assertEquals(6, day.partOne(80))
        assertEquals(8, day.partOne(150))
        assertEquals(8, day.partOne(130))
    }

    @Test
    fun partTwo() {
        assertEquals(1, day.partTwo(11))
        assertEquals(2, day.partTwo(22))
    }
}