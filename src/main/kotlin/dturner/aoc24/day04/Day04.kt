package dturner.aoc24.day04

import dturner.aoc24.utils.*

fun main() {

    val testInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().split("\n").filter { it.isNotEmpty() }

    check(part1(testInput) == 18)

    val input = readInput("Day04")
    println(part1(input))

    check(part2(testInput) == 9)
    part2(input).println()
}

fun part1(input: List<String>): Int {

    // The word "XMAS" can be found in 8 different directions.
    // Create a list of strings for each direction then search them all.
    val up = input.map { it.toCharArray() }
    val upRight = up.rotate45()
    val right = up.rotate90()
    val downRight = right.rotate45()
    val down = right.rotate90()
    val downLeft = down.rotate45()
    val left = down.rotate90()
    val upLeft = left.rotate45()

    return up.xmasCount() + upRight.xmasCount() + right.xmasCount() + downRight.xmasCount() + down.xmasCount() + downLeft.xmasCount() + left.xmasCount() + upLeft.xmasCount()
}

private fun List<CharArray>.xmasCount() : Int {
    var count = 0
    forEach {
        val stringRepresentation = it.joinToString("")
        val localCount = stringRepresentation.split("XMAS").size - 1
        count += localCount
    }
    return count
}

private fun List<CharArray>.rotate45() : List<CharArray> {

    val result = List(size + first().size - 1) { _ -> CharArray(first().size, { _ -> ' '}) }

    forEachIndexed { rowIndex, rows ->
        rows.forEachIndexed { colIndex, c ->
            result[rowIndex+colIndex][colIndex] = c
        }
    }

    return result
}

private fun List<CharArray>.rotate90() : List<CharArray> {

    val outputColumnSize = size
    val outputRowSize = first().size

    val columns: List<CharArray> =
        List(outputRowSize) { _ -> CharArray(outputColumnSize) }

    // iterate through each row, copying each character into a list corresponding to the column index
    forEachIndexed { rowIndex, row ->
        val insertColumnIndex = (outputColumnSize - rowIndex) - 1
        row.forEachIndexed { columnIndex, char ->
            columns[columnIndex][insertColumnIndex] = char
        }
    }
    return columns
}

fun List<CharArray>.toStrings() : List<String> = map {
        it.joinToString("")
    }


fun checkRotate90(){
    val input = listOf("AB", "CD")
    val expected = listOf("CA", "DB")
    val actual = input.map { it.toCharArray() }.rotate90().toStrings()
    println(actual)
    check(actual == expected)
}

fun checkRotate45(){

    val input = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().split("\n").filter { it.isNotEmpty() }

    val expected = """
        M
        MM
        ASM
        MMAS
        XSXMX
        XMASXX
        SXAMXMM
        SMASAMSA
        MASMASAMS
        MAXMMMMASM
         XMASXXSMA
          MMMAXAMM
           XMASAMX
            AXSXMM
             XMASA
              MMAS
               AMA
                SM
                 X
    """.trimIndent().split("\n").filter { it.isNotEmpty() }.map { it.trim() }

    println(expected)
    val output = input.map { it.toCharArray() }.rotate45().map { it.joinToString("").trim() }
    println(output)

    check(output == expected)
}

fun part2(input: List<String>): Int {

    // From a 3x3 char array, determine whether it's an X-MAS.
    // An X-MAS can be any of the following:
    // M S S M M M S S
    //  A   A   A   A
    // M S,S M,S S,M M

    val inputCharArray = input.map { it.toCharArray() }
    val rowSize = inputCharArray.first().size
    var count = 0

    // Move a 3x3 window through the array, checking to see whether it contains an X-MAS.
    for (rowIndex in 0 .. inputCharArray.size - 3){
        for (colIndex in 0 .. rowSize - 3){
            val window = inputCharArray.subList(rowIndex, rowIndex+3).map {
                listOf(it[colIndex], it[colIndex+1], it[colIndex+2])
            }.flatten()
            if (isXMAS(window)) count++
        }
    }

    return count
}

// Create a set of all possible X-MASes.
private val xMasSet = setOf("MSAMS", "SSAMM", "MMASS", "SMASM")

private fun isXMAS(input: List<Char>) : Boolean {

    // Only select the chars we care about.
    val significantChars = listOf(
        input[0],
        input[2],
        input[4],
        input[6],
        input[8]
    ).joinToString("")


    return xMasSet.contains(significantChars)
}