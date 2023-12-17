package me.nicolas.adventofcode.utils

import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.assertEquals

class CircularListTest {

    private val circularList = CircularList(listOf(1, 2, 3, 4, 5))

    @Test
    fun `get with positive index wraps around`() {
        assertEquals(1, circularList[5])
    }

    @Test
    fun `get with negative index accesses relative to end of list`() {
        assertEquals(5, circularList[-1])
        assertEquals(4, circularList[-2])
    }

    @Test
    fun `get with negative indexes wrap around`() {
        assertEquals(1, circularList[-10])
    }

    @Test
    fun `listIterator with positive index wraps around`() {
        assertThat(circularList.listIterator(6)).containsOnly(2, 3, 4, 5)
    }

    @Test
    fun `listIterator works with negative index`() {
        assertThat(circularList.listIterator(-2)).containsOnly(4, 5)
    }

    @Test
    fun `listIterator works with negative index that wraps around`() {
        assertThat(circularList.listIterator(-7)).containsOnly(4, 5)
    }

    @Test
    fun `subList with positive index wraps around`() {
        assertThat(circularList.subList(2, -1)).containsExactly(3, 4)
    }


    @Test
    fun `subList works with negative index`() {
        assertThat(circularList.subList(-3, -1)).containsExactly(3, 4)
    }

    @Test
    fun `subList works with negative index that wraps around`() {
        assertThat(circularList.subList(-3, -1)).containsExactly(3, 4)
    }

    @Test
    fun `subList works with a combination of positive and negative indexes`() {
        assertThat(circularList.subList(0, -3)).containsExactly(1, 2)
    }

    @Test
    fun `toString delegates to list implementation`() {
        assertEquals("[1, 2, 3, 4, 5]", circularList.toString())
    }
}