package org.removingalldoubt.values.sharable

var personsCreated = 0

class Person(name: String, age: Int) : Sharable<Person> {
    private var _name = name
    private var _age = age
    private var shared = false

    init { personsCreated++ }

    var name: String
        get() = _name
        set(value) {
            assertOwned()
            _name = value
        }

    var age: Int
        get() = _age
        set(value) {
            assertOwned()
            _age = value
        }

    fun advanceAge() { age++ }

    override fun hashCode(): Int = (this::class.hashCode() * 31 + name.hashCode()) * 31 + age.hashCode()
    override fun equals(other: Any?): Boolean = this === other ||
            (other is Person && name == other.name && age == other.age)

    override fun toString(): String = "Person($name, $age)"

    override fun toShared(): Person {
        if (!shared) {
            shared = true
        }
        return this
    }

    override fun toOwned(): Person {
        if (shared) {
            return Person(name, age)
        }
        return this
    }

    private fun assertOwned() {
        check(!shared) { "$this is not owned"}
    }
}

