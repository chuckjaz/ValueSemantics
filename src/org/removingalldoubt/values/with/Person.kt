package org.removingalldoubt.values.with

var personsCreated = 0

class Person(name: String, age: Int) {
    init { personsCreated++ }
    private val _name = name
    val name: String get() = _name
    fun withName(value: String): Person {
        return Person(value, _age)
    }

    private val _age = age
    val age: Int get() = _age
    fun withAge(value: Int): Person {
        return Person(_name, value)
    }

    // copy fun advanceAge() { age++ }
    fun withAdvanceAge() = withAge(age + 1)

    override fun hashCode(): Int = (this::class.hashCode() * 31 + name.hashCode()) * 31 + age.hashCode()
    override fun equals(other: Any?): Boolean = this === other ||
            (other is Person && name == other.name && age == other.age)

    override fun toString(): String = "Person($name, $age)"
}