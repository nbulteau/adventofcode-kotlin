package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10Test {
    private val day = Day10(2025, 10)
    
    val test = """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(7, day.partOne(test))
    }

    @Test
    fun partTwoWithZ3() {
        assertEquals(33, day.partTwoWithZ3(test))
    }
}