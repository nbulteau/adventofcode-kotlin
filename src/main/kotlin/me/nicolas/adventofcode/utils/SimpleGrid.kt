package me.nicolas.adventofcode.utils

/**
 * Represents a 2D coordinate plane.
 *
 * Each row must be the same length and there must be at least one row
 */
class SimpleGrid<T>(
    private val matrix: Array<Array<T>>
) {

    companion object {
        // Create a SimpleGrid (SimpleGrid<Char>) from the data string (each line is a row)
        fun of(data: String): SimpleGrid<Char> {
            return SimpleGrid(data.lines().map { it.toList().toTypedArray() }.toTypedArray())
        }

        fun intGrid(data: String): SimpleGrid<Int> {
            return SimpleGrid(data.lines().map { it.toList().map { c -> c.toString().toInt() }.toTypedArray() }
                .toTypedArray())
        }
    }

    init {
        assert(matrix.distinctBy { it.size }.size == 1) { "All rows must be the same length" }
        assert(matrix.isNotEmpty()) { "Grid must have at least one row" }
    }

    private val data: MutableMap<Point, T> = buildMap {
        matrix.forEachIndexed { y: Int, row -> row.forEachIndexed { x: Int, item -> set(Point(y, x), item) } }
    }.toMutableMap()

    /**
     * A map containing every point in this grid and its
     * corresponding item
     */
    val entries: Map<Point, T> = data

    val rows = matrix.indices
    val columns = matrix.first().indices

    var height = matrix.size
    var width = matrix.first().size

    val points: Set<Point>
        get() = data.keys

    val items: List<T>
        get() = data.values.toList()

    fun findOne(predicate: (T) -> Boolean) = points.first { point ->
        predicate(get(point))
    }

    fun findAll(t: T): List<Point> =
        entries.filter { (_, value) ->
            value === t
        }.keys.toList()

    operator fun get(point: Point): T {
        if (point !in this) throw IndexOutOfBoundsException("Point $point not within bounds of grid: ($height, $width)")
        return data[point]!!
    }

    fun getOrNull(point: Point): T? = data[point]

    fun getOrDefault(point: Point, default: T): T? = data[point] ?: default


    operator fun set(point: Point, item: T) {
        if (point !in this) {
            throw IndexOutOfBoundsException("Point $point not within bounds of grid: ($height, $width)")
        }
        data[point] = item
    }


    operator fun contains(point: Point) = point.y in rows && point.x in columns

    override fun toString(): String {
        return data.keys.chunked(width).joinToString("\n") { row ->
            row.map(::get).joinToString("")
        }
    }


}

fun SimpleGrid(size: Int) = SimpleGrid(Array(size) { Array(size) { '.' } })

fun SimpleGrid(width: Int, height: Int) = SimpleGrid(Array(height) { Array(width) { '.' } })