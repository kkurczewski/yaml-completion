package pl.kurczews.common

fun <T, U> MutableMap<T, U>.update(key: T, transform: (U) -> U) {
    this[key] = transform(this.getValue(key))
}