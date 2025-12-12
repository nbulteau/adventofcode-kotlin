package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12WithOptimisedGridTest {
    private val day = Day12WithOptimisedGrid(2025, 12, "Christmas Tree Farm")

    private val data ="""
        0:
        ###
        ##.
        ##.

        1:
        ###
        ##.
        .##

        2:
        .##
        ###
        ##.

        3:
        ##.
        ###
        ##.

        4:
        ###
        #..
        ###

        5:
        ###
        .#.
        ###

        4x4: 0 0 0 0 2 0
        12x5: 1 0 1 0 2 2
    """.trimIndent()

    private val longRunningData ="""
        0:
        ###
        ##.
        ##.

        1:
        ###
        ##.
        .##

        2:
        .##
        ###
        ##.

        3:
        ##.
        ###
        ##.

        4:
        ###
        #..
        ###

        5:
        ###
        .#.
        ###

        4x4: 0 0 0 0 2 0
        12x5: 1 0 1 0 2 2
        12x5: 1 0 1 0 3 2
    """.trimIndent()


    @Test
    fun partOne() {
        assertEquals(2, day.partOne(data))
    }

    @Test
    fun partTwo() {
        assertEquals(0, day.partTwo(data))
    }
}