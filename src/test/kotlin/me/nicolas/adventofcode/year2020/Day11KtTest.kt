package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day11KtTest {

    @Test
    fun lookAroundInDirection3() {
        val layout = """
            .......#.
            ...#.....
            .#.......
            .........
            ..#L....#
            ....#....
            .........
            #........
            ...#.....
        """.trimIndent().split("\n")

        var nbSeats = layout.lookInDirection(3, 4, Day11.Direction.EAST)
        assertEquals(1, nbSeats)

        nbSeats = layout.lookInDirection(3, 4)
        assertEquals(8, nbSeats)
    }

    @Test
    fun lookAroundInDirection2() {
        val layout = """
            .............
            .L.L.#.#.#.#.
            .............
        """.trimIndent().split("\n")

        var nbSeats = layout.lookInDirection(1, 1, Day11.Direction.EAST)
        assertEquals(0, nbSeats)

        nbSeats = layout.lookInDirection(1, 1)
        assertEquals(0, nbSeats)
    }

    @Test
    fun lookAroundInDirection1() {
        val layout = """
            .##.##.
            #.#.#.#
            ##...##
            ...L...
            ##...##
            #.#.#.#
            .##.##.
        """.trimIndent().split("\n")

        var nbSeats = layout.lookInDirection(3, 3, Day11.Direction.EAST)
        assertEquals(0, nbSeats)
        nbSeats = layout.lookInDirection(3, 3, Day11.Direction.NORTH)
        assertEquals(0, nbSeats)
        nbSeats = layout.lookInDirection(3, 3, Day11.Direction.WEST)
        assertEquals(0, nbSeats)
        nbSeats = layout.lookInDirection(3, 3, Day11.Direction.SOUTH)
        assertEquals(0, nbSeats)
        nbSeats = layout.lookInDirection(3, 3, Day11.Direction.SOUTH_EAST)
        assertEquals(0, nbSeats)
        nbSeats = layout.lookInDirection(3, 3, Day11.Direction.SOUTH_WEST)
        assertEquals(0, nbSeats)
        nbSeats = layout.lookInDirection(3, 3, Day11.Direction.NORTH_EAST)
        assertEquals(0, nbSeats)
        nbSeats = layout.lookInDirection(3, 3, Day11.Direction.NORTH_WEST)
        assertEquals(0, nbSeats)
        nbSeats = layout.lookInDirection(3, 3)
        assertEquals(0, nbSeats)
    }

    @Test
    fun processLayoutPartTwo() {
        val layout = """
            L.LL.LL.LL
            LLLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLLL
            L.LLLLLL.L
            L.LLLLL.LL
        """.trimIndent().split("\n")

        val result1 = layout.processLayoutPartTwo()
        val expected1 = """
            #.##.##.##
            #######.##
            #.#.#..#..
            ####.##.##
            #.##.##.##
            #.#####.##
            ..#.#.....
            ##########
            #.######.#
            #.#####.##
        """.trimIndent().split("\n")
        assertEquals(expected1, result1)

        val result2 = result1.processLayoutPartTwo()
        val expected2 = """
            #.LL.LL.L#
            #LLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLL#
            #.LLLLLL.L
            #.LLLLL.L#
        """.trimIndent().split("\n")
        assertEquals(expected2, result2)

        val result3 = result2.processLayoutPartTwo()
        val expected3 = """
            #.L#.##.L#
            #L#####.LL
            L.#.#..#..
            ##L#.##.##
            #.##.#L.##
            #.#####.#L
            ..#.#.....
            LLL####LL#
            #.L#####.L
            #.L####.L#
        """.trimIndent().split("\n")

        assertEquals(expected3, result3)
    }
}