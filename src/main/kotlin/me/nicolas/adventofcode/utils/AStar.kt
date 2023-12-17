package me.nicolas.adventofcode.utils

import java.util.*

// The function to get the neighbours of a point (the neighbours are the points we can reach from the current point)
typealias NeighbourFunction<T> = (T) -> Iterable<T>
// The function to get the cost between two points
typealias CostFunction<K> = (K, K) -> Int
// The function to check if we reached the end point (the function return true if we reached the end point)
typealias EndFunction<K> = (K) -> Boolean

/**
 * The vertex with the cost to reach it from the start point and the previous vertex
 */
data class SeenVertex<T>(val cost: Int, val prev: T?)

/**
 * The vertex with the score (the cost) to reach it from the start point (the previous vertex)
 */
data class ScoredVertex<T>(val vertex: T, val score: Int) : Comparable<ScoredVertex<T>> {
    override fun compareTo(other: ScoredVertex<T>): Int = (score).compareTo(other.score)
}

/**
 * The result of the graph search algorithm with the start point and the end point (if found) and the result
 */
class GraphSearchResult<T>(val start: T, val end: T?, private val result: Map<T, SeenVertex<T>>) {
    //  Return the cost of the vertex
    fun getScore(vertex: T) = result[vertex]?.cost ?: throw IllegalStateException("Result for $vertex not available")

    // Return the cost of the end point (if found)
    fun getScore() = end?.let { getScore(it) } ?: throw IllegalStateException("No path found")

    // Return the path from the start point to the end point (if found)
    fun getPath() = end?.let { getPath(it, emptyList()) } ?: throw IllegalStateException("No path found")

    // Return the set of seen points (the points we visited) during the search algorithm
    fun seen(): Set<T> = result.keys

    // Get the path from the end point to the start point using recursion (tail recursion)
    private tailrec fun getPath(endVertex: T, pathEnd: List<T>): List<T> {
        val previous = result[endVertex]?.prev

        return if (previous == null) {
            listOf(endVertex) + pathEnd
        } else {
            getPath(previous, listOf(endVertex) + pathEnd)
        }
    }
}

/**
 * Find the shortest path from the start point to the end point using A* algorithm
 * // https://www.redblobgames.com/pathfinding/a-star/introduction.html
 * // https://www.redblobgames.com/pathfinding/a-star/implementation.html
 */
fun <T> findShortestPath(
    startPoint: T, // The start point
    endFunction: EndFunction<T>, // The function to check if we reached the end point
    neighbours: NeighbourFunction<T>, // The function to get the neighbours of a point
    cost: CostFunction<T>, // The function to get the cost between two points
): GraphSearchResult<T> {
    val toVisit = PriorityQueue(listOf(ScoredVertex(startPoint, 0)))
    var endVertex: T? = null
    val seenPoints: MutableMap<T, SeenVertex<T>> = mutableMapOf(startPoint to SeenVertex(0, null))

    // Loop until we reach the end point or we visited all the points
    while (endVertex == null) {
        // Check if we visited all the points and we didn't find the end point
        if (toVisit.isEmpty()) {
            // We didn't find the end point
            return GraphSearchResult(startPoint, null, seenPoints)
        }

        // Get the next vertex to visit
        val (currentVertex, currentScore) = toVisit.remove()
        // Check if we reached the end point
        endVertex = if (endFunction(currentVertex)) currentVertex else null

        // Add all the neighbours of the current vertex to the queue
        val nextPoints = neighbours(currentVertex)
            // Filter out the points we already visited
            .filter { it !in seenPoints }
            // Add the cost to the current score
            .map { next -> ScoredVertex(next, currentScore + cost(currentVertex, next)) }
        // Add the points to the queue
        toVisit.addAll(nextPoints)
        // Add the points to the seen points
        seenPoints.putAll(nextPoints.associate { it.vertex to SeenVertex(it.score, currentVertex) })
    }

    // We found the end point
    return GraphSearchResult(startPoint, endVertex, seenPoints)
}