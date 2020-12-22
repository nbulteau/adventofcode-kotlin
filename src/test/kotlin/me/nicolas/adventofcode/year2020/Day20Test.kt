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
    fun rotateClockwise() {

        var bigPicture = Array(5) { CharArray(5) }
        bigPicture[0][1] = '#'
        bigPicture[0][2] = '#'
        bigPicture[0][3] = '#'

        bigPicture = Day20().rotateClockwise(bigPicture)
        assertEquals('#', bigPicture[1][4]) // West
        assertEquals('#', bigPicture[2][4]) // West
        assertEquals('#', bigPicture[3][4]) // West
    }

    @Test
    fun flipHorizontal() {

        var bigPicture = Array(5) { CharArray(5) }
        bigPicture[1][0] = '#'
        bigPicture[2][0] = '#'
        bigPicture[3][0] = '#'

        bigPicture = Day20().flipHorizontal(bigPicture)
        assertEquals('#', bigPicture[1][4]) // West
        assertEquals('#', bigPicture[2][4]) // West
        assertEquals('#', bigPicture[3][4]) // West
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
                str.mapIndexedNotNull { column, c ->
                    if (c == '#') Pair(
                        row,
                        column
                    ) else null
                }
            }

        var image = Array(50) { CharArray(50) }

        image = addSeaMonster(image, 10, 10)
        image = addSeaMonster(image, 25, 25)

        Day20().doRevealSeaMonsters(image, seaMonster)

        val count = image.map { line -> line.count { it == 'O' } }.reduce { acc, i -> acc + i }

        assertEquals(30, count) // West
    }

    private fun addSeaMonster(bigPicture: Array<CharArray>, row: Int, column: Int): Array<CharArray> {

        bigPicture[row][column + 18] = '#'
        bigPicture[row + 1][column] = '#'
        bigPicture[row + 1][column + 5] = '#'
        bigPicture[row + 1][column + 6] = '#'
        bigPicture[row + 1][column + 11] = '#'
        bigPicture[row + 1][column + 12] = '#'
        bigPicture[row + 1][column + 17] = '#'
        bigPicture[row + 1][column + 18] = '#'
        bigPicture[row + 1][column + 19] = '#'
        bigPicture[row + 2][column + 1] = '#'
        bigPicture[row + 2][column + 4] = '#'
        bigPicture[row + 2][column + 7] = '#'
        bigPicture[row + 2][column + 10] = '#'
        bigPicture[row + 2][column + 13] = '#'
        bigPicture[row + 2][column + 16] = '#'

        return bigPicture
    }
}