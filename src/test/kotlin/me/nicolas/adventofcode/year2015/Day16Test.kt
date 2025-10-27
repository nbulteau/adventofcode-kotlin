package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16Test {
    private val day16 = Day16(2015, 16)

    val testDataPartOne = """
        Sue 1: goldfish: 6, trees: 9, akitas: 0
        Sue 2: perfumes: 1, cars: 2, goldfish: 5
        Sue 3: trees: 4, cars: 2, children: 3
    """.trimIndent()

    private val testDataPartTwo = """
        Sue 1: goldfish: 6, trees: 9, akitas: 0
        Sue 2: perfumes: 5, cars: 2, goldfish: 5
        Sue 3: trees: 4, cars: 2, children: 3
    """.trimIndent()

    @Test
    fun testPartOne() {
        val result = day16.partOne(testDataPartOne)
        assertEquals(2, result)
    }

    @Test
    fun testPartTwo() {
        val result = day16.partTwo(testDataPartTwo)
        assertEquals(3, result)
    }
}