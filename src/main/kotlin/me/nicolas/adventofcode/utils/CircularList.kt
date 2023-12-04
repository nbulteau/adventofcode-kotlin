package me.nicolas.adventofcode.utils

class CircularList<out T>(private val list: List<T>) : List<T> by list {

    override fun get(index: Int): T =
        list[index.safely()]

    override fun listIterator(index: Int): ListIterator<T> =
        list.listIterator(index.safely())

    override fun subList(fromIndex: Int, toIndex: Int): List<T> =
        list.subList(fromIndex.safely(), toIndex.safely())

    override fun toString(): String =
        list.toString()

    private fun Int.safely(): Int =
        if (this < 0) (this % size + size) % size
        else this % size
}