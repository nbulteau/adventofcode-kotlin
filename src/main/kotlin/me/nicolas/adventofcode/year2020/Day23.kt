package me.nicolas.adventofcode.year2020

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// --- Day 23: Crab Cups ---
// https://adventofcode.com/2020/day/23
@ExperimentalTime
fun main() {

    println("--- Day 23: Crab Cups ---")
    println()

    val training = 389125467
    val data = 872495136

    val list = data.toString().chunked(1).map { str -> str.toInt() }

    // Part One
    Day23PartOne().partOne(list, 100)

    // Part Two
    val duration = measureTime { Day23PartTwo().partTwo(list.toMutableList(), 10_000_000) }
    println("Part two duration : $duration")
}


private class Day23PartOne {

    class Circle(private val list: MutableList<Int>) : MutableList<Int> by list {

        override fun addAll(index: Int, elements: Collection<Int>): Boolean =
            list.addAll(index.safely(), elements)

        override fun get(index: Int): Int =
            list[index.safely()]

        fun pickUpClockwiseOfTheCup(cup: Int): List<Int> {
            val index = list.indexOf(cup)
            // three cups that are immediately clockwise of the current cup
            return listOf(this[(index + 1).safely()], this[(index + 2).safely()], this[(index + 3).safely()])
        }

        fun selectDestinationCup(current: Int, pickUpList: List<Int>): Int {

            val min = list.minOf { it }

            // destination cup: the cup with a label equal to the current cup's label minus one
            var nextLabel = current - 1
            // If this would select one of the cups that was just picked up,
            // the crab will keep subtracting one until it finds a cup that wasn't just picked up.
            // If at any point in this process the value goes below the lowest value on any cup's label,
            // it wraps around to the highest value on any cup's label instead.
            while (nextLabel < min || nextLabel in pickUpList) {
                nextLabel = if (nextLabel < 1) this.maxOf { it } else --nextLabel
            }

            return nextLabel
        }

        override fun toString(): String =
            list.toString()

        private fun Int.safely(): Int =
            if (this < 0) (this % size + size) % size
            else this % size
    }

    private fun solveCircle(cups: Circle, nbMoves: Int): Circle {

        var index = 0
        var currentCup = cups.first()
        do {
            // pick up the three cups that are immediately clockwise of the current cup
            val pickUpList = cups.pickUpClockwiseOfTheCup(currentCup)
            // select a destination cup
            val destination = cups.selectDestinationCup(currentCup, pickUpList)
            // remove the three cups from the circle
            cups.removeAll(pickUpList)

            val insertAt = cups.indexOf(destination) + 1
            cups.addAll(insertAt, pickUpList)

            // select a new current cup: the cup which is immediately clockwise of the current cup
            currentCup = cups[cups.indexOf(currentCup) + 1]
        } while (++index < nbMoves)

        return cups
    }

    fun partOne(list: List<Int>, nbMoves: Int) {
        val circle = Circle(list.toMutableList())

        val cups = solveCircle(circle, nbMoves)

        println("cups: $cups")

        val index = cups.indexOf(1)
        val result = mutableListOf<Int>()
        for (i in 1 until cups.size) {
            result.add(cups[index + i])
        }

        println("Part one = ${result.joinToString("")}")
    }
}

private class Day23PartTwo {

    data class Cup(val id: Int, var next: Cup? = null)

    class LinkedCircle(intList: List<Int>) {

        private val cups = mutableMapOf<Int, Cup>()

        val size: Int
            get() = cups.size

        init {
            var firstCup: Cup? = null
            var previousCup: Cup? = null
            intList.forEach { id ->
                val newCup = Cup(id, previousCup)
                if (previousCup == null) { // first time
                    firstCup = newCup
                } else {
                    previousCup?.next = newCup
                }
                previousCup = newCup
                cups[id] = newCup
            }
            previousCup?.next = firstCup
        }

        operator fun get(id: Int) = cups[id]

        fun pickUpClockwiseOfTheCup(cup: Cup): Pair<Cup, Cup> = Pair(cup.next!!, cup.next?.next?.next!!)

        fun selectDestinationCup(current: Cup, pickUpList: Pair<Cup, Cup>, minMax: Pair<Int, Int>): Cup {
            val (min, max) = minMax

            val pickUpIdList =
                listOf(pickUpList.first.id, pickUpList.first.next!!.id, pickUpList.first.next!!.next!!.id)

            // destination cup: the cup with a label equal to the current cup's label minus one
            var nextLabel = current.id - 1
            // If this would select one of the cups that was just picked up,
            // the crab will keep subtracting one until it finds a cup that wasn't just picked up.
            // If at any point in this process the value goes below the lowest value on any cup's label,
            // it wraps around to the highest value on any cup's label instead.
            while (nextLabel < min || nextLabel in pickUpIdList) {
                nextLabel = if (nextLabel < 1) max else --nextLabel
            }

            return cups[nextLabel]!!
        }
    }

    /**
     * To debug
     */
    private fun display(cup: Cup, nbCupToDisplay: Int) {
        var currentCup = cup
        print("[")
        for (i in 0 until nbCupToDisplay) {
            print("${currentCup.id}, ")
            currentCup = currentCup.next!!
        }
        println("]")
    }

    private fun solveLinkedCircle(first: Int, cups: LinkedCircle, nbMoves: Int): LinkedCircle {

        val minMax = Pair(1, cups.size)
        var index = 0
        var currentCup = cups[first]!!
        do {
            // pick up the three cups that are immediately clockwise of the current cup
            val pickUpList = cups.pickUpClockwiseOfTheCup(currentCup)
            // select a destination cup
            val destination = cups.selectDestinationCup(currentCup, pickUpList, minMax)

            // remove the three cups from the circle
            currentCup.next = pickUpList.second.next
            // places the cups it just picked up so that they are immediately clockwise of the destination cup
            pickUpList.second.next = destination.next
            destination.next = pickUpList.first

            // select a new current cup: the cup which is immediately clockwise of the current cup
            currentCup = currentCup.next!!
        } while (++index <= nbMoves)

        return cups
    }

    fun partTwo(list: MutableList<Int>, nbMoves: Int) {

        // one million (1000000) in total
        val max = list.maxOf { it }
        for (i in (max + 1)..1_000_000) {
            list.add(i)
        }

        val circle = LinkedCircle(list)

        val cups = solveLinkedCircle(list.first(), circle, nbMoves)

        val result = cups[1]?.next?.id!!.toLong() * cups[1]?.next?.next?.id!!.toLong()

        println("Part two = $result")
    }
}