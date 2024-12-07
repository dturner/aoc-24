package dturner.aoc24.day07

import dturner.aoc24.utils.readInput
import dturner.aoc24.day07.Operator.*
import kotlin.math.pow

fun main() {

    val testInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().split("\n").filter { it.isNotEmpty() }
    val part1TestResult = part1(testInput)
    println(part1TestResult)
    check(part1TestResult == 3749L)
    val input = readInput("Day07")
    val part1Result = part1(input)
    println(part1Result)
    check(part1Result == 303876485655)

    val part2TestResult = part2(testInput)
    println(part2TestResult)
    check(part2TestResult == 11387L)

    val part2Result = part2(input)
    println(part2Result)
    check(part2Result == 146111650210682L)
}

fun part1(input: List<String>): Long {
    val equations = parseEquations(input)
    return getValidResultSum(equations, listOf(PLUS, MULTIPLY))
}

fun part2(input: List<String>): Long {
    val equations = parseEquations(input)
    return getValidResultSum(equations, listOf(PLUS, MULTIPLY, CONCAT))
}

private fun getValidResultSum(equations: List<Equation>, operatorsToUse: List<Operator>): Long {
    var validResultSum = 0L

    for (equation in equations){

        // Determine whether there's a valid set of operators that can be used to get the expectedResult.
        val numOperators = equation.inputValues.size - 1

        // TODO: Doing this for every equation is inefficient.
        //  The permutations could be calculated at the start when we know what the max number of input values.
        val operatorPerms = generateOperatorPermutations(numOperators, operatorsToUse)
        var isResultValid = false

        for (permutation in operatorPerms){
            var result = equation.inputValues.first()

            permutation.forEachIndexed{ operatorIndex, operator ->
                when (operator){
                    PLUS -> result += equation.inputValues[operatorIndex+1]
                    MULTIPLY -> result *= equation.inputValues[operatorIndex+1]
                    // TODO: This is super inefficient. Better to keep input values as strings.
                    CONCAT -> result = (result.toString() + equation.inputValues[operatorIndex+1].toString()).toLong()
                }
            }

            if (result == equation.expectedResult){
                isResultValid = true
                break
            }
        }

        if (isResultValid) validResultSum += equation.expectedResult
    }
    return validResultSum
}

private fun parseEquations(input: List<String>) = input.map {
    val parts = it.split(":")
    val inputValues = parts[1].trim().split(" ").map { it.toLong() }
    Equation(parts[0].toLong(), inputValues)
}

data class Equation(val expectedResult: Long, val inputValues: List<Long>)

fun generateOperatorPermutations(size: Int, operators: List<Operator>) : List<List<Operator>> {

    val operatorList = mutableListOf<List<Operator>>()
    val numPermutations = operators.size.toFloat().pow(size).toInt()

    for (i in 0 until numPermutations){
        val operatorString = i.toString(operators.size).padStart(size, '0')
        val permutation = operatorString.map { operators[it.digitToInt()] }
        operatorList.add(permutation)
    }

    return operatorList
}

enum class Operator{
    PLUS,
    MULTIPLY,
    CONCAT
}