package dturner.aoc24.day05

import dturner.aoc24.utils.*

fun main() {

    val testRules = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13
    """.trimIndent().split("\n").filter { it.isNotEmpty() }.toPairs()

    val testUpdates = """
        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent().split("\n").filter { it.isNotEmpty() }.toIntLists()

    check(part1(testRules, testUpdates) == 143)
    check(part2(testRules, testUpdates) == 123)

    val rules = readInput("Day05-rules").toPairs()
    val updates = readInput("Day05-updates").toIntLists()

    check(part1(rules, updates) == 5275)
    check(part2(rules, updates) == 6191)

}

fun part1(ruleStrings: List<Pair<Int, Int>>, updates: List<List<Int>>): Int {

    var middlePageSum = 0
    updates.forEach { update ->
        var isUpdateCorrect = true

        // check if all the rules are satisfied
        ruleStrings.forEach { rule ->
            if (!update.isRuleSatisfied(rule)){
                isUpdateCorrect = false
            }
        }
        if (isUpdateCorrect){
            middlePageSum += update[(update.size - 1) / 2]
        }
    }
    return middlePageSum
}

fun List<Int>.isRuleSatisfied(rule: Pair<Int,Int>) : Boolean {
    return if (contains(rule.first) && contains(rule.second)){
        indexOf(rule.first) < indexOf(rule.second)
    } else {
        true
    }
}

fun part2(rules: List<Pair<Int, Int>>, updates: List<List<Int>>): Int {
    var middlePageSum = 0

    updates.forEach { update ->
        var hasBeenFixed = false
        val mutableUpdate = update.toMutableList()
        while (!mutableUpdate.areRulesSatisfied(rules)){
            rules.forEach { rule ->
                if (!mutableUpdate.isRuleSatisfied(rule)){
                    // Fix it by swapping the incorrect values.
                    val tmp = mutableUpdate[mutableUpdate.indexOf(rule.first)]
                    mutableUpdate[mutableUpdate.indexOf(rule.first)] = mutableUpdate[mutableUpdate.indexOf(rule.second)]
                    mutableUpdate[mutableUpdate.indexOf(rule.second)] = tmp
                    hasBeenFixed = true
                }
            }
        }
        if (hasBeenFixed){
            middlePageSum += mutableUpdate[(mutableUpdate.size - 1) / 2]
        }
    }
    return middlePageSum
}

private fun List<String>.toPairs() = map {
    val values = it.split("|")
    Pair(values[0].toInt(), values[1].toInt())
}

private fun List<String>.toIntLists() = map {
    val values = it.split(",")
    values.map { value -> value.toInt() }
}

fun List<Int>.areRulesSatisfied(rules: List<Pair<Int,Int>>) : Boolean {

    var areRulesSatisfied = true

    rules.forEach { rule ->
        if (!isRuleSatisfied(rule)) areRulesSatisfied = false
    }

    return areRulesSatisfied
}
