package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {

    private val data = """
        1-3 a: abcde
        1-3 b: cdefg
        2-9 c: ccccccccc
    """.trimIndent()

    private val day = Day02(2020, 2, "Password Philosophy")

    @Test
    fun `part one`() {
        assertEquals(2, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(1, day.partTwo(data))
    }
}

