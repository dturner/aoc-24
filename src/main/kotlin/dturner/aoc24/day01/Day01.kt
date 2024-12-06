package dturner.aoc24.day01

import dturner.aoc24.utils.*
import kotlin.math.abs

fun main() {

    val testInput = """
3   4
4   3
2   5
1   3
3   9
3   3
""".split("\n").filter { it.isNotEmpty() }

    // Check that our test input gives the correct result.
    check(part1(testInput) == 11)

    // Run on the real input.
    val input = readInput("Day01")
    part1(input).println()

    check(part2(testInput) == 31)
    part2(input).println()
}

fun part1(input: List<String>): Int {

    /**
     * Pair up the smallest number in the left list with the smallest number in the right list,
     * then the second-smallest left number with the second-smallest right number, and so on.
     *
     * Within each pair, figure out how far apart the two numbers are; you'll need to add up all of those distances.
     */
    val (list1, list2) = input.map {
        val (first, second) = it.split("   ")
        first.toInt() to second.toInt()
    }.unzip()

    return list1.sorted().zip(list2.sorted()).sumOf {
        abs(it.first - it.second)
    }
}

fun part2(input: List<String>): Int {

    val (list1, list2) = input.map {
        val (first, second) = it.split("   ")
        first.toInt() to second.toInt()
    }.unzip()

    val occurrences = list2.groupingBy { it }.eachCount()
    return list1.sumOf { location -> (location * (occurrences[location] ?: 0))}
}
