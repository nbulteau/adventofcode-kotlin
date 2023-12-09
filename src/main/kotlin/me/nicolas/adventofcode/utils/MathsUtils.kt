package me.nicolas.adventofcode.utils

/**
 * GCD (Greatest Common Divisor) or HCF (Highest Common Factor)
 * of two numbers is the largest number that divides both of them.
 */
tailrec fun Long.gcd(other: Long): Long =
    if(other == 0L) this
    else other.gcd(this % other)

/**
 * LCM (Least Common Multiple) of two numbers is the smallest number that is divisible by both.
 * The formula to find the LCM of some numbers a and b is (a*b) / gcd(a, b) where GCD is the Greatest Common Divisor.
 */
fun Long.lcm(other: Long): Long =
    (this * other) / this.gcd(other)

fun List<Long>.lcm(): Long {
    return this.reduce { acc, i -> (acc * i) / acc.gcd(i) }.toLong()
}

