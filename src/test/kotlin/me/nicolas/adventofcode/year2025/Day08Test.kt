package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {

    private val test = """
        162,817,812
        57,618,57
        906,360,560
        592,479,940
        352,342,300
        466,668,158
        542,29,236
        431,825,988
        739,650,466
        52,470,668
        216,146,977
        819,987,18
        117,168,530
        805,96,715
        346,949,466
        970,615,88
        941,993,340
        862,61,35
        984,92,344
        425,690,689
    """.trimIndent()

    private val day = Day08(2025, 8)

    @Test
    fun testPartOne() {
        val result = day.partOne(test, numberOfConnections = 10)
        assertEquals(40, result)
    }

    @Test
    fun testPartTwo() {
        val result = day.partTwo(test)
        assertEquals(25272, result)
    }
}

