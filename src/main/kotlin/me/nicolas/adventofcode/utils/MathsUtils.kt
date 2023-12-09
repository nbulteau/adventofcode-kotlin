package me.nicolas.adventofcode.utils

/**
 * GCD (Greatest Common Divisor) or HCF (Highest Common Factor)
 * of two numbers is the largest number that divides both of them.
 */
private fun gcd(a: Long, b: Long): Long {
    var x = a
    var y = b
    while (y > 0) {
        val temp = y
        y = x % y // % is remainder
        x = temp
    }
    return x
}

/**
 * LCM (Least Common Multiple) of two numbers is the smallest number that is divisible by both.
 */
fun lcm(a: Long, b: Long) = a * (b / gcd(a, b))

fun List<Long>.lcm(): Long {
    return this.reduce { acc, i -> (acc * i) / gcd(acc, i) }.toLong()
}

