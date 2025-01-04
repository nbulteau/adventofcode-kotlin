package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText


// https://adventofcode.com/2018/day/19
fun main() {
    val data = readFileDirectlyAsText("/year2018/day19/data.txt")
    val day = Day19()
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day19(year: Int = 2018, day: Int = 19, title: String = "Go With The Flow") : AdventOfCodeDay(year, day, title) {

    private data class Instruction(val opcode: String, val a: Int, val b: Int, val c: Int)

    private class Device(
        val ipRegister: Int,
        val instructions: List<Instruction>,
        val registers: IntArray = IntArray(6),
    ) {

        fun executeProgram(maxCount: Int = Int.MAX_VALUE) {
            var count = 0
            while (registers[ipRegister] in instructions.indices && count++ < maxCount) {
                val instruction = instructions[registers[ipRegister]]
                executeInstruction(instruction)
                //println("ip=${registers[ipRegister]} [${registers.joinToString()}] $instruction")
                registers[ipRegister]++
            }
        }

        // Execute the instruction and update the registers
        private fun executeInstruction(instruction: Instruction) {
            with(instruction) {
                registers[c] = when (opcode) {
                    "addr" -> registers[a] + registers[b]
                    "addi" -> registers[a] + b
                    "mulr" -> registers[a] * registers[b]
                    "muli" -> registers[a] * b
                    "banr" -> registers[a] and registers[b]
                    "bani" -> registers[a] and b
                    "borr" -> registers[a] or registers[b]
                    "bori" -> registers[a] or b
                    "setr" -> registers[a]
                    "seti" -> a
                    "gtir" -> if (a > registers[b]) 1 else 0
                    "gtri" -> if (registers[a] > b) 1 else 0
                    "gtrr" -> if (registers[a] > registers[b]) 1 else 0
                    "eqir" -> if (a == registers[b]) 1 else 0
                    "eqri" -> if (registers[a] == b) 1 else 0
                    "eqrr" -> if (registers[a] == registers[b]) 1 else 0
                    else -> throw IllegalArgumentException("Unknown opcode: $opcode")
                }
            }
        }
    }

    fun partOne(data: String): Int {
        val device = parseInput(data)

        device.executeProgram()

        return device.registers[0]
    }

    /**
     *    The program calculates the sum of all factors of a big number : https://www.reddit.com/r/adventofcode/comments/a8dkz2/2018_days_16_19_21_i_made_an_elfcode_decompiler/
     *    It initialises R5 with a huge number, then uses an extremely inefficient algorithm to find numbers which are factors of that number,
     *    adding the factors up as it goes.
     *
     *    The program is equivalent to:
     *      goto 17;
     *  1:  c = 1;
     *      do {
     *          e = 1;
     *          do {
     *              b = c * e;
     *              if b == f {
     *                  a += c;
     *              }
     *              e += 1;
     *          } while e <= f;
     *          c += 1;
     *      } while c <= f;
     *      ip *= ip; // causes a jump to outside the address range and terminates the program => Halt
     * 17:  f += 2;     f = (2 * 2) * 19 * 11 = 836
     *      f *= f;
     *      f *= ip;
     *      f *= 11;
     *      b += 3;     b = 3 * 22 + 12 = 78
     *      b *= ip;
     *      b += 12;
     *      f += b;     f = 914
     *      ip += a;
     *      goto 1;
     *      b = ip;
     *      b *= ip;
     *      b += ip;    b = 27 * 28 + 29 = 785
     *      b *= ip;
     *      b *= 14;
     *      b *= ip;    b = 785 * 30 * 14 * 32 = 105550400
     *      f += b;     f = 105551314 (=> BigNumber)
     *      a = 0;
     *      goto 1;
     *
     */
    fun partTwo(data: String): Int {
        val device = parseInput(data)
        device.registers[0] = 1

        device.executeProgram(10000)

        // The big number is the value of register 5 at the end of the program
        val bigNumber = device.registers[5]
        var sum = 0
        for (i in 1..bigNumber) {
            if (bigNumber % i == 0) {
                println(i)
                sum += i
            }
        }

        return sum
    }

    private fun parseInput(data: String): Device {
        val lines = data.lines()
        val ipRegister = lines.first().split(" ")[1].toInt()
        val instructions = lines.drop(1).map { line ->
            val parts = line.split(" ")
            Instruction(parts[0], parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
        }

        return Device(ipRegister, instructions)
    }
}

