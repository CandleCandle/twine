package uk.me.candle.twine;

public class CharSeqSliceable implements Sliceable<CharSeqSliceable> {

    private final CharSequence wrapped;

    public CharSeqSliceable(CharSequence wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int size() {
        return wrapped.length();
    }

    @Override
    public CharSeqSliceable slice(int offset, int length) {
        return new CharSeqSliceable(wrapped.subSequence(offset, offset + length));
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }
}
