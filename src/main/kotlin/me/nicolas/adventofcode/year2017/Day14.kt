package me.nicolas.adventofcode.year2017

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 14: ---
// https://adventofcode.com/2017/day/14
fun main() {
    val data = "ljoxqyyw"
    val day = Day14()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day14(year: Int = 2017, day: Int = 14, title: String = "") : AdventOfCodeDay(year, day, title) {

    /**
     * partOne finds the number of used squares in the grid.
     * It generates 128 knot hashes based on the input data suffixed with row indices,
     * converts each hash from hexadecimal to binary, and counts the number of '1' bits
     * across all rows.
     */
    fun partOne(data: String): Int {
        val day10 = Day10()
        var used = 0
        for (i in 0 until 128) {
            val key = "$data-$i"
            val hash = day10.partTwo(key)
            // each hex char -> 4 bits
            for (ch in hash) {
                val v = ch.toString().toInt(16)
                used += Integer.bitCount(v)
            }
        }
        return used
    }

    /**
     * partTwo counts the number of distinct regions of used squares in the grid.
     * It constructs a 128x128 boolean grid from the knot hashes, where 'true' represents a used square.
     * It then performs a breadth-first search (BFS) to identify and count connected regions of 'true' squares.
     */
    fun partTwo(data: String): Int {
        val day10 = Day10()
        val grid = Array(128) { BooleanArray(128) }
        for (i in 0 until 128) {
            val key = "$data-$i"
            val hash = day10.partTwo(key)
            // Convert each hex char to 4 bits and fill row
            var col = 0
            for (char in hash) {
                val v = char.toString().toInt(16)
                // high-bit first: bit 3 down to 0
                for (b in 3 downTo 0) {
                    if (col < 128) {
                        grid[i][col] = ((v shr b) and 1) == 1
                        col++
                    }
                }
            }
            // safety: if row has fewer than 128 bits (shouldn't happen), remaining are false
        }

        val visited = Array(128) { BooleanArray(128) }
        var regions = 0
        val dirs = arrayOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

        // BFS to mark all connected 'true' cells
        fun bfs(sr: Int, sc: Int) {
            val stack = ArrayDeque<Pair<Int, Int>>()
            stack.add(Pair(sr, sc))
            visited[sr][sc] = true
            while (stack.isNotEmpty()) {
                val (r, c) = stack.removeLast()
                for ((dr, dc) in dirs) {
                    val nr = r + dr
                    val nc = c + dc
                    if (nr in 0 until 128 && nc in 0 until 128 && !visited[nr][nc] && grid[nr][nc]) {
                        visited[nr][nc] = true
                        stack.add(Pair(nr, nc))
                    }
                }
            }
        }

        for (r in 0 until 128) {
            for (c in 0 until 128) {
                if (grid[r][c] && !visited[r][c]) {
                    regions++
                    bfs(r, c)
                }
            }
        }

        return regions
    }
}