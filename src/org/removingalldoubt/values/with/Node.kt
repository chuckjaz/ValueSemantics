package org.removingalldoubt.values.with

var nodesCreated = 0

class Node<T>(value: T, left: Node<T>? = null, right: Node<T>? = null) {
    init { nodesCreated++ }
    private val _value = value
    val value get() = _value
    fun withValue(value: T) = Node(value, _left, _right)

    private val _left = left
    val left get() = _left
    fun withLeft(value: Node<T>?) = Node(_value, value, _right)

    private val _right = right
    val right get() = _right
    fun withRight(value: Node<T>?) = Node(_value, _left, value)

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

}