package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText


// --- Day 13: Shuttle Search ---
// https://adventofcode.com/2020/day/13
fun main() {

    println("--- Day 13: Shuttle Search ---")
    println()

    val training = readFileDirectlyAsText("/year2020/day13/training.txt")
    val data = readFileDirectlyAsText("/year2020/day13/data.txt")

    val (timestampStr, bus) = data.split("\n")
    val timestamp = timestampStr.toInt()
    val busIds = bus.split(",")

    // Part One
    Day13().partOne(timestamp, busIds)

    // Part Two
    Day13().partTwo(busIds)

    Day13().chineseRemainder(busIds)
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
     * busId must be > 2
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
     * https://rosettacode.org/wiki/Chinese_remainder_theorem#Kotlin
     * https://www.apprendre-en-ligne.net/crypto/rabin/resteschinois.html
     * https://fr.wikipedia.org/wiki/Th%C3%A9or%C3%A8me_des_restes_chinois
     *
     * x,x,11,x,7,x,x,x,x,x,13
     * x,x,x,17,11,6 => 785
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
    fun chineseRemainder(busList: List<String>) {

        println()

        val buses = mutableMapOf<Long, Long>()
        for (index in busList.indices) {
            if (busList[index] != "x") {
                val value = busList[index].toLong()
                buses[value] = index.toLong()
            }
        }

        // M = m1 · ... · mn
        var product: Long = buses.keys.reduce { M: Long, bus -> M * bus }

        println("M = $product")
        // Mi = M/mi
        val mi: Map<Long, Long> = buses.map { bus -> bus.key to product / bus.key }.toMap()
        println("Mi = $mi")
        // yi = Mi-1 mod mi => mod inverse
        val yi: Map<Long, Long> = buses.map { bus -> bus.key to modInverse(mi[bus.key]!!, bus.key) }.toMap()
        println("yi = $yi")
        // x = (a1 * M1 * y1 + ... + an * Mn * yn) mod M
        // congruence = busId - index
        val result =
            buses.map { bus -> (bus.key - buses[bus.key]!!) * mi[bus.key]!! * yi[bus.key]!! }.sumOf { it } % product
        println("x = ${buses.map { bus -> "(${buses[bus.key]!!} * ${mi[bus.key]!!} * ${yi[bus.key]!!})" }} mod $product")
        println(
            "x = ${
                buses.map { bus -> (bus.key - buses[bus.key]!!) * mi[bus.key]!! * yi[bus.key]!! }.sumOf { it }
            } mod $product"
        )

        println("Using Chinese remainder x = $result")
    }

    /**
     * https://www.apprendre-en-ligne.net/crypto/rabin/euclide.html
     */
    fun modInverse(a: Long, m: Long): Long {
        val mod = a % m
        for (x in 1..m) {
            if ((mod * x) % m == 1L) {
                return x
            }
        }
        return 0L
    }
}