package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day21Test {
    private val day = Day21(2024, 21)
    
    val test = """
        029A
        980A
        179A
        456A
        379A
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(126384, day.partOne(test))
    }
}