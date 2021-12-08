package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.readFileDirectlyAsText

// https://adventofcode.com/2021/day/4
fun main() {

    val training = readFileDirectlyAsText("/year2021/day04/training.txt")
    val data = readFileDirectlyAsText("/year2021/day04/data.txt")

    val input = data.split("\n")

    Day04().solve(input)
}

class Day04 {

    data class Tile(val value: Int, var isMarked: Boolean = false)
    data class Board(val tiles: List<List<Tile>>) {

        val value: Int
            get() = tiles.sumOf { line ->
                line.filter { tile -> !tile.isMarked }
                    .sumOf { tile -> tile.value }
            }

        fun markNumber(number: Int) {
            for (line in tiles.indices) {
                for (row in tiles[line].indices) {
                    if (tiles[line][row].value == number) {
                        tiles[line][row].isMarked = true
                    }
                }
            }
        }

        fun isWinningBoard(): Boolean {
            for (row in tiles) {
                if (row.all { tile -> tile.isMarked }) {
                    return true
                }
            }
            for (row in tiles[0].indices) {
                if (tiles.map { line -> line[row] }.all { tile -> tile.isMarked }) {
                    return true
                }
            }
            return false
        }
    }

    fun solve(input: List<String>) {

        val drawnNumbers = input[0].split(",").map { str -> str.toInt() }
        val boards = buildBoards(input.subList(2, input.size))

        println("Part one answer = ${partOne(drawnNumbers, boards)}")

        println("Part two answer = ${partTwo(drawnNumbers, boards)}")
    }

    private fun partOne(drawnNumbers: List<Int>, boards: List<Board>): Int {
        drawnNumbers.forEach { drawnNumber ->
            for (board in boards) {
                board.markNumber(drawnNumber)
                if (board.isWinningBoard()) {
                    return board.value * drawnNumber
                }
            }
        }
        return 0
    }

    private fun partTwo(drawnNumbers: List<Int>, boards: List<Board>): Int {
        drawnNumbers.forEach { drawnNumber ->
            for (board in boards) {
                board.markNumber(drawnNumber)
                if (boards.all { it.isWinningBoard() }) {
                    return board.value * drawnNumber
                }
            }
        }
        return 0
    }

    private fun buildBoards(lines: List<String>): List<Board> {
        val boards = mutableListOf<Board>()

        var index = 0
        do {
            val listOfTiles = mutableListOf<List<Tile>>()
            for (x in 0..4) {
                val line = lines[index++].windowed(2, 3).map { str -> Tile(str.trim().toInt()) }
                listOfTiles.add(line)
            }
            boards.add(Board(listOfTiles))
            index++
        } while (index < lines.size)

        return boards
    }
}

