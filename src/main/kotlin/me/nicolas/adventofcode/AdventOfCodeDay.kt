package me.nicolas.adventofcode

abstract class AdventOfCodeDay(title: String, adventOfCodeLink: String) {

    init {
        println(title)
        println(blue(adventOfCodeLink))
        println()
    }
}