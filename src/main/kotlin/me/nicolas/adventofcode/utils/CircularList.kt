package me.nicolas.adventofcode.utils

/**
 * CircularList is a wrapper around the List interface that provides circular (or cyclic) characteristics.
 * In a CircularList, after the last element, the next element is the first element, and similarly, before the first element, the previous element is the last element.
 * This class is read-only as it only overrides read operations from the List interface.
 */
class CircularList<out T>(private val list: List<T>) : List<T> by list {

    /**
     * Overrides the get function to provide a circular characteristic.
     * It calculates the safe index by using the modulo operator, which effectively makes the list circular.
     */
    override fun get(index: Int): T =
        list[index.safely()]

    /**
     * Overrides the listIterator function to start the iterator at a safe index in the circular list.
     */
    override fun listIterator(index: Int): ListIterator<T> =
        list.listIterator(index.safely())

    /**
     * Overrides the subList function to provide a sublist from the circular list.
     * It calculates the safe start and end indices to create the sublist.
     */
    override fun subList(fromIndex: Int, toIndex: Int): List<T> =
        list.subList(fromIndex.safely(), toIndex.safely())

    override fun toString(): String =
        list.toString()

    /**
     * Private helper function to calculate a safe index in the list.
     * If the index is negative, it calculates the corresponding positive index.
     * If the index is greater than the size of the list, it wraps around to the start of the list.
     */
    private fun Int.safely(): Int =
        if (this < 0) (this % size + size) % size
        else this % size
}