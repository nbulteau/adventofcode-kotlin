package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day23Test {
    private val day = Day23(2021, 23, "Amphipod")

    val test = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########
    """.trimIndent()

    val testPartTwo = """
        #############
        #...........#
        ###B#C#B#D###
          #D#C#B#A#
          #D#B#A#C#
          #A#D#C#A#
          #########
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(12521, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(44169, day.partTwo(testPartTwo))
    }
}
