package me.nicolas.adventofcode.utils

/**
 * Abstract base class for an Advent of Code puzzle day.
 * Each instance represents a specific day's puzzle in a specific year, with a given title.
 *
 * Upon initialization, it prints out the day number, the title of the puzzle, and the URL for the puzzle.
 */
abstract class AdventOfCodeDay(year: Int, day: Int, title: String) {

    init {
        // Print the day number and title
        println("--- Day $day: $title ---")
        // Print the URL for the puzzle
        println(blue("https://adventofcode.com/$year/day/$day"))
        println()
    }
}