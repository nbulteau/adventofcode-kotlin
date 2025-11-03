package me.nicolas.adventofcode.year2019

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@ExperimentalTime
class Day10Test {

    @Test
    fun testpartOneSmallExample() {
        val input = """
            .#..#
            .....
            #####
            ....#
            ...##
        """.trimIndent()

        println("\n=== Small Example Test ===")
        val day10 = Day10(2019, 10)
        val result = day10.partOne(input)
        println("Result: $result")
        assertEquals(8, result, "Best location should detect 8 asteroids")
    }

    @Test
    fun testpartOneLargerExample1() {
        val input = """
            ......#.#.
            #..#.#....
            ..#######.
            .#.#.###..
            .#..#.....
            ..#....#.#
            #..#....#.
            .##.#..###
            ##...#..#.
            .#....####
        """.trimIndent()

        val day10 = Day10(2019, 10)
        val result = day10.partOne(input)
        assertEquals(33, result, "Best location (5,8) should detect 33 asteroids")
    }

    @Test
    fun testpartOneLargerExample2() {
        val input = """
            #.#...#.#.
            .###....#.
            .#....#...
            ##.#.#.#.#
            ....#.#.#.
            .##..###.#
            ..#...##..
            ..##....##
            ......#...
            .####.###.
        """.trimIndent()

        val day10 = Day10(2019, 10)
        val result = day10.partOne(input)
        assertEquals(35, result, "Best location (1,2) should detect 35 asteroids")
    }

    @Test
    fun testpartOneLargerExample3() {
        val input = """
            .#..#..###
            ####.###.#
            ....###.#.
            ..###.##.#
            ##.##.#.#.
            ....###..#
            ..#.#..#.#
            #..#.#.###
            .##...##.#
            .....#.#..
        """.trimIndent()

        val day10 = Day10(2019, 10)
        val result = day10.partOne(input)
        assertEquals(41, result, "Best location (6,3) should detect 41 asteroids")
    }

    @Test
    fun testpartOneLargestExample() {
        val input = """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
        """.trimIndent()

        println("\n=== Largest Example Test (Expected 210) ===")
        val day10 = Day10(2019, 10)
        val result = day10.partOne(input)
        println("Result: $result")
        assertEquals(210, result, "Best location (11,13) should detect 210 asteroids")
    }

    @Test
    fun testpartTwoLargestExample() {
        val input = """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
        """.trimIndent()

        println("\n=== Part Two Largest Example Test ===")
        val day10 = Day10(2019, 10)
        val result = day10.partTwo(input)
        println("Result: $result")
        // The 200th asteroid to be vaporized is at 8,2 -> 8*100 + 2 = 802
        assertEquals(802, result, "200th vaporized asteroid should be at (8,2) -> 802")
    }
}
