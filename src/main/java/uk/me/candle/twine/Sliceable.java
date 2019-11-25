package uk.me.candle.twine;

public interface Sliceable<E extends Sliceable> {

    E slice(int offset, int length);

    int size();
}
