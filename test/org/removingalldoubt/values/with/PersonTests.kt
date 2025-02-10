package org.removingalldoubt.values.with

import org.removingalldoubt.values.expect
import org.removingalldoubt.values.toEqual
import org.removingalldoubt.values.toNotEqual
import org.testng.annotations.Test

class PersonTests {
    @Test
    fun canCreateAPerson() {
        val person = Person(name = "Bob", age = 42)
        expect(person.name).toEqual("Bob")
        expect(person.age).toEqual(42)
    }

    @Test
    fun personChangesAreIsolated() {
        var person = Person(name = "Bob", age = 42)
        val original = person
        // person.name = "Robert"
        person = person.withName("Robert")
        // person.age++
        person = person.withAge(person.age + 1)
        expect(person).toNotEqual(original)
        expect(person.name).toEqual("Robert")
        expect(person.age).toEqual(43)
        expect(original.name).toEqual("Bob")
        expect(original.age).toEqual(42)
    }
}