package uk.me.candle.twine;

public interface Sliceable<B, E extends Sliceable> {

    E slice(int offset, int length);

    B get(int offset);

    int size();
}
