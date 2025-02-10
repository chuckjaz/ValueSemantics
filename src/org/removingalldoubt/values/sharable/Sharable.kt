package org.removingalldoubt.values.sharable

/**
 * Instances that support this interface must obey the sharable semantics.
 *
 * The sharable semantics are,
 * - [Sharable] instances are deeply immutable when shared.
 * - [Sharable] instances are deeply mutable when owned.
 * - [Sharable] instances are created owned.
 * - Modifying a shared [Sharable] instance will throw an exception.
 * - "Depply" means it affects all instances in the "deep" set the [Sharable] instance.
 * - An instance is in the "deep" set of a sharable instance is if it the value of a var property of the [Sharable]
 *   instance or is in the deep set of another instance in the deep set. An instance that does not support
 *   [Sharable] is not part of the "deep" set even if it has a var property that instance returns a [Sharable] instance.
 * - All [Sharable] instances returned by a `val` property or returned from a method are shared.
 * - All [Sharable] objects returned by a 'var' property are shared if the instance is shared or owned if the instance is
 *   owned. Reference to owned [Sharable] instances are limited by the borrowing rules below.
 * - Modifying a property of a owned [Sharable] instance will only affect the owned instance, not any other instance.
 * - Modifying an owned [Sharable] object through a var property will be visible through the the owned [Sharable]
 *   instance but not through any other reference not borrowed from the instance.
 * - Owned instance returned by a var property of a [Sharable] instance of a var property are considered consumed
 *   once used (as in linear logic). They are valid only for the lifetime of the expression for which they are a part.
 * - Calling [Object.toString], [Object.hashCode] or any other [Object] method does not affect the owned state of the
 *   object.
 * - A method that neither calls [Sharable.toOwned] or [Sharable.toShared], even transitively, nor preserves a reference
 *   to the object, can borrow a shared instance. That is, [println] is safe to call on a [Sharable] object.
 *
 *  Undefined behavior,
 *  - Two live references to the same owned [Sharable] instance is undefined unless the reference is borrowed by the
 *    method called from the owner of the reference or ownership is transferred to the callee, or ownership is
 *    temporarily borrowed from a var property as described above. The borrow of an owned instance can either returned
 *    by the callee or consumed by the callee. If the ownership is consumed by the callee, the caller should drop the
 *    reference (i.e. the ownership moves to the callee). If the callee does not consume the value, the caller can be
 *    assumed to continue to own the value. If moving or borrowing the reference cannot be proven statically the caller
 *    must reassert ownership (by calling [Sharable.toOwned]) again.
 *
 * Convention,
 *  - val references to a sharable should be shared.
 *  - var references to a sharable should be owned.
 *
 *  Expectations,
 *  - calling [Sharable.toOwned] on an owned instance is O(1). It returns immediately with no allocation
 *  - calling [Sharable.toOwned] on a shareable instance will is O(1). It will return a copy of the instance that is
 *    in a owned state.  The first read of any property of a instance in the deep set of the instance may cause the
 *    instance to be copied.
 *  - calling [Sharable.toShared] on an instance that is sharable is O(1). It returns immediately with no allocations.
 *  - calling [Sharable.toShared] on an instance in an owned state is O(N) where N is the number of owned instances in
 *    the "deep" set of the instance. The instances set into the shared state in place with no memory allocation.
 *  - The result of [Sharable.toShared] is an immutable object and thread safe.
 *  - The result of [Sharable.toOwned] is a mutable object and is not thread safe.
 */
interface Sharable<T> {
    /**
     * Returns a shared instance of [T]. A shared instance of [T] is deeply immutable as described in [Sharable].
     */
    fun toShared(): T

    /**
     * Returns an owned instance of [T]. An owned instance of [T] is deeply mutable as described in [Sharable].
     */
    fun toOwned(): T
}
