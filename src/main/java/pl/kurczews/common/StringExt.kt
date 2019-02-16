package pl.kurczews.common

fun String.split2(delimiter: Char): Pair<String, String> {
    return this.split(delimiter, limit = 2).let { Pair(it[0], it.getOrElse(1) { "" }) }
}

fun String.slice2(index: Int): Pair<String, String> {
    return Pair(this.substring(0, index + 1), this.substring(index + 1))
}