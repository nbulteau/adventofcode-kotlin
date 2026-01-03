package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22Test {
    private val day = Day22(2023, 22, "Sand Slabs")
    private val data = """
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(5, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(7, day.partTwo(data))
    }
}