package org.removingalldoubt.values.sharable

fun sharableSample() {
    stats {
        val alice = Person(
            name = "Alice",
            age = 35
        ).toShared() // val are shared references. Sharable instance are born owned so need to be converted to shared.
        var bob = Person(name = "Bob", age = 42) // var are owned references, shareable objects are created owned.
        val carl = Person(name = "Carl", age = 21).toShared()

        println("Original bob: $bob") // The toShared() of bob can be elided if it can be proven that toShared() and toOwned() are not called by the callee.
        val originalBob = bob.toShared() // bob is no longer owned as ownership transferred to the copy

        println("Original bob: $originalBob, equal?: ${bob == originalBob}, identity equal?: ${bob === originalBob}")

        bob = bob.toOwned() // Ownership of bob needs to be re-established because bob was copied
        bob.age++
        bob.name = "Robert"

        println("Updated bob $bob")
        println("Original bob $originalBob")

        var people = ValueArray(alice, bob.toShared()) // A shared copy of bob must be passed as bob is used below
        val originalPeople = people.toShared() // people is no longer owned as ownership transferred to the copy
        println("people: $people")
        println("original people: $originalPeople")

        people = people.toOwned() // Ownership of "people" must be re-established as people was copied
        people.add(carl)
        println("People: $people, size: ${people.size}")
        println("Original $originalPeople, size: ${originalPeople.size}")

        people[1].name = "Bob"
        println("Modify Bob's name in place $people")

        people[1] = originalBob
        println("Back to original in place $people")

        updateName(people[1], "Robert") // Ownership is of person is borrowed
        advanceAge(people[1]) // Ownership of person is borrowed
        println("Back to Robert in place with abstraction over update: $people")

        people.remove(carl)
        println("original people: $originalPeople")
        println("Back to original list, $people, people == originalPeople = ${people == originalPeople}")

        // Tree of people
        // Ownership of bob moves to Node as this is the last reference to bob
        var tree = Node(bob, Node(alice), Node(carl))

        println("Tree: $tree")
        val originalTree = tree.toShared() // tree is no longer owned as ownership moved to the copy

        tree = tree.toOwned()
        tree.left?.let { it.value.age++ }

        println("Tree with Alice's age increased: $tree")
        println("Original tree: $originalTree")
    }
}

// fun updatePerson(var person: Person, newName: string) {
//   person.name = newName
// }
fun updateName(person: Person, newName: String) {
    person.name = newName
}

// fun advanceAge(var person: Person) {
//   person.age++
// }
fun advanceAge(person: Person) {
    person.advanceAge()
}

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