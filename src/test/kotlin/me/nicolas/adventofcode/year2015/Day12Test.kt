package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
    private val day = Day12(2015, 12)

    val json1 = "[1,2,3]"
    val json2 = "{\"a\":2,\"b\":4}"
    val json3 = "[[[3]]]"
    val json4 = "{\"a\":{\"b\":4},\"c\":-1}"
    val json5 = "{\"a\":[-1,1]}"
    val json6 = "[-1,{\"a\":1}]"
    val json7 = "[]"
    val json8 = "{}"
    val json9 = "[1,{\"c\":\"red\",\"b\":2},3]"
    val json10 = "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}"
    val json11 = "[1,\"red\",5]"

    @Test
    fun partOne() {
        assertEquals(6, day.partOne(json1))
        assertEquals(6, day.partOne(json2))
        assertEquals(3, day.partOne(json3))
        assertEquals(3, day.partOne(json4))
        assertEquals(0, day.partOne(json5))
        assertEquals(0, day.partOne(json6))
        assertEquals(0, day.partOne(json7))
        assertEquals(0, day.partOne(json8))

    }

    @Test
    fun partTwo() {
        assertEquals(6, day.partTwo(json1))
        assertEquals(6, day.partTwo(json2))
        assertEquals(3, day.partTwo(json3))
        assertEquals(3, day.partTwo(json4))
        assertEquals(0, day.partTwo(json5))
        assertEquals(0, day.partTwo(json6))
        assertEquals(0, day.partTwo(json7))
        assertEquals(0, day.partTwo(json8))
        assertEquals(4, day.partTwo(json9))
        assertEquals(0, day.partTwo(json10))
        assertEquals(6, day.partTwo(json11))
    }
}