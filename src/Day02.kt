enum class Rochambeau(
    val opponentChoice: String,
    val playerChoice: String,
    val score: Int,
) {
    ROCK("A", "X", 1) {
        override fun deduceOutcome(playerPlay: Rochambeau): Outcome {
            return when (playerPlay) {
                ROCK -> Outcome.DRAW
                PAPER -> Outcome.WON
                SCISSORS -> Outcome.LOST
            }
        }

        override fun deducePlayerPlay(outcome: Outcome): Rochambeau {
            return when (outcome) {
                Outcome.DRAW -> ROCK
                Outcome.WON -> PAPER
                Outcome.LOST -> SCISSORS
            }
        }
    },
    PAPER("B", "Y", 2) {
        override fun deduceOutcome(playerPlay: Rochambeau): Outcome {
            return when (playerPlay) {
                ROCK -> Outcome.LOST
                PAPER -> Outcome.DRAW
                SCISSORS -> Outcome.WON
            }
        }

        override fun deducePlayerPlay(outcome: Outcome): Rochambeau {
            return when (outcome) {
                Outcome.LOST -> ROCK
                Outcome.DRAW -> PAPER
                Outcome.WON -> SCISSORS
            }
        }
    },
    SCISSORS("C", "Z", 3) {
        override fun deduceOutcome(playerPlay: Rochambeau): Outcome {
            return when (playerPlay) {
                ROCK -> Outcome.WON
                PAPER -> Outcome.LOST
                SCISSORS -> Outcome.DRAW
            }
        }

        override fun deducePlayerPlay(outcome: Outcome): Rochambeau {
            return when (outcome) {
                Outcome.WON -> ROCK
                Outcome.LOST -> PAPER
                Outcome.DRAW -> SCISSORS
            }
        }
    };

    abstract fun deduceOutcome(playerPlay: Rochambeau): Outcome

    abstract fun deducePlayerPlay(outcome: Outcome): Rochambeau

    companion object {
        fun fromOpponentChoice(value: String): Rochambeau {
            return Rochambeau.values().asSequence()
                .firstOrNull { it.opponentChoice == value }
                ?: throw NoSuchElementException("Invalid opponent choice: `${value}`")
        }

        fun fromPlayerChoice(value: String): Rochambeau {
            return Rochambeau.values().asSequence()
                .firstOrNull { it.playerChoice == value }
                ?: throw NoSuchElementException("Invalid player choice: `${value}`")
        }
    }
}

enum class Outcome(
    val code: String,
    val score: Int,
) {
    LOST("X", 0),
    DRAW("Y", 3),
    WON("Z", 6);

    companion object {
        fun fromCode(value: String): Outcome {
            return Outcome.values().asSequence()
                .firstOrNull { it.code == value }
                ?: throw NoSuchElementException("Invalid outcome code: `${value}`")
        }
    }
}

interface Game {
    val opponentPlay: Rochambeau
    val playerPlay: Rochambeau
    val outcome: Outcome
    val score: Int
        get() = playerPlay.score + outcome.score
}

class DeducingOutcomeGame(
    override val opponentPlay: Rochambeau,
    override val playerPlay: Rochambeau,
) : Game {

    override val outcome: Outcome
        get() = opponentPlay.deduceOutcome(playerPlay)

    companion object {
        fun parse(line: String): DeducingOutcomeGame {
            val choices = line.split(' ')
            return DeducingOutcomeGame(
                Rochambeau.fromOpponentChoice(choices[0]),
                Rochambeau.fromPlayerChoice(choices[1])
            )
        }
    }
}

class DeducingPlayerPlayGame(
    override val opponentPlay: Rochambeau,
    override val outcome: Outcome,
) : Game {

    override val playerPlay: Rochambeau
        get() = opponentPlay.deducePlayerPlay(outcome)

    companion object {
        fun parse(line: String): DeducingPlayerPlayGame {
            val choices = line.split(' ')
            return DeducingPlayerPlayGame(
                Rochambeau.fromOpponentChoice(choices[0]),
                Outcome.fromCode(choices[1])
            )
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.asSequence()
            .map { DeducingOutcomeGame.parse(it) }
            .map { it.score }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence()
            .map { DeducingPlayerPlayGame.parse(it) }
            .map { it.score }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println("part 1 answer: ${part1(input)}")
    println("part 2 answer: ${part2(input)}")
}
