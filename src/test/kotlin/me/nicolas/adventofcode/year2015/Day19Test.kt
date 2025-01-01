package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {
    private val day = Day19(2015, 19)
    
    private val test1 = """
        H => HO
        H => OH
        O => HH
        
        HOH    
    """.trimIndent()

    private val test2 = """
        H => HO
        H => OH
        O => HH
        
        HOHOHO    
    """.trimIndent()

    private val test3 = """
        H => OO
        
        H2O    
    """.trimIndent()

    private val test4 = """
        e => H
        e => O
        H => HO
        H => OH
        O => HH
        
        HOH
    """.trimIndent()

    private val test5 = """
        e => H
        e => O
        H => HO
        H => OH
        O => HH
        
        HOHOHO
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(4, day.partOne(test1))
    }

    @Test
    fun partOneTwo() {
        assertEquals(7, day.partOne(test2))
    }

    @Test
    fun partOneThree() {
        assertEquals(1, day.partOne(test3))
    }

    @Test
    fun partTwo() {
        assertEquals(3, day.partTwo(test4))
    }

    @Test
    fun partTwoTwo() {
        assertEquals(6, day.partTwo(test5))
    }
}