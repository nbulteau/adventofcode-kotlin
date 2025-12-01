package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {
    private val day = Day01(2025, 1)
    
    val test = """
        L68
        L30
        R48
        L5
        R60
        L55
        L1
        L99
        R14
        L82
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(3, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(6, day.partTwo(test))
    }

    @Test
    fun `partTwo - R1000 from position 50 passes 0 ten times`() {
        val testData = "R1000"
        assertEquals(10, day.partTwo(testData))
    }
}