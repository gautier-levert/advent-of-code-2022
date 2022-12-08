import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun <T : Any?> Sequence<T>.chunkByPredicate(
    includeSeparatorElement: Boolean = false,
    predicate: (T) -> Boolean,
): Sequence<List<T>> {
    return sequence {
        var current = mutableListOf<T>()
        forEach { elt ->
            if (predicate(elt)) {
                if (includeSeparatorElement) {
                    current.add(elt)
                }
                yield(current)
                current = mutableListOf()
            } else {
                current.add(elt)
            }
        }
        yield(current)
    }
}

fun IntRange.fullyContainedIn(other: IntRange): Boolean {
    return first >= other.first && last <= other.last
}

fun IntRange.overlaps(other: IntRange): Boolean {
    return first <= other.last && last >= other.first
}

class Matrix<T : Any?>(
    val rows: Int,
    val columns: Int,
    init: (Int, Int) -> T
) {

    val rowIndexes: IntRange get() = 0 until rows

    val columnIndexes: IntRange get() = 0 until columns

    private fun indexToRow(index: Int): Int = index / rows

    private fun rowColumnToIndex(row: Int, column: Int): Int = row * rows + column

    private fun indexToColumn(index: Int): Int = index % rows

    private val values = Array<Any?>(rows * columns) { index -> init(indexToRow(index), indexToColumn(index)) }

    @Suppress("UNCHECKED_CAST")
    operator fun get(row: Int, column: Int): T {
        return values[rowColumnToIndex(row, column)] as T
    }

    operator fun set(row: Int, column: Int, value: T) {
        values[rowColumnToIndex(row, column)] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun count(predicate: (T) -> Boolean): Int {
        return values.count { predicate(it as T) }
    }

    operator fun contains(element: T): Boolean {
        return values.contains(element)
    }

    @Suppress("UNCHECKED_CAST")
    fun random(): T {
        return values.random() as T
    }

    @Suppress("UNCHECKED_CAST")
    fun random(random: kotlin.random.Random): T {
        return values.random(random) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun sumOf(selector: (T) -> Int): Int {
        return values.sumOf { selector(it as T) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <R : Comparable<R>> maxOf(selector: (T) -> R): R {
        return values.maxOf { selector(it as T) }
    }
}

infix fun Int.downUntil(to: Int): IntProgression {
    if (to >= Int.MAX_VALUE) {
        return IntRange.EMPTY
    }
    return this downTo (to + 1)
}
