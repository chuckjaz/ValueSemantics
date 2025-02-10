@file:Suppress("KotlinConstantConditions")

package org.removingalldoubt.values.with

fun withSample() {
    stats {
        val alice = Person(name = "Alice", age = 35)
        var bob = Person(name = "Bob", age = 42)
        val carl = Person(name = "Carl", age = 21)

        println("Original bob: $bob")
        val originalBob = bob
        println("Original bob: $originalBob, equal?: ${bob == originalBob}, identity equal?: ${bob === originalBob}")

        // bob.age++
        bob = bob.withAge(bob.age + 1)
        // bob.name = "Robert"
        bob = bob.withName("Robert")

        println("Updated bob $bob")

        var people = ValueArray(alice, bob)
        val originalPeople = people
        println("people: $people")
        println("original people: $originalPeople")

        // people.add(carl)
        people = people.withAdd(carl)
        println("People: $people, size: ${people.size}")
        println("Original $originalPeople, size: ${originalPeople.size}")

        // people[1].name = "Bob"
        people = people.withSet(1, people[1].withName("Bob"))
        println("Modify Bob's name in place $people")

        // people[1] = originalBob
        people = people.withSet(1, originalBob)
        println("Back to original in place $people")

        // updateName(people[1], "Robert") // Ownership is of person is borrowed
        people = people.withSet(1, withUpdateName(people[1], "Robert"))
        // advanceAge(people[1]) // Ownership of person is borrowed
        people = people.withSet(1, withAdvanceAge(people[1]))
        println("Back to Robert in place with abstraction over update: $people")

        people = people.withRemove(carl)
        println("original people: $originalPeople")
        println("Back to original list, $people, people == originalPeople = ${people == originalPeople}")

        // Tree of people
        // Ownership of bob moves to Node as this is the last reference to bob
        var tree = Node(bob, Node(alice), Node(carl))

        println("Tree: $tree")
        val originalTree = tree

        // tree.left?.let { it.value.age++ }
        tree = tree.withLeft(tree.left?.let { it.withValue(it.value.let { it.withAge(it.age + 1) }) })

        println("Tree with Alice's age increased: $tree")
        println("Original tree: $originalTree")
    }
}

//fun updateName(person: Person, newName: String) {
//    person.name = newName
//}
fun withUpdateName(person: Person, newName: String)= person.withName(newName)

//fun advanceAge(person: Person) {
//    person.age++
//}
fun withAdvanceAge(person: Person) = person.withAge(person.age + 1)

inline fun stats(crossinline block: () -> Unit) {
    val oldNodesCreated = nodesCreated
    val oldPersonsCreated = personsCreated
    val oldValueArraysCreated = valueArraysCreated
    val oldValueArraysCopies = valueArrayCopies
    block()
    println("""
        Nodes created:   ${nodesCreated - oldNodesCreated}
        Persons created: ${personsCreated - oldPersonsCreated}
        Arrays created:  ${valueArraysCreated - oldValueArraysCreated}
          copies:        ${valueArrayCopies - oldValueArraysCopies}
    """.trimIndent())
}