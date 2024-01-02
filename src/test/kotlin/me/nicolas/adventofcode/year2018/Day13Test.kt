package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Test {
    private val day = Day13(2018, 13, "Mine Cart Madness")

    @Test
    fun `part one training`() {
        val data = """
        /->-\        
        |   |  /----\
        | /-+--+-\  |
        | | |  | v  |
        \-+-/  \-+--/
          \------/   
    """.trimIndent()
        assertEquals("7,3", day.partOne(data))
    }

    @Test
    fun `part two training`() {
        val data = """
        />-<\  
        |   |  
        | /<+-\
        | | | v
        \>+</ |
          |   ^
          \<->/
    """.trimIndent()
        assertEquals("6,4", day.partTwo(data))
    }
}