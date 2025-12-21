package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.math.BigInteger

// --- Day 22: Slam Shuffle ---
// https://adventofcode.com/2019/day/22
fun main() {
    val data = readFileDirectlyAsText("/year2019/day22/data.txt")
    val day = Day22(2019, 22)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day22(year: Int, day: Int, title: String = "Slam Shuffle") : AdventOfCodeDay(year, day, title) {
    // Part One
    // This method uses a direct simulation of the shuffle instructions on a deck of size 10007.
    // It constructs a mutable list representing the deck in factory order (0..10006),
    // applies each instruction in order and finally returns the index/position of card 2019.
    // This is perfectly fine for the relatively small deck used in part one.
    fun partOne(data: String): Int {
        // deck size fixed for part one
        return shuffleAndFind(data, 10007, 2019)
    }

    // Part Two
    // The deck size and repetition count in part two are enormous, so simulation is impossible.
    // We model each shuffle instruction as an affine transformation modulo m:
    //   f(x) = a*x + b  (mod m)
    // where x is the original card position. Each instruction updates (a, b) as follows:
    // - deal into new stack: x -> -x - 1    => a' = -1 * a         ; b' = -1 * b - 1
    // - cut n:               x -> x - n      => a' = 1 * a          ; b' = b - n
    // - deal with inc n:     x -> n * x      => a' = n * a          ; b' = n * b
    // We compose all transformations to obtain a single (a, b) describing one full shuffle.
    // Repeating the shuffle 'times' times yields:
    //   f^times(x) = a^times * x + b * (1 - a^times) * inv(1 - a)  (mod m)
    // (special-cased when a == 1).
    // To find which card ends up at final position P (here P = 2020), we solve for x:
    //   x = inv(a^times) * (P - b^times)  (mod m)
    // All arithmetic is done with BigInteger modulo m to avoid overflow.
    fun partTwo(data: String): Long {
        val modulus = BigInteger.valueOf(119315717514047L)
        val times = BigInteger.valueOf(101741582076661L)

        val lines = data.lines().map { line -> line.trim() }.filter { line -> line.isNotEmpty() }

        // Represent the shuffle as f(x) = a*x + b (mod m)
        var a = BigInteger.ONE
        var b = BigInteger.ZERO

        for (line in lines) {
            when {
                line == "deal into new stack" -> {
                    // x -> -x - 1
                    val a2 = BigInteger.valueOf(-1).mod(modulus)
                    val b2 = BigInteger.valueOf(-1).mod(modulus)
                    a = a2.multiply(a).mod(modulus)
                    b = (a2.multiply(b).add(b2)).mod(modulus)
                }
                line.startsWith("cut ") -> {
                    // x -> x - n
                    val n = line.removePrefix("cut ").toBigInteger()
                    val a2 = BigInteger.ONE
                    val b2 = n.negate().mod(modulus)
                    a = a2.multiply(a).mod(modulus)
                    b = (a2.multiply(b).add(b2)).mod(modulus)
                }
                line.startsWith("deal with increment ") -> {
                    // x -> n * x
                    val inc = line.removePrefix("deal with increment ").toBigInteger()
                    val a2 = inc.mod(modulus)
                    val b2 = BigInteger.ZERO
                    a = a2.multiply(a).mod(modulus)
                    b = (a2.multiply(b).add(b2)).mod(modulus)
                }
                else -> {
                    // ignore
                }
            }
        }

        // Compute f^times(x) = a^times * x + b * (1 - a^times) * inv(1 - a)
        val aN = a.modPow(times, modulus)
        val bN = if (a == BigInteger.ONE) {
            // f(x) = x + b -> repeated times gives x + times*b
            b.multiply(times).mod(modulus)
        } else {
            val one = BigInteger.ONE
            val numerator = one.subtract(aN).mod(modulus)
            val denom = one.subtract(a).mod(modulus)
            val invDenom = denom.modInverse(modulus)
            b.multiply(numerator).mod(modulus).multiply(invDenom).mod(modulus)
        }

        // We want the card that ends up at position 2020, i.e. find x such that f^times(x) = 2020
        // Solve x = inv(aN) * (2020 - bN) mod m
        val targetPos = BigInteger.valueOf(2020L)
        val invAN = aN.modInverse(modulus)
        val x = invAN.multiply(targetPos.subtract(bN)).mod(modulus)

        return x.longValueExact()
    }

    // Public helper that returns the full deck after applying the instructions (useful for tests)
    fun shuffle(data: String, deckSize: Int): List<Int> {
        // Initialize deck in factory order 0..deckSize-1
        var deck = MutableList(deckSize) { it }
        val lines = data.lines().map { line -> line.trim() }.filter { line -> line.isNotEmpty() }

        for (line in lines) {
            when {
                line == "deal into new stack" -> deck.reverse()
                line.startsWith("cut ") -> {
                    val n = line.removePrefix("cut ").toInt()
                    val nn = ((n % deckSize) + deckSize) % deckSize
                    deck = (deck.drop(nn) + deck.take(nn)).toMutableList()
                }
                line.startsWith("deal with increment ") -> {
                    val inc = line.removePrefix("deal with increment ").toInt()
                    val newDeck = MutableList(deckSize) { -1 }
                    var pos = 0
                    for (card in deck) {
                        newDeck[pos] = card
                        pos = (pos + inc) % deckSize
                    }
                    deck = newDeck
                }

                else -> {
                    // ignore unknown lines or blank lines
                }
            }
        }

        return deck
    }

    // Returns the index of `target` after shuffling
    fun shuffleAndFind(data: String, deckSize: Int, target: Int): Int {
        val deck = shuffle(data, deckSize)
        return deck.indexOf(target)
    }
}