fun main() {

    fun part1(input: List<String>): Int {
        return input.count { line ->
            val ranges = line.split(',')
                .map { range ->
                    val values = range.split('-').map { it.toInt() }
                    values[0]..values[1]
                }

            return@count ranges[0].fullyContainedIn(ranges[1]) || ranges[1].fullyContainedIn(ranges[0])
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { line ->
            val ranges = line.split(',')
                .map { range ->
                    val values = range.split('-').map { it.toInt() }
                    values[0]..values[1]
                }

            return@count ranges[0].overlaps(ranges[1])
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println("part 1 answer: ${part1(input)}")
    println("part 2 answer: ${part2(input)}")
}
