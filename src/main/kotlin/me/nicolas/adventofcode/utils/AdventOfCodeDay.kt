package me.nicolas.adventofcode.utils

abstract class AdventOfCodeDay(year: Int, day: Int, title: String) {

    init {
        println("--- Day $day: $title ---")
        println(blue("https://adventofcode.com/$year/day/$day"))
        println()
    }
}