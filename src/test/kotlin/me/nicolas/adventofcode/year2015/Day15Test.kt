package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day15Test {
    private val day = Day15(2015, 15)
    
    val test = """
        Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
        Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(62842880, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(57600000, day.partTwo(test))
    }
}