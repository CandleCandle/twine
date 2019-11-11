package uk.me.candle.twine;

public class CharSeqSliceable implements Sliceable {

    private final CharSequence wrapped;

    public CharSeqSliceable(CharSequence wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int size() {
        return wrapped.length();
    }

    @Override
    public Sliceable slice(int offset, int length) {
        return new CharSeqSliceable(wrapped.subSequence(offset, offset + length));
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }
}
