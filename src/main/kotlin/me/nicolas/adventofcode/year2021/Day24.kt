package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.math.pow
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

/**
 *
 * It repeats the same instruction block (pattern of 18 lines) 14 times with each input.
 *
 * inp w
 * ---
 * w = input[index]
 * ---
 * mul x 0
 * add x z
 * mod x 26
 * ---
 * x = z % 26
 * ---
 * div z 1
 * ---
 * z /= {a=1}
 * ---
 * add x 15
 * eql x w
 * eql x 0
 * ---
 * x = if ((x + {b=15}) == w) 0 else 1
 * ---
 * mul y 0
 * add y 25
 * mul y x
 * add y 1
 * mul z y
 * ---
 * z *= (25 * x + 1)
 * ---
 * mul y 0
 * add y w
 * add y 9
 * mul y x
 * add z y
 * ---
 * z += (w + {c=9}) * x
 * ---
 *
 * If we group instructions
 * ---
 * x = if ((z % 26 + {b}) == w) 0 else 1
 * z /= {a}
 * z *= (25 * x + 1)
 * z += (w + {c}) * x
 * ---
 */

// https://adventofcode.com/2021/day/24
@ExperimentalTime
fun main() {

    val training = readFileDirectlyAsText("/year2021/day24/training.txt")
    val data = readFileDirectlyAsText("/year2021/day24/data.txt")

    val inputs = data.split("\n")

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day24().partOne(inputs) })

    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day24().partTwo(inputs) })
}

private class Day24 {
    companion object {
        private const val blockSize = 18
        private const val nbDigits = 14
    }

    fun partOne(inputs: List<String>): Long {
        val blocks = parseInput(inputs)
        val nomad = Nomad(blocks)

        return nomad.search(9 downTo 1)
    }

    fun partTwo(inputs: List<String>): Long {
        val blocks = parseInput(inputs)
        val nomad = Nomad(blocks)

        return nomad.search(1..9)
    }

    data class Block(private val a: Int, private val b: Int, private val c: Int) {

        /**
         * x = if ((z % 26 + {b}) == w) 0 else 1
         * z /= {a}
         * z *= (25 * x + 1)
         * z += (w + {c}) * x
         */
        fun processBlock(w: Long, z: Long): Long {
            val x = if ((z % 26 + this.b) == w) 0 else 1
            var toReturn = z / this.a
            toReturn *= (25 * x + 1)
            toReturn += (w + this.c) * x

            return toReturn
        }

        fun processBlockV2(w: Long, z: Long): Long =
            if (z % 26 + this.b == w) {
                z / this.a
            } else {
                ((z / this.a) * 26) + w + this.c
            }
    }

    fun parseInput(input: List<String>): List<Block> {

        return input.windowed(blockSize, blockSize).map { lines ->
            Block(
                lines[4].split(" ").last().toInt(),
                lines[5].split(" ").last().toInt(),
                lines[15].split(" ").last().toInt()
            )
        }
    }

    class Nomad(private val blocks: List<Block>) {

        private val cache: Array<MutableSet<Long>> = Array(nbDigits) { mutableSetOf() }

        fun search(digits: IntProgression, index: Int = 0, z: Long = 0): Long {
            if (index == blocks.size) {
                return if (z == 0L) 0L else -1L
            }
            if (cache[index].contains(z)) {
                return -1L
            }
            for (w in digits) {
                val newZ = blocks[index].processBlockV2(w.toLong(), z)
                val output = search(digits, index + 1, newZ)
                if (output != -1L) {
                    return w * 10.toDouble().pow(nbDigits - index - 1).toLong() + output
                }
            }

            cache[index].add(z)
            return -1L
        }
    }
}