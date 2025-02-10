package org.removingalldoubt.values.with

import org.removingalldoubt.values.expect
import org.removingalldoubt.values.toEqual
import org.removingalldoubt.values.toNotEqual
import org.testng.annotations.Test

class NodeTest {
    @Test
    fun canCreateANode() {
        val node = Node("Some value")
        expect(node.value).toEqual("Some value")
        expect(node.left).toEqual(null)
        expect(node.right).toEqual(null)
    }

    @Test
    fun canCreateATree() {
        val alice = Person(name = "Alice", age = 35)
        val bob = Person(name = "Bob", age = 42)
        val carl = Person(name = "Carl", age = 52)
        val tree = Node(bob, Node(alice), Node(carl))
        expect(tree.value).toEqual(bob)
        expect(tree.left?.value).toEqual(alice)
        expect(tree.right?.value).toEqual(carl)
    }

    @Test
    fun carModifyTheTree() = stats {
        val alice = Person(name = "Alice", age = 35)
        val bob = Person(name = "Bob", age = 42)
        val carl = Person(name = "Carl", age = 52)
        var tree = Node(bob, Node(alice), Node(carl))
        val originalTree = tree

        expect(tree.value).toEqual(bob)
        expect(tree.left?.value).toEqual(alice)
        expect(tree.right?.value).toEqual(carl)

        tree = tree.withLeft(tree.left?.let { it.withValue(it.value.withAge(it.value.age + 1))})

        expect(tree).toNotEqual(originalTree)
        expect(originalTree.left?.value).toEqual(alice)
        expect(tree.value).toEqual(bob)
        expect(tree.right?.value).toEqual(carl)
        expect(tree.left?.value).toNotEqual(alice)
        expect(tree.left?.value?.name).toEqual("Alice")
        expect(tree.left?.value?.age).toEqual(36)
    }
}