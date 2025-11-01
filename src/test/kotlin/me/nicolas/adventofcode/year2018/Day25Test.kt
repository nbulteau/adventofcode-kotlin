package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day25Test {

    private val day = Day25(2018, 25, "Four-Dimensional Adventure")

    @Test
    fun `example 1 - two constellations`() {
        val data = """
            0,0,0,0
            3,0,0,0
            0,3,0,0
            0,0,3,0
            0,0,0,3
            0,0,0,6
            9,0,0,0
            12,0,0,0
        """.trimIndent()

        assertEquals(2, day.partOne(data))
    }

    @Test
    fun `example 2 - four constellations`() {
        val data = """
            -1,2,2,0
            0,0,2,-2
            0,0,0,-2
            -1,2,0,0
            -2,-2,-2,2
            3,0,2,-1
            -1,3,2,2
            -1,0,-1,0
            0,2,1,-2
            3,0,0,0
        """.trimIndent()

        assertEquals(4, day.partOne(data))
    }

    @Test
    fun `example 3 - three constellations`() {
        val data = """
            1,-1,0,1
            2,0,-1,0
            3,2,-1,0
            0,0,3,1
            0,0,-1,-1
            2,3,-2,0
            -2,2,0,0
            2,-2,0,-1
            1,-1,0,-1
            3,2,0,2
        """.trimIndent()

        assertEquals(3, day.partOne(data))
    }

    @Test
    fun `example 4 - eight constellations`() {
        val data = """
            1,-1,-1,-2
            -2,-2,0,1
            0,2,1,3
            -2,3,-2,1
            0,2,3,-2
            -1,-1,1,-2
            0,-2,-1,0
            -2,2,3,-1
            1,2,2,0
            -1,-2,0,-2
        """.trimIndent()

        assertEquals(8, day.partOne(data))
    }
}

