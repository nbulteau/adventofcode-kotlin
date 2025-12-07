package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {
    private val day = Day13(2021, 13, "Transparent Origami")

    val test = """
        6,10
        0,14
        9,10
        9,0
        8,10
        0,10
        6,0
        6,12
        4,1
        6,1
        0,13
        10,14
        9,10
        0,10
        0,0
        8,4
        11,4
        11,14
        2,15
        3,4
        10,4
        2,14
        15,4
        1,4

        fold along y=7
        fold along x=5
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(17, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(16, day.partTwo(test))
    }
}
