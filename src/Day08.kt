fun main() {

    fun part1(input: List<String>): Int {
        val forest = Matrix(input.size, input[0].length) { row, column ->
            input[row][column].digitToInt()
        }
        val visible = Matrix(input.size, input[0].length) { _, _ -> false }

        for (i in forest.rowIndexes) {
            for (j in forest.columnIndexes) {
                val tree = forest[i, j]

                var visibleFromLeft = true
                for (x in 0 until i) {
                    val obsctacle = forest[x, j]
                    if (obsctacle >= tree) {
                        visibleFromLeft = false
                    }
                }
                var visibleFromTop = true
                for (x in 0 until j) {
                    val obsctacle = forest[i, x]
                    if (obsctacle >= tree) {
                        visibleFromTop = false
                    }
                }
                var visibleFromBottom = true
                for (x in (forest.rows - 1) downUntil i) {
                    val obsctacle = forest[x, j]
                    if (obsctacle >= tree) {
                        visibleFromBottom = false
                    }
                }
                var visibleFromRight = true
                for (x in (forest.columns - 1) downUntil j) {
                    val obsctacle = forest[i, x]
                    if (obsctacle >= tree) {
                        visibleFromRight = false
                    }
                }

                visible[i, j] = visibleFromTop || visibleFromLeft || visibleFromBottom || visibleFromRight
            }
        }

        val sum = visible.count { it }

        return sum
    }

    fun part2(input: List<String>): Int {
        val forest = Matrix(input.size, input[0].length) { row, column ->
            input[row][column].digitToInt()
        }
        val vision = Matrix(input.size, input[0].length) { _, _ ->
            0
        }

        for (i in forest.rowIndexes) {
            for (j in forest.columnIndexes) {
                val tree = forest[i, j]

                var leftBlocked = false
                var leftView = 0
                for (x in (i - 1) downTo 0) {
                    val obstacle = forest[x, j]
                    if (!leftBlocked) {
                        leftView += 1
                        leftBlocked = obstacle >= tree
                    }
                }
                var rightBlocked = false
                var rightView = 0
                for (x in (i + 1) until forest.rows) {
                    val obstacle = forest[x, j]
                    if (!rightBlocked) {
                        rightView += 1
                        rightBlocked = obstacle >= tree
                    }
                }
                var topBlocked = false
                var topView = 0
                for (x in (j - 1) downTo 0) {
                    val obstacle = forest[i, x]
                    if (!topBlocked) {
                        topView += 1
                        topBlocked = obstacle >= tree
                    }
                }
                var bottomBlocked = false
                var bottomView = 0
                for (x in (j + 1) until  forest.rows) {
                    val obstacle = forest[i, x]
                    if (!bottomBlocked) {
                        bottomView += 1
                        bottomBlocked = obstacle >= tree
                    }
                }

                vision[i, j] = topView * bottomView * leftView * rightView
            }
        }

        return vision.maxOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println("part 1 answer: ${part1(input)}")
    println("part 2 answer: ${part2(input)}")
}
