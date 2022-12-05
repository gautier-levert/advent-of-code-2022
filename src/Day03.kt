fun main() {

    fun priority(c: Char): Int {
        if (c in 'a'..'z') {
            return (c - 'a') + 1
        } else if (c in 'A'..'Z') {
            return (c - 'A') + 27
        } else {
            throw IllegalArgumentException("Unsupported character `$c`")
        }
    }

    fun part1(input: List<String>): Int {
        return input.asSequence()
            .map { line ->
                val halfLength = line.length / 2

                val firstCompartment = line.subSequence(0, halfLength)
                    .toSet()
                val secondCompartment = line.subSequence(halfLength, line.length)
                    .toSet()

                val commonElements = firstCompartment.intersect(secondCompartment)

                return@map commonElements.sumOf { priority(it) }
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence()
            .chunked(3)
            .map { (first, second, third) ->
                val badge = first.toSet().intersect(second.toSet()).intersect(third.toSet()).first()
                return@map priority(badge)
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println("part 1 answer: ${part1(input)}")
    println("part 2 answer: ${part2(input)}")
}
