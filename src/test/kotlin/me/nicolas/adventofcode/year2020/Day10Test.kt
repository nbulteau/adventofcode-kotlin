package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {

    private val data = """
        28
        33
        18
        42
        31
        14
        46
        20
        48
        47
        24
        23
        49
        45
        19
        38
        39
        11
        1
        32
        25
        35
        8
        17
        7
        9
        4
        2
        34
        10
        3
    """.trimIndent()

    private val day = Day10(2020, 10, "Adapter Array")

    @Test
    fun `part one`() {
        assertEquals(220, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(19208, day.partTwo(data))
    }
}

