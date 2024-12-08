package dturner.aoc24.day08

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day08KtTest {

    @Test
    fun testFindAntinodesPart2() {

        val input = Pair(Point(0, 0), Point(3, 1))
        val mapBounds = Point(10,10)

        val expected = listOf(
            Point(0, 0),
            Point(3, 1),
            Point(6, 2),
            Point(9, 3),
        )

        assertEquals(expected, findAntinodesPart2(input, mapBounds))
    }

    @Test
    fun testFindAntinodes() {

        val input = Pair(Point(4, 3), Point(5, 5))
        val expected = listOf(Point(3, 1), Point(6, 7))
        val mapBounds = Point(10,10)

        assertEquals(expected, findAntinodesPart1(input, mapBounds))
    }

    @Test
    fun testFindPairs() {

        val input = listOf('A', 'B', 'C')

        val expected = listOf(
            Pair('A', 'B'),
            Pair('A', 'C'),
            Pair('B', 'C'),
        )

        assertEquals(expected, findPairs(input))

    }

    @Test
    fun testFindAntennas() {

        val input = """
            A..
            A..
            B..
            ..B
        """.trimIndent().split("\n").filter { it.isNotEmpty() }

        val expected = mapOf(
            'A' to listOf(Point(0,0), Point(0,1)),
            'B' to listOf(Point(0, 2), Point(2, 3))
        )

        assertEquals(expected, findAntennas(input))
    }


}