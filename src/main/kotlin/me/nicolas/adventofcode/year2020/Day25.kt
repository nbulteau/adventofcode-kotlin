package me.nicolas.adventofcode.year2020

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 25: Combo Breaker ---
// https://adventofcode.com/2020/day/25
@ExperimentalTime
fun main() {

    println("--- Day 25: Combo Breaker ---")
    println()

    val training = Pair(5764801, 17807724)
    val data = Pair(19241437, 17346587)

    val (cardPublicKey, doorPublicKey) = data

    // Part One
    Day25().partOne(cardPublicKey, doorPublicKey)

    // Part Two (168 s)
    val duration = measureTime { Day25().partTwo() }
    println("Part two duration : $duration")
}


private class Day25 {

    fun partOne(cardPublicKey: Int, doorPublicKey: Int) {

        val initialSubjectNumber = 7

        // card's loop size
        var value = 1
        var cardLoopSize = 0
        while (value != cardPublicKey) {
            value *= initialSubjectNumber
            value %= 20201227
            cardLoopSize++
        }

        // process encryption key
        var encryptionKey = 1L
        for (i in 0 until cardLoopSize) {
            encryptionKey *= doorPublicKey
            encryptionKey %= 20201227
        }

        println("Part one = $encryptionKey")
    }


    fun partTwo() {
        // Nothing to do :)
        println("Part two = $")
    }
}