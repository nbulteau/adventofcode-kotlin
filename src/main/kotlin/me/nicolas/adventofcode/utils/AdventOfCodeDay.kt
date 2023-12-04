package me.nicolas.adventofcode.utils

abstract class AdventOfCodeDay(title: String, adventOfCodeLink: String) {

    init {
        println(title)
        println(blue(adventOfCodeLink))
        println()
    }
}