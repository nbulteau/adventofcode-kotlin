package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import java.security.MessageDigest


// --- Day 4: The Ideal Stocking Stuffer ---
// https://adventofcode.com/2015/day/4
fun main() {
    val data = "bgvyzdsv"
    val day = Day04(2015, 4, "The Ideal Stocking Stuffer")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day04(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {


    fun partOne(secretKey: String): Int {
        val md = MessageDigest.getInstance("MD5")
        var number = 0

        while (true) {
            val input = secretKey + number
            val hash = md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
            if (hash.startsWith("00000")) {
                return number
            }
            number++
        }

        return 0
    }

    fun partTwo(secretKey: String): Int {
        val md = MessageDigest.getInstance("MD5")
        var number = 0

        while (true) {
            val input = secretKey + number
            val hash = md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
            if (hash.startsWith("000000")) {
                return number
            }
            number++
        }

        return 0    }
}
