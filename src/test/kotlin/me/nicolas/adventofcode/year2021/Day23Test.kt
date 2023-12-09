package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day23Test {

    private val day = Day23(2023, 23, "Amphipod")


    @Test
    fun `part one training`() {
        val data = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########   
        """.trimIndent()

        assertEquals(12521, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        val data = """
        #############
        #...........#
        ###B#C#B#D###
          #D#C#B#A#
          #D#B#A#C#
          #A#D#C#A#
          #########
        """.trimIndent()

        assertEquals(44169, day.partTwo(data))
    }
}