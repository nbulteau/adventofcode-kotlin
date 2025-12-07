package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {
    private val day = Day02(2021, 2)

    val test = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(150, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(900, day.partTwo(test))
    }
}
