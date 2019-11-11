package com.example.smartredact.common.extension

/**
 * Created by TuHA on 6/6/2019.
 */
inline fun <E> Collection<E>.contains(predicate: (e: E) -> Boolean): Boolean {
    this.forEach { element ->
        val hasContain = predicate.invoke(element)
        if (hasContain) {
            return true
        }
    }

    return false
}

inline fun <E> MutableList<E>.removeItemIf(predicate: (e: E) -> Boolean): Int {
    for (i in this.indices) {
        if (predicate.invoke(this[i])) {
            this.removeAt(i)
            return i
        }
    }
    return -1
}

fun <T> ArrayList<T>.removeAt(startIndex: Int, count: Int) {
    for (i in startIndex until startIndex + count) {
        if (startIndex >= this.size) {
            return
        }
        this.removeAt(startIndex)
    }
}

inline fun <T> List<T>.forEachDown(action: (T) -> Unit) {
    val iterator = this.listIterator(size)
    while (iterator.hasPrevious()) {
        action.invoke(iterator.previous())
    }
}

inline fun <T> List<T>.forEachDownIndexed(action: (Int, T) -> Unit) {
    val iterator = this.listIterator(size)
    while (iterator.hasPrevious()) {
        action.invoke(iterator.previousIndex(), iterator.previous())
    }
}

inline fun <T> List<T>.compareWith(other: List<T>, predicate: (first: T, second: T) -> Boolean): Boolean {
    if (this.size != other.size) {
        return false
    }

    this.forEachIndexed { index, t ->
        if (!predicate(t, other[index])) {
            return false
        }
    }

    return true
}

inline fun <T, R> MutableList<R>.addAll(list: List<T>, block: (T) -> R) {
    list.forEach {
        this.add(block(it))
    }
}

inline fun <T> Iterable<T>.any(predicate: (index: Int, T) -> Boolean): Boolean {
    if (this is Collection && isEmpty()) return false
    this.forEachIndexed { index, element ->
        if (predicate(index, element)) return true
    }
    return false
}

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

fun <T> Sequence<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this.toList())
}