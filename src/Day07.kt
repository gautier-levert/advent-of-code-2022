interface FileSystemElement {
    val name: String
    val size: Int
    val parent: FileSystemFolder?
    val children: List<FileSystemElement>

    fun recursiveChildren(): Sequence<FileSystemElement>

    companion object {
        fun parse(line: String, parent: FileSystemFolder): FileSystemElement {
            val (size, name) = line.split(' ')
            return if (size == "dir") {
                FileSystemFolder(name, parent)
            } else {
                FileSystemFile(name, size.toInt(), parent)
            }
        }
    }
}

data class FileSystemFolder(
    override val name: String,
    override val parent: FileSystemFolder? = null
) : FileSystemElement {

    override val children: List<FileSystemElement>
        get() = elements.toList()

    private val elements = mutableListOf<FileSystemElement>()

    fun add(elt: FileSystemElement) {
        elements.add(elt)
    }

    override fun recursiveChildren(): Sequence<FileSystemElement> {
        return children.asSequence()
            .flatMap { sequenceOf(it) + it.recursiveChildren() }
    }

    fun resolve(name: String): FileSystemFolder? {
        if (name == "..") {
            return parent
        }
        return elements
            .find { it is FileSystemFolder && it.name == name } as FileSystemFolder?
    }

    override val size: Int
        get() = children.sumOf { it.size }

    override fun toString(): String {
        val subStrings = elements.asSequence()
            .map { it.toString() }
            .flatMap { it.split('\n') }
            .map { "  $it" }
            .joinToString("\n")

        return "$name -> $size\n${subStrings}"
    }
}

data class FileSystemFile(
    override val name: String,
    override val size: Int,
    override val parent: FileSystemFolder? = null
) : FileSystemElement {
    override val children: List<FileSystemElement>
        get() = emptyList()

    override fun recursiveChildren(): Sequence<FileSystemElement> {
        return emptySequence()
    }

    override fun toString(): String {
        return "$name -> $size"
    }
}

fun Sequence<FileSystemElement>.folders(): Sequence<FileSystemFolder> {
    return filter { it is FileSystemFolder }.map { it as FileSystemFolder }
}

fun main() {

    fun part1(input: List<String>): Int {

        val root = FileSystemFolder("/")

        input.asSequence()
            .drop(1)
            .fold(root) { currentFolder, line ->
                if (line.startsWith('$')) {
                    if (line.startsWith("\$ cd")) {
                        val folder = line.split(' ')[2]
                        return@fold currentFolder.resolve(folder)
                            ?: throw IllegalArgumentException("Unable to find folder `$folder` inside folder `${currentFolder.name}`")
                    }
                } else {
                    val file = FileSystemElement.parse(line, currentFolder)
                    currentFolder.add(file)
                }
                return@fold currentFolder
            }

        println(root)

        return root.recursiveChildren()
            .folders()
            .filter { it.size < 100000 }
            .sumOf { it.size }
    }

    fun part2(input: List<String>): Int {
        val root = FileSystemFolder("/")

        input.asSequence()
            .drop(1)
            .fold(root) { currentFolder, line ->
                if (line.startsWith('$')) {
                    if (line.startsWith("\$ cd")) {
                        val folder = line.split(' ')[2]
                        return@fold currentFolder.resolve(folder)
                            ?: throw IllegalArgumentException("Unable to find folder `$folder` inside folder `${currentFolder.name}`")
                    }
                } else {
                    val file = FileSystemElement.parse(line, currentFolder)
                    currentFolder.add(file)
                }
                return@fold currentFolder
            }

        val totalDiskAvailable = 70000000
        val spaceNeeded = 30000000
        val allUsed = root.size

        println(root)

        return root.recursiveChildren()
            .folders()
            .sortedBy { it.size }
            .find { folder -> ((totalDiskAvailable - allUsed) + folder.size) >= spaceNeeded }!!
            .size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println("part 1 answer: ${part1(input)}")
    println("part 2 answer: ${part2(input)}")
}
