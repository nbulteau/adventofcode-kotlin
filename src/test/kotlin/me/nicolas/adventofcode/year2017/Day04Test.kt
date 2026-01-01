package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day04Test {
    private val day = Day04()
    
    val test = """
        aa bb cc dd ee
        aa bb cc dd aa
        aa bb cc dd aaa
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(2, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(0, day.partTwo(test))
    }
}