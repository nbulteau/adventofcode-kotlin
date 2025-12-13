package me.nicolas.adventofcode.year2019

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18Test {
    private val day = Day18(2019, 18)

    private val test1 = """
        #########
        #b.A.@.a#
        #########
    """.trimIndent()

    @Test
    fun partOne_1() {
        assertEquals(8, day.partOne(test1))
    }

    private val test2 = """
        ########################
        #f.D.E.e.C.b.A.@.a.B.c.#
        ######################.#
        #d.....................#
        ########################
    """.trimIndent()

    @Test
    fun partOne_2() {
        assertEquals(86, day.partOne(test2))
    }

    private val test3 = """
        ########################
        #...............b.C.D.f#
        #.######################
        #.....@.a.B.c.d.A.e.F.g#
        ########################
    """.trimIndent()

    @Test
    fun partOne_3() {
        assertEquals(132, day.partOne(test3))
    }

    private val test4 = """
        #################
        #i.G..c...e..H.p#
        ########.########
        #j.A..b...f..D.o#
        ########@########
        #k.E..a...g..B.n#
        ########.########
        #l.F..d...h..C.m#
        #################
    """.trimIndent()

    @Test
    fun partOne_4() {
        assertEquals(136, day.partOne(test4))
    }

    private val test5 = """
        ########################
        #@..............ac.GI.b#
        ###d#e#f################
        ###A#B#C################
        ###g#h#i################
        ########################
    """.trimIndent()

    @Test
    fun partOne_5() {
        assertEquals(81, day.partOne(test5))
    }

    private val testPartTwo1 = """
        #######
        #a.#Cd#
        ##...##
        ##.@.##
        ##...##
        #cB#Ab#
        ####### 
    """.trimIndent()

    @Test
    fun partTwo_1() {
        // use empty input -> partTwo should return 0
        assertEquals(8, day.partTwo(testPartTwo1))
    }

    private val testPartTwo2 = """
        ###############
        #d.ABC.#.....a#
        ######...######
        ######.@.######
        ######...######
        #b.....#.....c#
        ###############
    """.trimIndent()

    @Test
    fun partTwo_2() {
        // use empty input -> partTwo should return 0
        assertEquals(24, day.partTwo(testPartTwo2))
    }

    private val testPartTwo3 = """
        #############
        #DcBa.#.GhKl#
        #.###...#I###
        #e#d#.@.#j#k#
        ###C#...###J#
        #fEbA.#.FgHi#
        #############
    """.trimIndent()

    @Test
    fun partTwo_3() {
        // use empty input -> partTwo should return 0
        assertEquals(32, day.partTwo(testPartTwo3))
    }

    private val testPartTwo4 = """
        #############
        #g#f.D#..h#l#
        #F###e#E###.#
        #dCba...BcIJ#
        #####.@.#####
        #nK.L...G...#
        #M###N#H###.#
        #o#m..#i#jk.#
        #############
    """.trimIndent()

    @Test
    fun partTwo_4() {
        // use empty input -> partTwo should return 0
        assertEquals(72, day.partTwo(testPartTwo4))
    }
}