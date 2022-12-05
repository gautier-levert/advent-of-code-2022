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
