package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {
    private val day = Day19(2024, 19)
    
    val test = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(6, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(16, day.partTwo(test))
    }
}