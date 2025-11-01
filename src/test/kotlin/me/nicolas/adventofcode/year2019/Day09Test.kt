package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class Day09Test {

    @Test
    fun `test quine program`() {
        // Takes no input and produces a copy of itself as output
        val program = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
        val day09 = Day09(program)
        val intCodeProgram = IntCodeProgram(program.split(",").map { it.toLong() })
        val output = intCodeProgram.execute(mutableListOf())

        val expected = program.split(",").map { it.toLong() }
        assertEquals(expected, output)
    }

    @Test
    fun `test 16-digit number`() {
        // Should output a 16-digit number
        val program = "1102,34915192,34915192,7,4,7,99,0"
        val intCodeProgram = IntCodeProgram(program.split(",").map { it.toLong() })
        val output = intCodeProgram.execute(mutableListOf())

        assertTrue(output[0] >= 1000000000000000L)
        assertTrue(output[0] < 10000000000000000L)
    }

    @Test
    fun `test large number output`() {
        // Should output the large number in the middle
        val program = "104,1125899906842624,99"
        val intCodeProgram = IntCodeProgram(program.split(",").map { it.toLong() })
        val output = intCodeProgram.execute(mutableListOf())

        assertEquals(1125899906842624L, output[0])
    }

    @Test
    fun `solve part 1`() {
        val data = readFileDirectlyAsText("/year2019/day09/data.txt")
        val day09 = Day09(data)
        val result = day09.partOne()
        println("Part 1 BOOST keycode: $result")
        assertTrue(result > 0)
    }

    @Test
    fun `solve part 2`() {
        val data = readFileDirectlyAsText("/year2019/day09/data.txt")
        val day09 = Day09(data)
        val result = day09.partTwo()
        println("Part 2 coordinates: $result")
        assertTrue(result > 0)
    }
}

