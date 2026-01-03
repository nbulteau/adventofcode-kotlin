package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
    fun `part one training`() {
        assertEquals(325, day.partOne(data, 20))
    }
}