package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14Test {
    private val day = Day14(2015, 14)
    
    val test = """
        Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1120, day.partOne(test, 1000))
    }

    @Test
    fun partTwo() {
        assertEquals(689, day.partTwo(test, 1000))
    }
}