package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {
    private val day = Day11(2015, 11)
    
    val test1 = """
        abcdefgh
    """.trimIndent()

    val test2 = """
        ghijklmn
    """.trimIndent()
    @Test
    fun partOne() {
        assertEquals("abcdffaa", day.partOne(test1))
        assertEquals("ghjaabcc", day.partOne(test2))
    }
}