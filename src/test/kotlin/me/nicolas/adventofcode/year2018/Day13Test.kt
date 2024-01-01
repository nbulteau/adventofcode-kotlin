package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Test {
    private val day13 = Day13(2018, 13, "Mine Cart Madness")

    @Test
    fun partOne() {
        val data = """
        /->-\        
        |   |  /----\
        | /-+--+-\  |
        | | |  | v  |
        \-+-/  \-+--/
          \------/   
    """.trimIndent()
        assertEquals("7,3", day13.partOne(data))
    }

    @Test
    fun partTwo() {
        val data = """
        />-<\  
        |   |  
        | /<+-\
        | | | v
        \>+</ |
          |   ^
          \<->/
    """.trimIndent()
        assertEquals("6,4", day13.partTwo(data))
    }
}