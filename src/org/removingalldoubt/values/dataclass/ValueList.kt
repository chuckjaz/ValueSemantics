package org.removingalldoubt.values.dataclass

var valueListsCreated = 0

@Suppress("UNCHECKED_CAST")
class ValueList<T: Any>(vararg items: T) {
    private val array = items as Array<T>

    init { valueListsCreated++ }

    val size get() = array.size

    operator fun get(index: Int) = array[index]

    fun set(index: Int, value: T): ValueList<T> =
        if (this[index] != value) {
            val newArray = array.copyOf()
            newArray[index] = value
            ValueList(*newArray)
        } else this

    fun add(value: T): ValueList<T> = ValueList(*(array + value))

    fun remove(value: T): ValueList<T> = array.indexOf(value).let { index ->
        @Suppress("UNCHECKED_CAST")
        if (index > 0) ValueList(*(arrayWithout(index)) as Array<out T>) else this
    }

    override fun toString(): String = "[${array.joinToString()}]"

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is ValueList<*>) return false
        val iterator = other.array.iterator()
        if (array.size != other.array.size) return false
        for (value in array) {
            if (!iterator.hasNext()) return false
            val otherValue = iterator.next()
            if (otherValue != value) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var code = this::class.hashCode()
        for (value in array) {
            code = code * 31 + value.hashCode()
        }
        return code
    }

    private fun arrayWithout(index: Int): Array<T> =
        Array<Any>(size - 1) { if (it < index) array[it] else array[it + 1]} as Array<T>
}