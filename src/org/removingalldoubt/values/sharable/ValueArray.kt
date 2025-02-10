package org.removingalldoubt.values.sharable

var valueArraysCreated = 0
var valueArrayCopies = 0

@Suppress("UNCHECKED_CAST")
class ValueArray<T> private constructor(
    private var _array: Array<Any?>,
    @Suppress("UNUSED_PARAMETER") disambiguate: Int
) : Sharable<ValueArray<T>> {
    private var shared: Boolean = false

    init { valueArraysCreated++ }
    val size get() = _array.size

    constructor(vararg items: T) : this(_array = (items as Array<Any?>), 0)

    operator fun get(index: Int): T {
        val value = _array[index] as T
        if (shared) return value
        val ownedValue = value.toOwned()
        _array[index] = ownedValue
        return ownedValue
    }

    operator fun set(index: Int, value: T) {
        assertOwned()
        _array[index] = value
    }

    fun add(value: T) {
        assertOwned()
        _array += value
    }

    fun remove(value: T): Boolean  = _array.indexOf(value).let { index ->
        if (index > 0) {
            assertOwned()
            _array = arrayWithout(index)
            true
        } else false
    }

    override fun toShared(): ValueArray<T> {
        if (!shared) {
            _array = _array.toShared()
            shared = true
        }
        return this
    }

    override fun toOwned(): ValueArray<T> {
        if (shared) {
            valueArrayCopies++
            return ValueArray(_array.copyOf(), 0)
        }
        return this
    }

    override fun toString(): String = "[${_array.joinToString()}]"

    override fun equals(other: Any?): Boolean = other === this ||
        (other is ValueArray<*> && _array.contentEquals(other._array))

    override fun hashCode(): Int {
        var code = this::class.hashCode()
        for (value in _array) {
            code = code * 31 + value.hashCode()
        }
        return code
    }

    private fun assertOwned() {
        check(!shared) { "$this is not owned" }
    }

    private fun arrayWithout(index: Int): Array<Any?> =
        Array(size - 1) { if (it < index) _array[it] else _array[it + 1] }.also { valueArrayCopies++ }

    private fun T.toOwned(): T = if (this is Sharable<*>) this.toOwned() as T else this

    private fun Array<Any?>.toShared(): Array<Any?> {
        valueArrayCopies++
        val copy = copyOf()
        for (i in copy.indices) {
            val item = copy[i]
            @Suppress("UNCHECKED_CAST")
            copy[i] = if (item is Sharable<*>) item.toShared() as T else item
        }
        return copy
    }
}

val emptyValueArray: ValueArray<*> = ValueArray<Any?>()
@Suppress("UNCHECKED_CAST")
fun <T> valueArrayOf() = emptyValueArray as ValueArray<T>
fun <T> valueArrayOf(vararg item: T) = ValueArray(*item)