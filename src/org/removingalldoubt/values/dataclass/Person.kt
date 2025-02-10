package org.removingalldoubt.values.dataclass

var personsCreated = 0

data class Person(
    val name: String,
    val age: Int,
) {
    init { personsCreated++ }
}
