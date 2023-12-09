package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {

    private val day = Day03(2023, 3, "Gear Ratios")
    private val data = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..      
    """.trimIndent()

    @Test
    fun `part one training`() {
        val lines = data.split("\n")

        assertEquals(4361, day.partOne(lines))
    }

    @Test
    fun `part two training`() {
        val lines = data.split("\n")

        assertEquals(467835, day.partTwo(lines))
    }
}