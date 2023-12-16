package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day16Test {

    private val day = Day16(2023, 16, "The Floor Will Be Lava")

    private val data = """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """.trimIndent()

    @Test
    fun `part one training`() {
        assertEquals(46, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(51, day.partTwo(data))
    }
}