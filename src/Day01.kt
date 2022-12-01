fun main() {

    fun part1(input: List<String>): Int {
        return input.asSequence()
            .chunkByPredicate { it.isEmpty() }
            .maxOf { list ->
                list.asSequence()
                    .map { it.toInt(10) }
                    .sum()
            }
    }

    fun part2(input: List<String>): Int {
        return input.asSequence()
            .chunkByPredicate { it.isEmpty() }
            .map { list ->
                list.asSequence()
                    .map { it.toInt(10) }
                    .sum()
            }
            .sortedDescending()
            .take(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println("part 1 answer: ${part1(input)}")
    println("part 2 answer: ${part2(input)}")
}
