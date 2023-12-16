package me.nicolas.adventofcode.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GridTest {

    @Test
    fun of() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        assertEquals(9, grid.map().size)
        assertEquals('1', grid[Pair(0, 0)])
        assertEquals('2', grid[Pair(0, 1)])
        assertEquals('3', grid[Pair(0, 2)])
        assertEquals('4', grid[Pair(1, 0)])
        assertEquals('5', grid[Pair(1, 1)])
        assertEquals('6', grid[Pair(1, 2)])
        assertEquals('7', grid[Pair(2, 0)])
        assertEquals('8', grid[Pair(2, 1)])
        assertEquals('9', grid[Pair(2, 2)])
    }

    @Test
    fun getCardinalNeighbors() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        assertEquals(2, grid.getCardinalNeighbors(Pair(0, 0)).size)
        assertEquals(3, grid.getCardinalNeighbors(Pair(0, 1)).size)
        assertEquals(2, grid.getCardinalNeighbors(Pair(0, 2)).size)
        assertEquals(3, grid.getCardinalNeighbors(Pair(1, 0)).size)
        assertEquals(4, grid.getCardinalNeighbors(Pair(1, 1)).size)
        assertEquals(3, grid.getCardinalNeighbors(Pair(1, 2)).size)
        assertEquals(2, grid.getCardinalNeighbors(Pair(2, 0)).size)
        assertEquals(3, grid.getCardinalNeighbors(Pair(2, 1)).size)
        assertEquals(2, grid.getCardinalNeighbors(Pair(2, 2)).size)
    }

    @Test
    fun findAll() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        assertEquals(1, grid.findAll('1').size)
        assertEquals(1, grid.findAll('2').size)
        assertEquals(1, grid.findAll('3').size)
        assertEquals(1, grid.findAll('4').size)
        assertEquals(1, grid.findAll('5').size)
        assertEquals(1, grid.findAll('6').size)
        assertEquals(1, grid.findAll('7').size)
        assertEquals(1, grid.findAll('8').size)
        assertEquals(1, grid.findAll('9').size)
    }

    @Test
    fun invert() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        val invertedGrid = grid.invert()

        assertEquals(9, invertedGrid.map().size)
        assertEquals('1', invertedGrid[Pair(0, 0)])
        assertEquals('4', invertedGrid[Pair(0, 1)])
        assertEquals('7', invertedGrid[Pair(0, 2)])
        assertEquals('2', invertedGrid[Pair(1, 0)])
        assertEquals('5', invertedGrid[Pair(1, 1)])
        assertEquals('8', invertedGrid[Pair(1, 2)])
        assertEquals('3', invertedGrid[Pair(2, 0)])
        assertEquals('6', invertedGrid[Pair(2, 1)])
        assertEquals('9', invertedGrid[Pair(2, 2)])
    }

    @Test
    fun display() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        grid.display()
    }

    @Test
    fun getRow() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        assertEquals(3, grid.getRow(0).size)
        assertEquals(3, grid.getRow(1).size)
        assertEquals(3, grid.getRow(2).size)
    }

    @Test
    fun getColumn() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        assertEquals(3, grid.getColumn(0).size)
        assertEquals(3, grid.getColumn(1).size)
        assertEquals(3, grid.getColumn(2).size)
    }

    @Test
    fun get() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        assertEquals('1', grid[Pair(0, 0)])
        assertEquals('2', grid[Pair(0, 1)])
        assertEquals('3', grid[Pair(0, 2)])
        assertEquals('4', grid[Pair(1, 0)])
        assertEquals('5', grid[Pair(1, 1)])
        assertEquals('6', grid[Pair(1, 2)])
        assertEquals('7', grid[Pair(2, 0)])
        assertEquals('8', grid[Pair(2, 1)])
        assertEquals('9', grid[Pair(2, 2)])
    }

    @Test
    fun set() {
        val grid = Grid.of("""
            123
            456
            789
        """.trimIndent()
        )

        grid[0, 0] = 'a'
        grid[0, 1] = 'b'
        grid[0, 2] = 'c'
        grid[1, 0] = 'd'
        grid[1, 1] = 'e'
        grid[1, 2] = 'f'
        grid[2, 0] = 'g'
        grid[2, 1] = 'h'
        grid[2, 2] = 'i'

        assertEquals('a', grid[Pair(0, 0)])
        assertEquals('b', grid[Pair(0, 1)])
        assertEquals('c', grid[Pair(0, 2)])
        assertEquals('d', grid[Pair(1, 0)])
        assertEquals('e', grid[Pair(1, 1)])
        assertEquals('f', grid[Pair(1, 2)])
        assertEquals('g', grid[Pair(2, 0)])
        assertEquals('h', grid[Pair(2, 1)])
        assertEquals('i', grid[Pair(2, 2)])
    }
}