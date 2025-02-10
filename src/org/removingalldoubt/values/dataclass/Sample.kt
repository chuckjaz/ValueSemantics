package org.removingalldoubt.values.dataclass

fun dataClassSample() {
    stats {
        val alice = Person(name = "Alice", age = 35)
        var bob = Person(name = "Bob", age = 42)
        val carl = Person(name = "Carl", age = 21)

        println("Original bob: $bob")
        val originalBob = bob

        val unmodifiedBob = bob.copy(name = "Bob")
        println("Unmodified bob: $unmodifiedBob, equal?: ${bob == unmodifiedBob}, identity equal?: ${bob === unmodifiedBob}")

        // bob.age++
        // bob.name = "Robert"
        bob = bob.copy(age = bob.age + 1, name = "Robert")

        println("Updated bob $bob")
        println("Copy of original bob $originalBob")

        var people = ValueList(alice, bob)
        val originalPeople = people

        // people.add(carl)
        people = people.add(carl)
        println("People: $people")
        println("Original $originalPeople")

        // people[1].name = "Bob"
        people = people.set(1, people[1].copy(name = "Bobby"))
        println("Modify Bob's name in place $people")

        // people[1] = originalBob
        people = people.set(1, originalBob)
        println("Back to original in place $people")

        // updateName(person[1], "Robert")
        // advanceAge(person[1])
        people = people.set(1, updateName(people[1], "Robert"))
        people = people.set(1, advanceAge(people[1]))
        println("Back to Robert in place with abstraction over update: $people")

        // people.remove(carl)
        people = people.remove(carl)
        println("Back to original list, $people, people == originalPeople = ${people == originalPeople}")

        // Tree of people
        var tree = Node(bob, Node(alice), Node(carl))

        println("Tree: $tree")

        // tree.left?.age++
        tree = tree.copy(left = tree.left?.let { it.copy(value = it.value.let { it.copy(age = it.age + 1) }) })
        println("Tree with Alice's age increased: $tree")
    }
}

// fun updatePerson(var person: Person, newName: string) {
//   person.name = newName
// }
fun updateName(person: Person, newName: String): Person {
    return person.copy(name = newName)
}

// fun advanceAge(var person: Person) {
//   person.age++
// }
fun advanceAge(person: Person): Person {
    return person.copy(age = person.age + 1)
}


inline fun stats(crossinline block: () -> Unit) {
    val oldNodesCreated = nodesCreated
    val oldPersonsCreated = personsCreated
    val oldValueArraysCreated = valueListsCreated
    block()
    println("""
        Nodes created:   ${nodesCreated - oldNodesCreated}
        Persons created: ${personsCreated - oldPersonsCreated}
        Arrays created:  ${valueListsCreated - oldValueArraysCreated}
    """.trimIndent())
}