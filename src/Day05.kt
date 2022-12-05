class StackState(
    val numberOfStacks: Int,
) {

    val stacks = Array(numberOfStacks) { ArrayDeque<Char>() }

    fun topLine(): String {
        return stacks.asSequence()
            .map { it.last() }
            .joinToString("")
    }

    fun executeOneByOne(action: StackAction) {
        val origin = stacks[action.origin - 1]
        val destination = stacks[action.destination - 1]

        for (i in 0 until action.quantity) {
            destination.addLast(origin.removeLast())
        }
    }

    fun executeSingleBatch(action: StackAction) {
        val origin = stacks[action.origin - 1]
        val destination = stacks[action.destination - 1]

        val buffer = ArrayDeque<Char>()

        for (i in 0 until action.quantity) {
            buffer.addFirst(origin.removeLast())
        }

        destination.addAll(buffer)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        val stackHeight = stacks.maxOf { it.size }
        for (level in (stackHeight - 1) downTo 0) {
            stacks.forEach { stack ->
                stack.elementAtOrNull(level)?.let { sb.append('[').append(it).append(']') } ?: sb.append("   ")
                sb.append(' ')
            }
            sb.append('\n')
        }
        for (i in 0 until numberOfStacks) {
            sb.append(' ').append(i + 1).append("  ")
        }
        return sb.toString()
    }

    companion object {
        fun parse(stateString: List<String>): StackState {

            val number = stateString.last().split(' ').last().toInt()

            val state = StackState(number)

            stateString.asReversed().asSequence().drop(1).forEach { line ->
                for (i in 0 until state.numberOfStacks) {
                    val charIndex = i * 4 + 1
                    if (line.length > charIndex) {
                        val toAddChar = line.elementAt(charIndex)
                        if (toAddChar != ' ') {
                            state.stacks[i].addLast(toAddChar)
                        }
                    }
                }
            }

            return state
        }
    }
}

class StackAction(
    val quantity: Int,
    val origin: Int,
    val destination: Int,
) {

    override fun toString(): String {
        return "move $quantity from $origin to $destination"
    }

    companion object {
        fun parse(line: String): StackAction {
            val split = line.split(' ')

            return StackAction(
                split[1].toInt(),
                split[3].toInt(),
                split[5].toInt(),
            )
        }
    }
}

fun main() {

    fun part1(input: List<String>): String {
        val emptyLineIndex = input.indexOfFirst { it.isEmpty() }

        val initialState = input.subList(0, emptyLineIndex)

        val state = StackState.parse(initialState)

        input.asSequence()
            .drop(emptyLineIndex + 1)
            .map { StackAction.parse(it) }
            .forEach { action ->
                state.executeOneByOne(action)
            }

        return state.topLine()
    }

    fun part2(input: List<String>): String {
        val emptyLineIndex = input.indexOfFirst { it.isEmpty() }

        val initialState = input.subList(0, emptyLineIndex)

        val state = StackState.parse(initialState)

        input.asSequence()
            .drop(emptyLineIndex + 1)
            .map { StackAction.parse(it) }
            .forEach { action ->
                state.executeSingleBatch(action)
            }

        return state.topLine()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println("part 1 answer: ${part1(input)}")
    println("part 2 answer: ${part2(input)}")
}
