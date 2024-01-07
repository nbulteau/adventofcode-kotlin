package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day15Test {
    private val day = Day15(2018, 15, "Beverage Bandits")

    @Test
    fun `part one training one`() {
        val data = """
            #######   
            #.G...#
            #...EG#
            #.#.#G#
            #..G#E#
            #.....#   
            #######
        """.trimIndent()
        assertEquals(27730, day.partOne(data))
    }

    @Test
    fun `part one training two`() {
        val data = """
            #######
            #G..#E#
            #E#E.E#
            #G.##.#
            #...#E#
            #...E.#
            #######
        """.trimIndent()
        assertEquals(36334, day.partOne(data))
    }

    @Test
    fun `part one training three`() {
        val data = """
            #######
            #E..EG#
            #.#G.E#
            #E.##E#
            #G..#.#
            #..E#.#
            #######
        """.trimIndent()
        assertEquals(39514, day.partOne(data))
    }

    @Test
    fun `part one training four`() {
        val data = """
            #######
            #E.G#.#
            #.#G..#
            #G.#.G#
            #G..#.#
            #...E.#
            #######
        """.trimIndent()
        assertEquals(27755, day.partOne(data))
    }

    @Test
    fun `part one training five`() {
        val data = """
            #######
            #.E...#
            #.#..G#
            #.###.#
            #E#G#G#
            #...#G#
            #######
        """.trimIndent()
        assertEquals(28944, day.partOne(data))
    }

    @Test
    fun `part one training six`() {
        val data = """
            #########
            #G......#
            #.E.#...#
            #..##..G#
            #...##..#
            #...#...#
            #.G...G.#
            #.....G.#
            #########
        """.trimIndent()
        assertEquals(18740, day.partOne(data))
    }

    @Test
    fun `part two training one`() {
        val data = """
            #######
            #.G...#
            #...EG#
            #.#.#G#
            #..G#E#
            #.....#
            #######
        """.trimIndent()
        assertEquals(4988, day.partTwo(data))
    }

    @Test
    fun `part two training two`() {
        val data = """
            ####### 
            #E..EG# 
            #.#G.E#
            #E.##E#
            #G..#.#
            #..E#.#
            #######
        """.trimIndent()
        assertEquals(31284, day.partTwo(data))
    }

    @Test
    fun `part two training three`() {
        val data = """
            #######
            #E.G#.#
            #.#G..#
            #G.#.G#
            #G..#.#
            #...E.#
            #######
        """.trimIndent()
        assertEquals(3478, day.partTwo(data))
    }

    @Test
    fun `part two training four`() {
        val data = """
            #######
            #.E...#
            #.#..G#
            #.###.#
            #E#G#G#
            #...#G#
            #######
        """.trimIndent()
        assertEquals(6474, day.partTwo(data))
    }

    @Test
    fun `part two training five`() {
        val data = """
            #########
            #G......#
            #.E.#...#
            #..##..G#
            #...##..#
            #...#...#
            #.G...G.#
            #.....G.# 
            #########
        """.trimIndent()
        assertEquals(1140, day.partTwo(data))
    }
}