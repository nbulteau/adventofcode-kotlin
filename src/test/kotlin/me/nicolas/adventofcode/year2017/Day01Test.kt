package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {
    private val day = Day01()

    @Test
    fun partOne_example1122() {
        assertEquals(3, day.partOne("1122"))
    }

    @Test
    fun partOne_example1111() {
        assertEquals(4, day.partOne("1111"))
    }

    @Test
    fun partOne_example1234() {
        assertEquals(0, day.partOne("1234"))
    }

    @Test
    fun partOne_example91212129() {
        assertEquals(9, day.partOne("91212129"))
    }

    @Test
    fun partTwo_example1212() {
        assertEquals(6, day.partTwo("1212"))
    }

    @Test
    fun partTwo_example1221() {
        assertEquals(0, day.partTwo("1221"))
    }

    @Test
    fun partTwo_example123425() {
        assertEquals(4, day.partTwo("123425"))
    }

    @Test
    fun partTwo_example123123() {
        assertEquals(12, day.partTwo("123123"))
    }

    @Test
    fun partTwo_example12131415() {
        assertEquals(4, day.partTwo("12131415"))
    }
}