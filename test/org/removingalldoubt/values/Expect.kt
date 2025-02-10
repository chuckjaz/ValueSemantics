package org.removingalldoubt.values

interface Expect<T> { val actual: T }

fun <T> expect(actual: T): Expect<T> {
    return object : Expect<T> {
        override val actual: T get() = actual
    }
}

fun <T> Expect<T>.toEqual(expected: T) {
    if (expected != actual) {
        throw Error("Expected $actual, to equal $expected")
    }
}

fun <T> Expect<T>.toNotEqual(expected: T) {
    if (expected === actual) {
        throw Error("Expected $actual, to not equal $expected")
    }
}