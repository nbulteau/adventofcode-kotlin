package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2021/day/13
fun main() {
    val data = readFileDirectlyAsText("/year2021/day13/data.txt")
    val day = Day13(2021, 13)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}


class Day13(year: Int, day: Int, title: String = "Transparent Origami") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val (dots: Set<Pair<Int, Int>>, foldInstructions) = parseInput(data)

        val paper = Paper(dots)
        processInstruction(paper, foldInstructions.first())

        return paper.dots.size
    }

    fun partTwo(data: String): Int {
        val (dots: Set<Pair<Int, Int>>, foldInstructions) = parseInput(data)

        val paper = Paper(dots)
        foldInstructions.forEach { instruction ->
            processInstruction(paper, instruction)
        }
        paper.display()

        return paper.dots.size
    }

    private fun parseInput(data: String): Pair<Set<Pair<Int, Int>>, List<String>> {
        val (dotsPart, foldInstructionsPart) = data.split("\n\n")
        val dots: Set<Pair<Int, Int>> = dotsPart
            .split("\n")
            .flatMap { line ->
                line
                    .split(",")
                    .map { str -> str.toInt() }
                    .zipWithNext { a, b -> Pair(a, b) }
            }
            .toSet()
        val foldInstructions = foldInstructionsPart.split("\n")
        return Pair(dots, foldInstructions)
    }


    private class Paper(dots: Set<Pair<Int, Int>>) {

        var dots: Set<Pair<Int, Int>> = dots
            private set

        fun foldX(line: Int) {
            val toKeep = dots.filter { xy -> xy.first < line }.toMutableSet()
            val fold = dots.filter { xy -> xy.first > line }.map { xy ->
                Pair(xy.first - ((xy.first - line) * 2), xy.second)
            }.toSet()
            toKeep.addAll(fold)
            dots = toKeep
        }

        fun foldY(line: Int) {
            val toKeep = dots.filter { xy -> xy.second < line }.toMutableSet()
            val fold = dots.filter { xy -> xy.second > line }.map { xy ->
                Pair(xy.first, xy.second - ((xy.second - line) * 2))
            }.toSet()
            toKeep.addAll(fold)
            dots = toKeep
        }

        fun display() {
            val maxX = dots.maxOf { it.first } + 1
            val maxY = dots.maxOf { it.second } + 1
            // init grid
            val grid = ArrayList<CharArray>(maxY)
            for (y in 0 until maxY) {
                grid.add(CharArray(maxX) { ' ' })
            }
            dots.forEach { grid[it.second][it.first] = '#' }
            // display
            for (y in 0 until maxY) {
                println(grid[y])
            }
        }
    }

    private fun processInstruction(paper: Paper, instruction: String) {
        val line = instruction.split("=").last().toInt()
        if (instruction.startsWith("fold along y")) {
            paper.foldY(line)
        } else {
            paper.foldX(line)
        }
    }
}