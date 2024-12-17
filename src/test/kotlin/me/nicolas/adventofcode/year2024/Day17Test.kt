package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17Test {
    private val day = Day17(2024, 17)

    val test0 = """
        Register A: 0
        Register B: 0
        Register C: 9

        Program: 2,6
    """.trimIndent()

    val test1 = """
        Register A: 10
        Register B: 0
        Register C: 0

        Program: 5,0,5,1,5,4
    """.trimIndent()

    val test2 = """
        Register A: 2024
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
    """.trimIndent()

    val test3 = """
        Register A: 0
        Register B: 29
        Register C: 0

        Program: 1,7
    """.trimIndent()

    val test4 = """
        Register A: 0
        Register B: 2024
        Register C: 43690

        Program: 4,0
    """.trimIndent()


    val test = """
        Register A: 729
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals("", day.partOne(test0)) // would set register B to 1
        assertEquals("0,1,2", day.partOne(test1))
        assertEquals("4,2,5,6,7,7,7,7,3,1,0", day.partOne(test2))
        assertEquals("", day.partOne(test3)) // would set register B to 26
        assertEquals("", day.partOne(test4)) // would set register B to 44354

        assertEquals("4,6,3,5,6,3,5,2,1,0", day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(117440, day.partTwo(test))
    }
}