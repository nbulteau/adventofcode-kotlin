package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
    private val day = Day12(2024, 12)
    
    val test1 = """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent()

    val test2 = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
    """.trimIndent()

    val test3 = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent()

    val test4 = """
        EEEEE
        EXXXX
        EEEEE
        EXXXX
        EEEEE
    """.trimIndent()

    val test5 = """
        AAAAAA
        AAABBA
        AAABBA
        ABBAAA
        ABBAAA
        AAAAAA
    """.trimIndent()

    @Test
    fun partOneOne() {
        assertEquals(140, day.partOne(test1))
    }

    @Test
    fun partOneTwo() {
        assertEquals(772, day.partOne(test2))
    }

    @Test
    fun partOneThree() {
        assertEquals(1930, day.partOne(test3))
    }

    @Test
    fun partTwoOne() {
        assertEquals(80, day.partTwo(test1))
    }

    @Test
    fun partTwoTwo() {
        assertEquals(436, day.partTwo(test2))
    }

    @Test
    fun parTwoThree() {
        assertEquals(1206, day.partTwo(test3))
    }

    @Test
    fun parTwoFour() {
        assertEquals(236, day.partTwo(test4))
    }

    @Test
    fun parTwoFive() {
        assertEquals(368, day.partTwo(test5))
    }
}