package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {

    private val day = Day01(2023, 1,"Trebuchet?!")

    @Test
    fun `part one training`() {
        val data = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet           
        """.trimIndent()
        val lines = data.split("\n")

        assertEquals( 142, day.partOne(lines))
    }

    @Test
    fun `part two training`() {
        val data = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen      
        """.trimIndent()
        val lines = data.split("\n")

        assertEquals( 281, day.partTwo(lines))
    }
}