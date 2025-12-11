package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {
    private val day = Day11(2025, 11)

    val test = """
        aaa: you hhh
        you: bbb ccc
        bbb: ddd eee
        ccc: ddd eee fff
        ddd: ggg
        eee: out
        fff: out
        ggg: out
        hhh: ccc fff iii
        iii: out
    """.trimIndent()

    val testPartTwo = """
        svr: aaa bbb
        aaa: fft
        fft: ccc
        bbb: tty
        tty: ccc
        ccc: ddd eee
        ddd: hub
        hub: fff
        eee: dac
        dac: fff
        fff: ggg hhh
        ggg: out
        hhh: out
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(5L, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(2L, day.partTwo(testPartTwo))
    }

    @Test
    fun partTwoOptimised() {
        assertEquals(2L, day.partTwoOptimised(testPartTwo))
    }
}