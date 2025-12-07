package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.*
import kotlin.math.abs

/**
 * Inspired by Alexandre Grison code : https://github.com/agrison/advent-of-code/blob/master/src/main/kotlin/me/grison/aoc/y2021/Day23.kt
 */
fun main() {

    val dataPart1 = readFileDirectlyAsText("/year2021/day23/data-part1.txt")
    val dataPart2 = readFileDirectlyAsText("/year2021/day23/data-part2.txt")

    val day = Day23(2023, 23, "Amphipod")
    prettyPrintPartOne { day.partOne(dataPart1) }
    prettyPrintPartTwo { day.partTwo(dataPart2) }
}

private typealias Room = List<Int>
private typealias Hallway = List<Int?>

class Day23(year: Int, day: Int, title: String = "Amphipod") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String) = organizeAmphipods(loadList(data.lines()), 2)

    fun partTwo(data: String) = organizeAmphipods(loadList(data.lines()), 4)

    private enum class Amphipod { A, B, C, D }

    private val roomExits = listOf(2, 4, 6, 8)

    private fun organizeAmphipods(rooms: List<List<Int>>, roomSize: Int): Int {
        val queue = PriorityQueue<Situation>()
        queue.add(Situation(roomSize, 0, rooms))

        val visited = mutableSetOf<Pair<List<Room>, Hallway>>()
        while (queue.isNotEmpty()) {
            val situation = queue.poll()
            if (situation.isOrganized()) {
                return situation.energy
            }

            if (situation.currentState() in visited) {
                continue
            }

            visited.add(situation.currentState())

            // first move amphipods from rooms to hallway
            for ((roomNumber, currentRoom) in situation.rooms.withIndex()) {
                // if room is not empty and not all amphipods are in the room
                if (currentRoom.isNotEmpty() && !currentRoom.all { it == roomNumber }) {
                    // move last amphipod from room to hallway
                    val amphipod = currentRoom.last()
                    val left = (roomExits[roomNumber] downTo 0) - roomExits.toSet()
                    val right = (roomExits[roomNumber]..10) - roomExits.toSet()
                    for (direction in listOf(left, right)) {
                        for (position in direction) {
                            if (situation.hallwayIsOccupied(position)) {
                                break
                            }
                            val steps = roomSize - currentRoom.size + abs(roomExits[roomNumber] - position) + 1
                            val newSituation = Situation(
                                size = roomSize,
                                energy = situation.energy + steps * cost(amphipod),
                                rooms = situation.rooms.update(roomNumber, currentRoom.dropLast(1)),
                                hallway = situation.hallway.update(position, amphipod)
                            )

                            if (newSituation.currentState() !in visited) {
                                queue.add(newSituation)
                            }
                        }
                    }
                }
            }

            // then move amphipods back to room where possible
            for ((index, amphipod) in situation.hallway.withIndex()) {
                if (cannotEnterRoom(situation, index, amphipod)) {
                    continue
                }

                val steps = roomSize - situation.occupants(amphipod!!) + abs(roomExits[amphipod] - index)
                val newSituation = Situation(
                    size = roomSize,
                    energy = situation.energy + steps * cost(amphipod),
                    rooms = situation.rooms.update(amphipod, situation.rooms[amphipod] + amphipod),
                    hallway = situation.hallway.update(index, null)
                )

                if (newSituation.currentState() !in visited) {
                    queue.add(newSituation)
                }
            }
        }

        return -1
    }

    private fun cannotEnterRoom(situation: Situation, index: Int, amphipod: Int?): Boolean {
        return situation.hallwayIsFree(index) ||
                index < roomExits[amphipod!!] && !situation.hallwayIsFree(index + 1, roomExits[amphipod]) ||
                index > roomExits[amphipod] && !situation.hallwayIsFree(roomExits[amphipod] + 1, index) ||
                situation.rooms[amphipod].any { it != amphipod }
    }

    private fun cost(amphipod: Int): Int {
        return when (Amphipod.entries[amphipod]) {
            Amphipod.A -> 1
            Amphipod.B -> 10
            Amphipod.C -> 100
            Amphipod.D -> 1000
        }
    }

    private data class Situation(
        val size: Int,
        val energy: Int,
        val rooms: List<Room>,
        val hallway: Hallway = List(11) { null },
    ) : Comparable<Situation> {
        override fun compareTo(other: Situation) = energy.compareTo(other.energy)
        fun currentState() = Pair(rooms, hallway)
        fun occupants(amphipod: Int) = rooms[amphipod].size
        fun hallwayIsOccupied(index: Int) = hallway[index] != null
        fun hallwayIsFree(index: Int) = hallway[index] == null
        fun hallwayIsFree(from: Int, to: Int) = hallway.subList(from, to).all { it == null }
        fun isOrganized(): Boolean {
            return hallway.all { it == null } &&
                    rooms.all { it.size == size } &&
                    rooms.withIndex().all { it.value.isOrganized(it.index) }
        }

        private fun Room.isOrganized(roomNumber: Int) = isNotEmpty() && all { it == roomNumber }
    }

    private fun <T> List<T>.update(i: Int, value: T): List<T> {
        val copy = this.toMutableList()
        copy[i] = value
        return copy
    }

    private fun loadList(inputs: List<String>): List<Room> {
        return inputs
            .map { line -> line.filter { char -> char in 'A'..'D' }.map { it.toString() } }
            .filter { strings -> strings.isNotEmpty() }
            .transpose()
            .map { strings -> strings.reversed() }
            .map { strings -> strings.map { Amphipod.valueOf(it).ordinal } }
    }

    private fun List<List<String>>.transpose(): List<List<String>> {
        val rows = this.map { it.size }.maxByOrNull { it } ?: -1
        val iterators = this.map { it.iterator() }

        return (0 until rows).map { iterators.filter { it.hasNext() }.map { it.next() } }
    }
}