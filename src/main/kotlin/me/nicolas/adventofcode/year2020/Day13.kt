package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText


// --- Day 13:  ---
// https://adventofcode.com/2020/day/13
fun main() {

    println("--- Day 13:  ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day13/training.txt")
    val data = readFileDirectlyAsText("/year2020/day13/data.txt")

    val (timestampStr, bus) = training.split("\n")
    val timestamp = timestampStr.toInt()
    val busIds = bus.split(",")

    // Part One
    Day13().partOne(timestamp, busIds)

    // Part Two
    Day13().partTwo(busIds)
}

class Day13 {

    fun partOne(initialTimestamp: Int, busList: List<String>) {

        val busIdsList = busList.filter { str -> str != "x" }.map { str -> str.toInt() }
        var timestamp = initialTimestamp.toDouble()
        var busId = 0
        var found = false

        do {
            timestamp++
            for (id in busIdsList) {
                if (timestamp.div(id).isInteger()) {
                    found = true
                    busId = id
                    break
                }
            }
        } while (!found)

        println("Part one = ${(timestamp - initialTimestamp) * busId}")
    }

    private fun Double.isInteger(): Boolean {
        return this - this.toInt() == 0.0
    }

    /**
     * 7,13,x,x,59,x,31,19
     *
     * https://rosettacode.org/wiki/Chinese_remainder_theorem#Kotlin
     */
    fun partTwo(busList: List<String>) {

        val buses = mutableMapOf<Int, Int>()
        for (index in busList.indices) {
            if (busList[index] != "x") {
                val value = busList[index].toInt()
                buses[value] = index
            }
        }

        println(buses)

        var result: Long = 0
        var incrementer: Long = 1

        for (busId in buses.keys) {
            while ((result + buses[busId]!!) % busId != 0L) {
                result += incrementer
            }
            // bus ids are prime, so you just needed to multiply them together
            incrementer *= busId.toLong()
        }

        println("Part two = $result")
    }

    /**
     *
     * https://www.apprendre-en-ligne.net/crypto/rabin/resteschinois.html
     *
     * x,x,11,x,7,x,x,x,x,x,13
     *
     * x mod 7 = 5
     * x mod 11 = 3
     * x mod 13 = 10
     *
     * M = m1 · ... · mn
     *   => M = 1001
     * Mi = M/mi
     *   => M1 = 143, M2 = 91, M3 = 77.
     * yi = Mi-1 mod mi
     *   => y1 = 5, y2 = 4, y3 = 12.
     * x = (a1 * M1 * y1 + ... + an * Mn * yn) mod M
     *   => x = ((5 * 143 * 5) + (3 * 91 * 4) + (10 * 77 * 12)) mod 1001
     *      x = 13 907 mod 1001
     *      x = 894
     */
    fun partTwoNotWOrking(busList: List<String>) {

        val buses = mutableMapOf<Int, Int>()
        for (index in busList.indices) {
            if (busList[index] != "x") {
                val value = busList[index].toInt()
                buses[value] = index
            }
        }

        // M = m1 · ... · mn
        var m: Long = buses.keys.reduce { M, bus -> M * bus }.toLong()
        println("M = $m")
        // Mi = M/mi
        val mi: Map<Int, Long> = buses.map { bus -> bus.key to m / bus.key.toLong() }.toMap()
        println("Mi = $mi")
        // yi = Mi-1 mod mi
        val yi: Map<Int, Int> = buses.map { bus -> bus.key to modInverse(mi[bus.key]!!, bus.key) }.toMap()
        println("yi = $yi")
        // x = (a1 * M1 * y1 + ... + an * Mn * yn) mod M
        val result = buses.map { bus ->
            if (buses[bus.key]!! > 0) {
                buses[bus.key]!!
            } else {
                1
            } * mi[bus.key]!! * yi[bus.key]!!
        }.sumOf { it } % m

        println("x = ${buses.map { bus -> "(${buses[bus.key]!!} * ${mi[bus.key]!!} * ${yi[bus.key]!!})" }} mod $m")
        println("x = ${buses.map { bus -> buses[bus.key]!! * mi[bus.key]!! * yi[bus.key]!! }.sumOf { it }} mod $m")

        println("Part two = $result")
    }

    // https://www.apprendre-en-ligne.net/crypto/rabin/euclide.html
    fun modInverse(a: Long, m: Int): Int {
        val mod = a % m
        for (x in 1..m) {
            if ((mod * x) % m == 1L) {
                return x
            }
        }
        return 0
    }


}