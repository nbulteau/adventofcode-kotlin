package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day20Test {

    @Test
    fun extractTiles() {
        val data = """
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
        """.trimIndent()
        val sections = data.split("\n\n")
        val tiles = extractTiles(sections)
        val tile = tiles[0]
        assertEquals("..##.#..#.", tile.edges[0]) // North
        assertEquals("...#.##..#", tile.edges[1]) // East
        assertEquals("..###..###", tile.edges[2]) // South
        assertEquals(".#####..#.", tile.edges[3]) // West
    }

    @Test
    fun rotate1() {

        val data = """
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
        """.trimIndent()
        val sections = data.split("\n\n")
        val tiles = extractTiles(sections)
        val tile = tiles[0]

        tile.rotateClockwise()
        assertEquals(".#..#####.", tile.edges[0]) // North
        assertEquals("..##.#..#.", tile.edges[1]) // East
        assertEquals("#..##.#...", tile.edges[2]) // South
        assertEquals("..###..###", tile.edges[3]) // West
    }

    @Test
    fun rotate2() {

        val data = """
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
        """.trimIndent()
        val sections = data.split("\n\n")
        val tiles = extractTiles(sections)
        val tile = tiles[0]

        tile.rotateClockwise()
        tile.rotateClockwise()
        tile.rotateClockwise()
        tile.rotateClockwise()

        assertEquals("..##.#..#.", tile.edges[0]) // North
        assertEquals("...#.##..#", tile.edges[1]) // East
        assertEquals("..###..###", tile.edges[2]) // South
        assertEquals(".#####..#.", tile.edges[3]) // West
    }

    @Test
    fun flip1() {

        val data = """
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
        """.trimIndent()
        val sections = data.split("\n\n")
        val tiles = extractTiles(sections)
        val tile = tiles[0]

        tile.flipHorizontal()
        assertEquals(".#..#.##..", tile.edges[0]) // North
        assertEquals(".#####..#.", tile.edges[1]) // East
        assertEquals("###..###..", tile.edges[2]) // South
        assertEquals("...#.##..#", tile.edges[3]) // West
    }

    @Test
    fun flip2() {

        val data = """
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
        """.trimIndent()
        val sections = data.split("\n\n")
        val tiles = extractTiles(sections)

        val tile = tiles[0]

        tile.flipHorizontal()
        tile.flipHorizontal()

        assertEquals("..##.#..#.", tile.edges[0]) // North
        assertEquals("...#.##..#", tile.edges[1]) // East
        assertEquals("..###..###", tile.edges[2]) // South
        assertEquals(".#####..#.", tile.edges[3]) // West
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

        Day20().doRevealSeaMonsters(image, seaMonster)

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