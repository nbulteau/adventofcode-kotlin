package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText


// --- Day 3: Toboggan Trajectory ---
// https://adventofcode.com/2020/day/3
fun main() {

    val training = readFileDirectlyAsText("/year2020/day3/training.txt")
    val data = readFileDirectlyAsText("/year2020/day3/data.txt")

    val pattern = data.split("\n")

    partOne(pattern)

    partTwo(pattern)
}

private fun partOne(maze: List<String>) {

    val count = treesCount(maze, 1, 1) *
            treesCount(maze, 1, 3) *
            treesCount(maze, 1, 5) *
            treesCount(maze, 1, 7) *
            treesCount(maze, 2, 1)

    println(count)
}

private fun partTwo(maze: List<String>) {

    val count = treesCount(maze, 1, 3)

    println(count)
}

private fun treesCount(maze: List<String>, vStep: Int, hStep: Int): Long {
    val width = maze[0].length
    val high = maze.size
    var indexV = 0
    var indexH = 0
    var count = 0L

    do {
        if (maze[indexH][indexV] == '#') {
            count++
        }
        indexH += vStep
        indexV = (indexV + hStep) % width
    } while (indexH < high)

    return count
}
