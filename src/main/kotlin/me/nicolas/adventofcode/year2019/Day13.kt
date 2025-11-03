package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 13: Care Package ---
// https://adventofcode.com/2019/day/13
fun main() {
    val data = readFileDirectlyAsText("/year2019/day13/data.txt")
    val day = Day13(2019, 13)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day13(year: Int, day: Int, title: String = "Care Package") : AdventOfCodeDay(year, day, title) {
    /**
     * Part One: Count the number of block tiles on the screen when the game exits.
     *
     * The arcade cabinet outputs tiles in groups of 3 values:
     * - x position (distance from the left)
     * - y position (distance from the top)
     * - tile id (0=empty, 1=wall, 2=block, 3=paddle, 4=ball)
     *
     * We run the IntCode program once and count all tiles with id = 2 (blocks).
     */
    fun partOne(data: String): Int {
        // Parse the IntCode program from comma-separated input
        val program = data.trim().split(",").map { it.toLong() }
        val intCodeProgram = IntCodeProgram(program)

        // Execute the program and collect all outputs
        val output = intCodeProgram.execute(mutableListOf())

        // Count block tiles (tile id = 2)
        // Output comes in groups of 3: x, y, tile_id
        var blockCount = 0
        for (i in output.indices step 3) {
            if (i + 2 < output.size) {
                val tileId = output[i + 2]
                if (tileId == 2L) {
                    blockCount++
                }
            }
        }

        return blockCount
    }

    /**
     * Part Two: Play the game automatically and return the final score.
     *
     * Strategy:
     * 1. Set memory address 0 to 2 to play for free (insert quarters)
     * 2. Track the ball and paddle positions as the game runs
     * 3. Move the paddle to follow the ball by providing joystick input:
     *    - Input -1: move paddle left
     *    - Input 0: keep paddle neutral
     *    - Input 1: move paddle right
     * 4. When output has x=-1, y=0, the third value is the score (not a tile)
     * 5. Continue until all blocks are broken and return the final score
     */
    fun partTwo(data: String): Long {
        // Parse program and modify memory address 0 to enable free play
        val program = data.trim().split(",").map { it.toLong() }.toMutableList()
        // Set memory address 0 to 2 to play for free
        program[0] = 2

        val intCodeProgram = IntCodeProgram(program)
        val inputs = mutableListOf<Long>()

        var score = 0L
        var ballX = 0
        var paddleX = 0

        // Game loop: process outputs and provide inputs until game halts
        while (!intCodeProgram.isHalted()) {
            // Get three outputs: x, y, tile_id (or score)
            val x = intCodeProgram.executeUntilOutput(inputs) ?: break
            val y = intCodeProgram.executeUntilOutput(inputs) ?: break
            val tileIdOrScore = intCodeProgram.executeUntilOutput(inputs) ?: break

            // Check if this is a score update (special position x=-1, y=0)
            if (x == -1L && y == 0L) {
                score = tileIdOrScore
            } else {
                // Track ball and paddle positions to control the game
                when (tileIdOrScore) {
                    3L -> paddleX = x.toInt()  // Paddle tile
                    4L -> {
                        ballX = x.toInt()  // Ball tile
                        // AI strategy: move paddle to follow the ball
                        inputs.add(when {
                            ballX < paddleX -> -1L  // Move left
                            ballX > paddleX -> 1L   // Move right
                            else -> 0L              // Stay neutral
                        })
                    }
                }
            }
        }

        return score
    }
}