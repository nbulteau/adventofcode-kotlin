package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10Test {
    private val day = Day10(2024, 10)
    
    val test = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent()

    val test2 = """
        8888808
        8843218
        8858828
        8865438
        1172241
        1187651
        1191111
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(36, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(81, day.partTwo(test))
    }
}