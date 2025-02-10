@file:Suppress("CanBeVal")

package org.removingalldoubt.values.sharable

import org.removingalldoubt.values.expect
import org.removingalldoubt.values.toEqual
import org.removingalldoubt.values.toNotEqual
import org.testng.annotations.Test

class PersonTests {
    @Test
    fun canCreateAPerson() {
        val person = Person(name = "Bob", age = 42).toShared()
        expect(person.name).toEqual("Bob")
        expect(person.age).toEqual(42)
    }

    @Test
    fun personChangesAreIsolated() {
        var person = Person(name = "Bob", age = 42)
        val original = person.toShared()
        person = person.toOwned()
        person.name = "Robert"
        person.age++
        expect(person).toNotEqual(original)
        expect(person.name).toEqual("Robert")
        expect(person.age).toEqual(43)
        expect(original.name).toEqual("Bob")
        expect(original.age).toEqual(42)
    }
}

