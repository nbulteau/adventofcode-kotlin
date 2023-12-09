package me.nicolas.adventofcode.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day25Test {

    private val day = Day25(2022, 25, "Full of Hot Air")

    private val data = """
            1=-0-2
            12111
            2=0=
            21
            2=01
            111
            20012
            112
            1=-1=
            1-12
            12
            1=
            122  
        """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals("2=-1=0", day.partOne(data))
    }
}