package me.nicolas.adventofcode.utils

fun Map<Pair<Int, Int>, Char>.display() {
    val xMin = keys.minByOrNull { it.first }!!.first
    val xMax = keys.maxByOrNull { it.first }!!.first
    val yMin = keys.minByOrNull { it.second }!!.second
    val yMax = keys.maxByOrNull { it.second }!!.second

    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            print(this.getOrDefault(Pair(x, y), '.'))
        }
        println()
    }
    println()
}
