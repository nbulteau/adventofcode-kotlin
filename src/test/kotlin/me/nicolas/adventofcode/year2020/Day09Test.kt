package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09Test {

    private val data = """
        35
        20
        15
        25
        47
        40
        62
        55
        65
        95
        102
        117
        150
        182
        127
        219
        299
        277
        309
        576
    """.trimIndent()

    private val day = Day09(2020, 9, "Encoding Error")

    @Test
    fun `part one`() {
        assertEquals(127, day.partOne(data, 5))
    }

    @Test
    fun `part two`() {
        assertEquals(62, day.partTwo(data, 5))
    }
}

