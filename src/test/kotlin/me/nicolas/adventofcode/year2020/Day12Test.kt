package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    private val data = """
        F10
        N3
        F7
        R90
        F11
    """.trimIndent()

    private val day = Day12(2020, 12, "Rain Risk")

    @Test
    fun `part one`() {
        assertEquals(25, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(286, day.partTwo(data))
    }

    @Test
    fun getNextDirection() {

        assertEquals(Day12.Direction.EAST, Day12.Direction.EAST.getNextDirection(0))
        assertEquals(Day12.Direction.SOUTH, Day12.Direction.EAST.getNextDirection(90))
        assertEquals(Day12.Direction.WEST, Day12.Direction.EAST.getNextDirection(180))
        assertEquals(Day12.Direction.NORTH, Day12.Direction.EAST.getNextDirection(270))
        assertEquals(Day12.Direction.NORTH, Day12.Direction.NORTH.getNextDirection(0))
        assertEquals(Day12.Direction.EAST, Day12.Direction.NORTH.getNextDirection(90))
        assertEquals(Day12.Direction.SOUTH, Day12.Direction.NORTH.getNextDirection(180))
        assertEquals(Day12.Direction.WEST, Day12.Direction.NORTH.getNextDirection(270))
        assertEquals(Day12.Direction.WEST, Day12.Direction.WEST.getNextDirection(0))
        assertEquals(Day12.Direction.NORTH, Day12.Direction.WEST.getNextDirection(90))
        assertEquals(Day12.Direction.EAST, Day12.Direction.WEST.getNextDirection(180))
        assertEquals(Day12.Direction.SOUTH, Day12.Direction.WEST.getNextDirection(270))
        assertEquals(Day12.Direction.SOUTH, Day12.Direction.SOUTH.getNextDirection(0))
        assertEquals(Day12.Direction.WEST, Day12.Direction.SOUTH.getNextDirection(90))
        assertEquals(Day12.Direction.NORTH, Day12.Direction.SOUTH.getNextDirection(180))
        assertEquals(Day12.Direction.EAST, Day12.Direction.SOUTH.getNextDirection(270))
    }

    @Test
    fun `taxicabGeometry1 sample`() {

        val taxicabGeometry = Day12.TaxicabGeometry()

        taxicabGeometry.move(Day12.Instruction('F', 10))
        assertEquals(10, taxicabGeometry.path)
        taxicabGeometry.move(Day12.Instruction('N', 3))
        assertEquals(13, taxicabGeometry.path)
        taxicabGeometry.move(Day12.Instruction('F', 7))
        assertEquals(20, taxicabGeometry.path)
        taxicabGeometry.move(Day12.Instruction('R', 90))
        assertEquals(20, taxicabGeometry.path)
        taxicabGeometry.move(Day12.Instruction('F', 11))
        assertEquals(25, taxicabGeometry.path)
    }

    @Test
    fun `taxicabGeometry1 rotate`() {

        val taxicabGeometry = Day12.TaxicabGeometry()

        taxicabGeometry.move(Day12.Instruction('R', 0))
        assertEquals(Day12.Direction.EAST, taxicabGeometry.currentDirection)
        taxicabGeometry.move(Day12.Instruction('R', 90))
        assertEquals(Day12.Direction.SOUTH, taxicabGeometry.currentDirection)
        taxicabGeometry.move(Day12.Instruction('R', 180))
        assertEquals(Day12.Direction.NORTH, taxicabGeometry.currentDirection)
        taxicabGeometry.move(Day12.Instruction('R', 270))
        assertEquals(Day12.Direction.WEST, taxicabGeometry.currentDirection)

        taxicabGeometry.move(Day12.Instruction('L', 0))
        assertEquals(Day12.Direction.WEST, taxicabGeometry.currentDirection)
        taxicabGeometry.move(Day12.Instruction('L', 90))
        assertEquals(Day12.Direction.SOUTH, taxicabGeometry.currentDirection)
        taxicabGeometry.move(Day12.Instruction('L', 180))
        assertEquals(Day12.Direction.NORTH, taxicabGeometry.currentDirection)
        taxicabGeometry.move(Day12.Instruction('L', 270))
        assertEquals(Day12.Direction.EAST, taxicabGeometry.currentDirection)
    }

    @Test
    fun `taxicabGeometry2 sample`() {

        val taxicabGeometry = Day12.TaxicabGeometryWithWayPoint()

        taxicabGeometry.move(Day12.Instruction('F', 10))
        assertEquals(110, taxicabGeometry.path)
        taxicabGeometry.move(Day12.Instruction('N', 3))
        assertEquals(110, taxicabGeometry.path)
        taxicabGeometry.move(Day12.Instruction('F', 7))
        assertEquals(208, taxicabGeometry.path)
        taxicabGeometry.move(Day12.Instruction('R', 90))
        assertEquals(208, taxicabGeometry.path)
        taxicabGeometry.move(Day12.Instruction('F', 11))
        assertEquals(286, taxicabGeometry.path)
    }

    @Test
    fun `taxicabGeometry2 rotate`() {

        val taxicabGeometry =
            Day12.TaxicabGeometryWithWayPoint(startWaypointEastWest = -10, startWaypointNorthSouth = 1)

        taxicabGeometry.move(Day12.Instruction('R', 0))
        assertEquals(-10, taxicabGeometry.waypointEastWest)
        assertEquals(1, taxicabGeometry.waypointNorthSouth)
        taxicabGeometry.move(Day12.Instruction('R', 90))
        assertEquals(-1, taxicabGeometry.waypointEastWest)
        assertEquals(-10, taxicabGeometry.waypointNorthSouth)
        taxicabGeometry.move(Day12.Instruction('R', 180))
        assertEquals(1, taxicabGeometry.waypointEastWest)
        assertEquals(10, taxicabGeometry.waypointNorthSouth)
        taxicabGeometry.move(Day12.Instruction('R', 270))
        assertEquals(10, taxicabGeometry.waypointEastWest)
        assertEquals(-1, taxicabGeometry.waypointNorthSouth)

        taxicabGeometry.move(Day12.Instruction('L', 0))
        assertEquals(10, taxicabGeometry.waypointEastWest)
        assertEquals(-1, taxicabGeometry.waypointNorthSouth)
        taxicabGeometry.move(Day12.Instruction('L', 90))
        assertEquals(-1, taxicabGeometry.waypointEastWest)
        assertEquals(-10, taxicabGeometry.waypointNorthSouth)
        taxicabGeometry.move(Day12.Instruction('L', 180))
        assertEquals(1, taxicabGeometry.waypointEastWest)
        assertEquals(10, taxicabGeometry.waypointNorthSouth)
        taxicabGeometry.move(Day12.Instruction('L', 270))
        assertEquals(-10, taxicabGeometry.waypointEastWest)
        assertEquals(1, taxicabGeometry.waypointNorthSouth)
    }

}