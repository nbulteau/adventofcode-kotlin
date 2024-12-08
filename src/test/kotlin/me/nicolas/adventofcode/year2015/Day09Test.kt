package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {
    private val day = Day09(2015, 9, "")
    
    val test = """
        London to Dublin = 464
        London to Belfast = 518
        Dublin to Belfast = 141
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(605, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(982, day.partTwo(test))
    }
}