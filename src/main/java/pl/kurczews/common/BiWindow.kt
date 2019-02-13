package pl.kurczews.common

fun <T> Iterable<T>.forEach2(consume: (first: T, second: T) -> Unit) {
    this.windowed(2) { window -> consume(window[0], window[1]) }
}