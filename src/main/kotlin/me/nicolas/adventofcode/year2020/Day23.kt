package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 23: Crab Cups ---
// https://adventofcode.com/2020/day/23
fun main() {
    val data = "872495136"
    val day = Day23(2020, 23, "Crab Cups")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day23(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String, nbMoves: Int = 100): Long {
        val list = data.map { it.toString().toInt() }
        val circle = Circle(list.toMutableList())
        val cups = solveCircle(circle, nbMoves)
        val index = cups.indexOf(1)
        val result = mutableListOf<Int>()
        for (i in 1 until cups.size) {
            result.add(cups[index + i])
        }
        return result.joinToString("").toLong()
    }

    fun partTwo(data: String, nbMoves: Int = 10_000_000): Long {
        val list = data.map { it.toString().toInt() }.toMutableList()
        // one million (1_000_000) in total
        val max = list.maxOf { it }
        for (i in (max + 1)..1_000_000) {
            list.add(i)
        }
        val circle = LinkedCircle(list)
        val cups = solveLinkedCircle(list.first(), circle, nbMoves)
        return cups[1]?.next?.id!!.toLong() * cups[1]?.next?.next?.id!!.toLong()
    }

    private class Circle(private val list: MutableList<Int>) : MutableList<Int> by list {
        override fun addAll(index: Int, elements: Collection<Int>): Boolean = list.addAll(index.safely(), elements)
        override fun get(index: Int): Int = list[index.safely()]
        fun pickUpClockwiseOfTheCup(cup: Int): List<Int> {
            val index = list.indexOf(cup)
            return listOf(this[(index + 1).safely()], this[(index + 2).safely()], this[(index + 3).safely()])
        }

        fun selectDestinationCup(current: Int, pickUpList: List<Int>): Int {
            val min = list.minOf { it }
            var nextLabel = current - 1
            while (nextLabel < min || nextLabel in pickUpList) {
                nextLabel = if (nextLabel < 1) this.maxOf { it } else --nextLabel
            }
            return nextLabel
        }

        override fun toString(): String = list.toString()
        private fun Int.safely(): Int = if (this < 0) (this % size + size) % size else this % size
    }

    private fun solveCircle(cups: Circle, nbMoves: Int): Circle {
        var index = 0
        var currentCup = cups.first()
        do {
            val pickUpList = cups.pickUpClockwiseOfTheCup(currentCup)
            val destination = cups.selectDestinationCup(currentCup, pickUpList)
            cups.removeAll(pickUpList.toSet())
            val insertAt = cups.indexOf(destination) + 1
            cups.addAll(insertAt, pickUpList)
            currentCup = cups[cups.indexOf(currentCup) + 1]
        } while (++index < nbMoves)
        return cups
    }

    private data class Cup(val id: Int, var next: Cup? = null)

    private class LinkedCircle(intList: List<Int>) {
        private val cups = mutableMapOf<Int, Cup>()
        val size: Int get() = cups.size

        init {
            var firstCup: Cup? = null
            var previousCup: Cup? = null
            intList.forEach { id ->
                val newCup = Cup(id, previousCup)
                if (previousCup == null) {
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
            val pickUpIdList = listOf(pickUpList.first.id, pickUpList.first.next!!.id, pickUpList.first.next!!.next!!.id)
            var nextLabel = current.id - 1
            while (nextLabel < min || nextLabel in pickUpIdList) {
                nextLabel = if (nextLabel < 1) max else --nextLabel
            }
            return cups[nextLabel]!!
        }
    }

    private fun solveLinkedCircle(first: Int, cups: LinkedCircle, nbMoves: Int): LinkedCircle {
        val minMax = Pair(1, cups.size)
        var index = 0
        var currentCup = cups[first]!!
        do {
            val pickUpList = cups.pickUpClockwiseOfTheCup(currentCup)
            val destination = cups.selectDestinationCup(currentCup, pickUpList, minMax)
            currentCup.next = pickUpList.second.next
            pickUpList.second.next = destination.next
            destination.next = pickUpList.first
            currentCup = currentCup.next!!
        } while (++index < nbMoves) // Corrected from <= to <
        return cups
    }
}