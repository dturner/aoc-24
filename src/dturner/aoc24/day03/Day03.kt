package dturner.aoc24.day03

import dturner.aoc24.utils.*

fun main() {

    val testInput = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

    check(part1(testInput) == 161)

    val input = readInput("Day03").first()
    part1(input).println()

    val testInput2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
    check(part2(testInput2) == 48)
    part2(input).println()
}

fun part1(input: String): Int {
    val regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
    val matches = regex.findAll(input)
    return matches.sumOf { match ->
        match.groupValues[1].toInt() * match.groupValues[2].toInt()
    }
}

fun part2(input: String): Int {

    // Match the commands: "do", "don't" and "mul(X,Y)".
    val regex = Regex("do(?!n't)|don't|mul\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)")
    val matches = regex.findAll(input)

    var areCommandsEnabled = true
    var runningTotal = 0

    matches.forEach { match ->
        val command = match.groupValues[0]
        when (command) {
            "do" -> areCommandsEnabled = true
            "don't" -> areCommandsEnabled = false
            else -> {
                if (areCommandsEnabled){
                    // Process multiply command.
                    val first = match.groupValues[1].toInt()
                    val second = match.groupValues[2].toInt()
                    val product = first * second
                    runningTotal += product
                }
            }
        }
    }
    return runningTotal
}