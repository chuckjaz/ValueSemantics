@file:Suppress("UNCHECKED_CAST")

package org.removingalldoubt.values.with

var valueArraysCreated = 0
var valueArrayCopies = 0

class ValueArray<T> private constructor(private val _array: Array<Any?>, disambiguate: Int) {
    constructor(vararg items: T) : this(_array = (items as Array<Any?>), 0)

    init { valueArraysCreated++ }
    val size get() = _array.size

    operator fun get(index: Int): T = _array[index] as T
    fun withSet(index: Int, value: T): ValueArray<T> {
        valueArrayCopies++
        val newArray = _array.copyOf()
        newArray[index] = value
        return ValueArray(newArray, 0)
    }

    fun withAdd(value: T) = ValueArray<T>(_array + value, 0)

    fun withRemove(value: T): ValueArray<T>  = _array.indexOf(value).let { index ->
        if (index > 0) ValueArray(arrayWithout(index), 0) else this
    }

    private fun arrayWithout(index: Int): Array<Any?> =
        Array(size - 1) { if (it < index) _array[it] else _array[it + 1] }.also { valueArrayCopies++ }

    override fun toString(): String = "[${_array.joinToString()}]"

    override fun equals(other: Any?): Boolean =
        other === this ||
            (other is ValueArray<*> && _array.contentEquals(other._array))

    override fun hashCode(): Int {
        var code = this::class.hashCode()
        for (value in _array) {
            code = code * 31 + value.hashCode()
        }
        return code
    }
}

fun <T> valueArrayOf(vararg item: T) = ValueArray(*item)