package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {
    private val day = Day08(2015, 8, "")
    
    val test = """
        ""
        "abc"
        "aaa\"aaa"
        "\x27"
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(12, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(19, day.partTwo(test))
    }
}