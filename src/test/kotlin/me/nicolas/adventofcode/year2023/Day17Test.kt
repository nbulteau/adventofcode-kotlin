package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17Test {

    private val day = Day17(2023, 17, "Clumsy Crucible")

    @Test
    fun `part one training`() {
        val data = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
        """.trimIndent()
        assertEquals(102, day.partOne(data))
    }

    @Test
    fun `part two training one`() {
        val data = """
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991
        """.trimIndent()

        assertEquals(71, day.partTwo(data))
    }

    @Test
    fun `part two training two`() {
        val data = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
        """.trimIndent()

        assertEquals(94, day.partTwo(data))
    }
}