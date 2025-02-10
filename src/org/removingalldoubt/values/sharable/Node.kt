@file:Suppress("UNCHECKED_CAST")

package org.removingalldoubt.values.sharable

var nodesCreated = 0

class Node<T>(value: T, left: Node<T>? = null, right: Node<T>? = null): Sharable<Node<T>> {
    private var _value = value
    private var _left = left
    private var _right = right
    private var shared = false

    init { nodesCreated++ }

    var value: T
        get() {
            val value = _value
            return if (shared) value else {
                val newValue = value.toOwned()
                _value = newValue
                newValue
            }
        }
        set(value) {
            assertOwned()
            _value = value
        }

    var left: Node<T>?
        get() {
            val value = _left
            return if (shared) value else {
                val newValue = value?.toOwned()
                _left = newValue
                newValue
            }
        }
        set(value) {
            assertOwned()
            _left = value
        }

    var right: Node<T>?
        get()  {
            val value = _right
            return if (shared) value else {
                val newValue = value?.toOwned()
                _right = newValue
                newValue
            }
        }
        set(value) {
            assertOwned()
            _right = value
        }

    override fun toShared(): Node<T> {
        _value = _value.toShared()
        _left = _left?.toShared()
        _right = _right?.toShared()
        shared = true
        return this
    }

    override fun toOwned(): Node<T> {
        if (shared) {
            return Node(value, left, right)
        }
        return this
    }

    override fun toString(): String = when  {
        _left != null && _right != null -> "Node($_value, $_left, $_right)"
        _right != null -> "Node($_value, $_right)"
        _left != null -> "Node($_value, $_left)"
        else -> "Node($_value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Node<*>) return false
        return _value == other._value && _left == other._left && _right == other._right
    }

    override fun hashCode(): Int =
        ((this::class.hashCode() * 31 + _value.hashCode()) * 31 + (_left?.hashCode() ?: 0)) + (_right?.hashCode() ?: 0)

    private fun assertOwned() {
        check(!shared) { "$this is not owned" }
    }

    private fun T.toOwned(): T = if (this is Sharable<*>) this.toOwned() as T else this
    private fun T.toShared(): T = if (this is Sharable<*>) this.toShared() as T else this
}
