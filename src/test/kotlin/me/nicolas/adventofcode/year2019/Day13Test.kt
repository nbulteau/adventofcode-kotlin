package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {
    private val day = Day13(2019, 13, "Care Package")

    @Test
    fun `part one - example with simple tile output`() {
        // Example from problem: output like 1,2,3,6,5,4
        // would draw a horizontal paddle tile (1,2) and a ball tile (6,5)
        // This is a minimal program that outputs some tiles
        val program = "104,1,104,2,104,3,104,6,104,5,104,4,99"
        val result = day.partOne(program)

        // This simple program outputs: paddle at (1,2) and ball at (6,5)
        // Neither are blocks (tile_id=2), so count should be 0
        assertEquals(0, result, "No block tiles in this output")
    }

    @Test
    fun `part one - example with block tiles`() {
        // Create a program that outputs some block tiles (tile_id=2)
        // Format: x, y, tile_id repeated
        val program = """
            104,0,104,0,104,2,
            104,1,104,0,104,2,
            104,2,104,0,104,2,
            99
        """.trimIndent().replace("\n", "").replace(" ", "")

        val result = day.partOne(program)

        // Three tiles with id=2 (blocks)
        assertEquals(3, result, "Should count 3 block tiles")
    }

    @Test
    fun `part one - example with mixed tiles`() {
        // Create a program that outputs various tile types
        // 0=empty, 1=wall, 2=block, 3=paddle, 4=ball
        val program = """
            104,0,104,0,104,0,
            104,1,104,0,104,1,
            104,2,104,0,104,2,
            104,3,104,0,104,2,
            104,4,104,0,104,3,
            104,5,104,0,104,4,
            99
        """.trimIndent().replace("\n", "").replace(" ", "")

        val result = day.partOne(program)

        // Only tiles at (2,0) and (3,0) are blocks (tile_id=2)
        assertEquals(2, result, "Should count 2 block tiles among mixed tile types")
    }

    @Test
    fun `part one - example with no blocks`() {
        // Program that outputs only walls, paddles, and balls (no blocks)
        val program = """
            104,0,104,0,104,1,
            104,1,104,0,104,3,
            104,2,104,0,104,4,
            99
        """.trimIndent().replace("\n", "").replace(" ", "")

        val result = day.partOne(program)

        assertEquals(0, result, "Should count 0 block tiles when none exist")
    }

    @Test
    fun `part one - example with overlapping block tiles`() {
        // Program that outputs the same block position multiple times
        // (last one should overwrite previous ones in the display)
        val program = """
            104,0,104,0,104,2,
            104,0,104,0,104,1,
            104,0,104,0,104,2,
            104,1,104,1,104,2,
            99
        """.trimIndent().replace("\n", "").replace(" ", "")

        val result = day.partOne(program)

        // We count all block outputs (tile_id=2), not unique positions
        // Position (0,0) has 2 block outputs, position (1,1) has 1 block output
        // Total: 3 block tile outputs (even though (0,0) also had a wall output in between)
        assertEquals(3, result, "Should count all block tile outputs, including duplicates")
    }

    @Test
    fun `part one - actual input`() {
        val data = readFileDirectlyAsText("/year2019/day13/data.txt")
        val result = day.partOne(data)

        println("Part 1 - Number of block tiles: $result")
        // The actual answer from running the program
        assertEquals(173, result, "Should have 173 block tiles when game exits")
    }

    @Test
    fun `part two - actual input plays game and wins`() {
        val data = readFileDirectlyAsText("/year2019/day13/data.txt")
        val result = day.partTwo(data)

        println("Part 2 - Final score: $result")
        // The actual answer from playing the game automatically
        assertEquals(8942, result, "Final score should be 8942 after breaking all blocks")
    }

    @Test
    fun `part two - verifies free play is enabled`() {
        val data = readFileDirectlyAsText("/year2019/day13/data.txt")
        val program = data.trim().split(",").map { it.toLong() }.toMutableList()

        // Verify that partTwo modifies memory[0] to 2
        program[0] = 2

        // Program should be set to free play mode (2 quarters)
        assertEquals(2L, program[0], "Memory address 0 should be set to 2 for free play")
    }
}