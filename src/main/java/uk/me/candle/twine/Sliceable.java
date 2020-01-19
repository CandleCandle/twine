package uk.me.candle.twine;

/**
 * Provides the minimal interface for sequences of elements.
 *
 * @param <B> Type of element that is not dividable. e.g. `Byte` or `Character`
 * @param <S> Self referential generics. Should be the implementation type.
 */
public interface Sliceable<E, S extends Sliceable> {

    S slice(int offset, int length);

    E get(int offset);

    int size();
}
