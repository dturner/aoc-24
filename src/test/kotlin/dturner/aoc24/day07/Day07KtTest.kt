package dturner.aoc24.day07

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import dturner.aoc24.day07.Operator.*

class Day07KtTest {

    @Test
    fun testGenerateOperatorPermutations() {

        val input1 = 1
        val operators = listOf(PLUS, MULTIPLY, CONCAT)
        val expected1 = listOf(listOf(PLUS), listOf(MULTIPLY), listOf(CONCAT))
        assertEquals(expected1, generateOperatorPermutations(input1, operators))

        val input2 = 2
        val expected2 = listOf(
            listOf(PLUS, PLUS),
            listOf(PLUS, MULTIPLY),
            listOf(PLUS, CONCAT),
            listOf(MULTIPLY, PLUS),
            listOf(MULTIPLY, MULTIPLY),
            listOf(MULTIPLY, CONCAT),
            listOf(CONCAT, PLUS),
            listOf(CONCAT, MULTIPLY),
            listOf(CONCAT, CONCAT)
        )
        assertEquals(expected2, generateOperatorPermutations(input2, operators))
    }
}