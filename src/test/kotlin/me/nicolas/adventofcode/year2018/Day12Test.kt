package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day12Test {

    private val day = Day12(2018, 12, "Subterranean Sustainability")
    private val data = """
        initial state: #..#.#..##......###...###
        
        ...## => #
        ..#.. => #
        .#... => #
        .#.#. => #
        .#.## => #
        .##.. => #
        .#### => #
        #.#.# => #
        #.### => #
        ##.#. => #
        ##.## => #
        ###.. => #
        ###.# => #
        ####. => #
    """.trimIndent()

    @Test
    fun `part one training one`() {
        assertEquals(325, day.partOne(data, 20))
    }
}