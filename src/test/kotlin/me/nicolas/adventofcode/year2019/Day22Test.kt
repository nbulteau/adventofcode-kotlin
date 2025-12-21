package me.nicolas.adventofcode.year2019

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day22Test {
    private val day = Day22(2019, 22)

    @Test
    fun example_dealWithIncrement7_then_newStack_twice() {
        val instructions = """
            deal with increment 7
            deal into new stack
            deal into new stack
        """.trimIndent()
        val result = day.shuffle(instructions, 10)
        val expected = listOf(0, 3, 6, 9, 2, 5, 8, 1, 4, 7)
        assertEquals(expected, result)
    }

    @Test
    fun example_cut6_dealWithIncrement7_newStack() {
        val instructions = """
            cut 6
            deal with increment 7
            deal into new stack
        """.trimIndent()
        val result = day.shuffle(instructions, 10)
        val expected = listOf(3, 0, 7, 4, 1, 8, 5, 2, 9, 6)
        assertEquals(expected, result)
    }

    @Test
    fun example_twoIncrements_and_cutMinus2() {
        val instructions = """
            deal with increment 7
            deal with increment 9
            cut -2
        """.trimIndent()
        val result = day.shuffle(instructions, 10)
        val expected = listOf(6, 3, 0, 7, 4, 1, 8, 5, 2, 9)
        assertEquals(expected, result)
    }

    @Test
    fun example_longSequence() {
        val instructions = """
            deal into new stack
            cut -2
            deal with increment 7
            cut 8
            cut -4
            deal with increment 7
            cut 3
            deal with increment 9
            deal with increment 3
            cut -1
        """.trimIndent()
        val result = day.shuffle(instructions, 10)
        val expected = listOf(9, 2, 5, 8, 1, 4, 7, 0, 3, 6)
        assertEquals(expected, result)
    }
}