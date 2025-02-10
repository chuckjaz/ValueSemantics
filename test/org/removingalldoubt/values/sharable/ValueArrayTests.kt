package org.removingalldoubt.values.sharable

import org.removingalldoubt.values.expect
import org.removingalldoubt.values.toEqual
import org.testng.annotations.Test

class ValueArrayTests {
    @Test
    fun canCreateAnEmptyValueArray() {
        val array = valueArrayOf<String>()
        expect(array.size).toEqual(0)
    }

    @Test
    fun canCreateAValueArray() {
        val array = valueArrayOf(1, 2, 3).toShared()
        expect(array.size).toEqual(3)
        expect(array[0]).toEqual(1)
        expect(array[1]).toEqual(2)
        expect(array[2]).toEqual(3)
    }

    @Test
    fun canModifyTheArray() {
        var array = ValueArray(1, 2, 3)
        val copy = array.toShared()
        array = array.toOwned()
        array.add(4)
        expect(array).toEqual(valueArrayOf(1, 2, 3, 4))
        expect(copy).toEqual(valueArrayOf(1, 2, 3))
    }

    @Test
    fun canCreateAnArrayOfPeople() = stats {
        val alice = Person(name = "Alice", 35).toShared()
        val bob = Person("Bob", 42).toShared()
        val carl = Person("Carl", 52).toShared()
        var array = valueArrayOf(alice, bob)
        val original = array.toShared()
        array = array.toOwned()
        array.add(carl)
        array[0].age++
        expect(array).toEqual(valueArrayOf(Person("Alice", 36), bob, carl))
        expect(original).toEqual(valueArrayOf(alice, bob))
        array.remove(carl)
        array[0].age--
        expect(array).toEqual(original)
    }
}