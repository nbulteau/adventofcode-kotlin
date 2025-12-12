package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day20Test {

    private val data = """
        Tile 2311:
        ..##.#..#.
        ##..#.....
        #...##..#.
        ####.#...#
        ##.##.###.
        ##...#.###
        .#.#.#..##
        ..#....#..
        ###...#.#.
        ..###..###

        Tile 1951:
        #.##...##.
        #.####...#
        .....#..##
        #...######
        .##.#....#
        .###.#####
        ###.##.##.
        .###....#.
        ..#.#..#.#
        #...##.#..

        Tile 1171:
        ####...##.
        #..##.#..#
        ##.#..#.#.
        .###.####.
        ..###.####
        .##....##.
        .#...####.
        #.##.####.
        ####..#...
        .....##...

        Tile 1427:
        ###.##.#..
        .#..#.##..
        .#.##.#..#
        #.#.#.##.#
        ....#...##
        ...##..##.
        ...#.#####
        .#.####.#.
        ..#..###.#
        ..##.#..#.

        Tile 1489:
        ##.#.#....
        ..##...#..
        .##..##...
        ..#...#...
        #####...#.
        #..#.#.#.#
        ...#.#.#..
        ##.#...##.
        ..##.##.##
        ###.##.#..

        Tile 2473:
        #....####.
        #..#.##...
        #.##..#...
        ######.#.#
        .#...#.#.#
        .#########
        .###.#..#.
        ########.#
        ##...##.#.
        ..###.#.#.

        Tile 2971:
        ..#.#....#
        #...###...
        #.#.###...
        ##.##..#..
        .#####..##
        .#..####.#
        #..#.#..#.
        ..####.###
        ..#.#.###.
        ...#.#.#.#

        Tile 2729:
        ...#.#.#.#
        ####.#....
        ..#.#.....
        ....#..#.#
        .##..##.#.
        .#.####...
        ####.#.#..
        ##.####...
        ##..#.##..
        #.##...##.

        Tile 3079:
        #.#.#####.
        .#..######
        ..#.......
        ######....
        ####.#..#.
        .#...#.##.
        #.#####.##
        ..#.###...
        ..#.......
        ..#.###...
    """.trimIndent()

    private val day = Day2O()

    @Test
    fun `part one`() {
        Assertions.assertEquals(20899048083289, day.partOne(data))
    }

    @Test
    fun `part two`() {
        Assertions.assertEquals(273, day.partTwo(data))
    }

    @Test
    fun doRevealSeaMonsters() {

        val seaMonster = """
                              # 
            #    ##    ##    ###
             #  #  #  #  #  #   
        """.trimIndent()
            .split("\n")
            .flatMapIndexed { row, str ->
                str.mapIndexedNotNull { column, c -> if (c == '#') Pair(row, column) else null }
            }

        var image = Array(50) { CharArray(50) }

        image = addSeaMonster(image, 10, 10)
        image = addSeaMonster(image, 25, 25)

        day.doRevealSeaMonsters(image, seaMonster)

        val count = image.map { line -> line.count { it == 'O' } }.reduce { acc, i -> acc + i }

        assertEquals(30, count) // West
    }

    private fun addSeaMonster(picture: Array<CharArray>, row: Int, column: Int): Array<CharArray> {

        picture[row][column + 18] = '#'
        picture[row + 1][column] = '#'
        picture[row + 1][column + 5] = '#'
        picture[row + 1][column + 6] = '#'
        picture[row + 1][column + 11] = '#'
        picture[row + 1][column + 12] = '#'
        picture[row + 1][column + 17] = '#'
        picture[row + 1][column + 18] = '#'
        picture[row + 1][column + 19] = '#'
        picture[row + 2][column + 1] = '#'
        picture[row + 2][column + 4] = '#'
        picture[row + 2][column + 7] = '#'
        picture[row + 2][column + 10] = '#'
        picture[row + 2][column + 13] = '#'
        picture[row + 2][column + 16] = '#'

        return picture
    }
}