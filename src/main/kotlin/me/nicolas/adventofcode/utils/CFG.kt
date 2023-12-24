package me.nicolas.adventofcode.utils

// Lagrange's Interpolation
internal object GFG {
    // function to interpolate the given data points using Lagrange's formula
    // xi corresponds to the new data point whose value is to be obtained
    fun interpolate(data: Array<Data>, xi: Long): Long {
        var result = 0L // Initialize result
        for (i in data.indices) {
            // Compute individual terms of above formula
            var term = data[i].y
            for (j in data.indices) {
                if (j != i) {
                    term = term * (xi - data[j].x) / (data[i].x - data[j].x)
                }
            }

            // Add current term to result
            result += term
        }
        return result
    }

    // To represent a data point : corresponding to x and y = f(x)
    internal class Data(var x: Long, var y: Long)
}

fun main() {
    val f = arrayOf(
        GFG.Data(100,6536, ),
        GFG.Data(500,167004, ),
        //GFG.Data(1000, 668697, ),
        GFG.Data(5000, 16733044, )
    )

    // Using the interpolate function to obtain
    println("Value of f(1000) is : " + GFG.interpolate(f, 1000))
    println("Value of f(5000) is : " + GFG.interpolate(f, 5000))
}
