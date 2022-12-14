package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.AdventOfCodeDay
import me.nicolas.adventofcode.prettyPrintPartOne
import me.nicolas.adventofcode.prettyPrintPartTwo
import me.nicolas.adventofcode.readFileDirectlyAsText

fun main() {
    val training = readFileDirectlyAsText("/year2022/day13/training.txt")
    val data = readFileDirectlyAsText("/year2022/day13/data.txt")

    val inputs = data.split("\n")

    val day = Day13("--- Day 13: Distress Signal ---", "https://adventofcode.com/2022/day/13")
    prettyPrintPartOne { day.partOne(inputs) }
    prettyPrintPartTwo { day.partTwo(inputs) }
}

private class Day13(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    fun partOne(inputs: List<String>): Int {
        val packets = inputs.filter { it.isNotBlank() }.map { it.toPacket() }

        return packets.chunked(2) { (first, second) -> first <= second }
            .withIndex()
            .filter { it.value }
            .sumOf { it.index + 1 }
    }

    fun partTwo(inputs: List<String>): Int {
        val startDividerPacket = Packet.PacketList(listOf(Packet.PacketList(listOf(Packet.PacketInteger(2)))))
        val endDividerPacket = Packet.PacketList(listOf(Packet.PacketList(listOf(Packet.PacketInteger(6)))))
        val packets = inputs.filter { it.isNotBlank() }.map { it.toPacket() }

        var start = 1
        var end = 2
        for (packet in packets) {
            if (packet <= startDividerPacket) {
                start++
            }
            if (packet <= endDividerPacket) {
                end++
            }
        }
        return start * end
    }

    private sealed class Packet : Comparable<Packet> {
        data class PacketInteger(val value: Int) : Packet() {
            override fun compareTo(other: Packet): Int = when (other) {
                is PacketInteger -> this.value compareTo other.value
                // If exactly one value is an integer, convert the integer to a list which contains that integer as its only value
                is PacketList -> PacketList(listOf(this)) compareTo other
            }
        }

        data class PacketList(val list: List<Packet>) : Packet() {
            override fun compareTo(other: Packet): Int {
                return when (other) {
                    // If exactly one value is an integer, convert the integer to a list which contains that integer as its only value
                    is PacketInteger -> this compareTo PacketList(listOf(other))
                    is PacketList -> {
                        val iterator1 = this.list.iterator()
                        val iterator2 = other.list.iterator()
                        // If the lists are the same length and no comparison makes a decision about the order, continue checking the next part of the input.
                        while (iterator1.hasNext() && iterator2.hasNext()) {
                            val comparison = iterator1.next() compareTo iterator2.next()
                            if (comparison != 0) {
                                return comparison
                            }
                        }
                        // If the left list runs out of items first, the inputs are in the right order. If the right list runs out of items first, the inputs are not in the right order.
                        if (iterator1.hasNext() && !iterator2.hasNext()) {
                            1
                        } else  if (!iterator1.hasNext() && !iterator2.hasNext()){
                            0
                        } else { // (!iterator1.hasNext() && iterator2.hasNext())
                            -1
                        }
                    }
                }
            }
        }
    }

    private fun recursiveParser(string: String, startIndex: Int = 0): Pair<Int, Packet> {
        return if (string[startIndex] == '[') {
            var index = startIndex + 1
            val list = buildList {
                while (string[index] != ']') {
                    val (endIndex, value) = recursiveParser(string, index)
                    this.add(value)
                    index = if (string[endIndex] == ',') {
                        endIndex + 1
                    } else {
                        endIndex
                    }
                }
            }
            Pair(index + 1, Packet.PacketList(list))
        } else {
            var index = startIndex + 1
            while (index < string.length && string[index] in '0'..'9') {
                index++
            }
            Pair(index, Packet.PacketInteger(string.substring(startIndex, index).toInt()))
        }
    }

    private fun String.toPacket(): Packet {
        val (_, packet) = recursiveParser(this)
        return packet
    }
}


