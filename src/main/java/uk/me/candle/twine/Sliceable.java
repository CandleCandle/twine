package uk.me.candle.twine;

public interface Sliceable {

    Sliceable slice(int offset, int length);

    int size();
}
