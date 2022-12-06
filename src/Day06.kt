fun main() {

    fun part1(input: String): Int {
        return input.windowed(4, 1)
            .indexOfFirst { substring -> substring.toSet().size == 4 } + 4
    }

    fun part2(input: String): Int {
        return input.windowed(14, 1)
            .indexOfFirst { substring -> substring.toSet().size == 14 } + 14
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput.first()) == 7) { "Expected 7 given ${part1(testInput.first())}"}
    check(part2(testInput.first()) == 19) { "Expected 19 given ${part2(testInput.first())}"}

    val input = readInput("Day06")
    println("part 1 answer: ${part1(input.first())}")
    println("part 2 answer: ${part2(input.first())}")
}
