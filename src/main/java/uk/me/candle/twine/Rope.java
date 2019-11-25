package uk.me.candle.twine;

import java.util.List;


public interface Rope<T extends Sliceable> extends Iterable<T> {
    /**
     * Inserts 'other' into a Rope.
     * May return a new instance of a Rope.
     * @param offset 'other' is inserted at this offset.
     * @param other Sliceable to insert at 'offset'
     * @return a Rope that contains 'other' at the specified offset, may return 'this'.
     */
    Rope<T> insert(int offset, T other);
    /**
     * Inserts 'other' into a Rope.
     * May return a new instance of a Rope.
     * @param offset 'other' is inserted at this offset.
     * @param other Rope to insert at 'offset'
     * @return a Rope that contains 'other' at the specified offset, may return 'this'.
     */
    Rope<T> insert(int offset, Rope<T> other);
    /**
     * Removes 'length' elements starting at 'offset'.
     * @param offset starting offset at which to start to remove elements.
     * @param length number of elements to remove.
     * @return a Rope without the specified elements, may return 'this'.
     */
    Rope<T> delete(int offset, int length);
    /**
     * Appends 'other' to the end of this rope.
     * @param other Rope that should be appended to this Rope.
     * @return a rope that also contains 'other', may return 'this'.
     */
    Rope<T> append(Rope<T> other);
    /**
     * Appends 'other' to the end of this rope.
     * @param other Sliceable that should be appended to this Rope.
     * @return a rope that also contains 'other', may return 'this'.
     */
    Rope<T> append(T other);
    /**
     * Splits a rope into two, with 'position' as the first element of the second Rope.
     * @param position element at this position becomes the first element of the second Rope.
     * @return List of size two, the two sections of this rope split at position, may return 'this' as one of the two sections.
     */
    List<Rope<T>> split(int position);

    /**
     * Count of elements on this Rope.
     * @return the number of elements in this Rope.
     */
    int size();
}
