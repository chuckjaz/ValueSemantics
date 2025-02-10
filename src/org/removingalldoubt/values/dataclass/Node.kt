package org.removingalldoubt.values.dataclass

var nodesCreated = 0

data class Node<T>(val value: T, val left: Node<T>? = null, val right: Node<T>? = null) {
    init { nodesCreated ++ }
    override fun toString(): String = when  {
        left != null && right != null -> "Node($value, $left, $right)"
        right != null -> "Node($value, $right)"
        left != null -> "Node($value, $left)"
        else -> "Node($value)"
    }
}
