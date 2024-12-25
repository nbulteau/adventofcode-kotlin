package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day25Test {
    private val day = Day25(2024, 25)
    
    val test = """
        #####
        .####
        .####
        .####
        .#.#.
        .#...
        .....
        
        #####
        ##.##
        .#.##
        ...##
        ...#.
        ...#.
        .....
        
        .....
        #....
        #....
        #...#
        #.#.#
        #.###
        #####
        
        .....
        .....
        #.#..
        ###..
        ###.#
        ###.#
        #####
        
        .....
        .....
        .....
        #....
        #.#..
        #.#.#
        ##### 
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(3, day.partOne(test))
    }
}