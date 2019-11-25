package uk.me.candle.twine;

import java.nio.ByteBuffer;

public class ByteBufferSliceable implements Sliceable<ByteBufferSliceable> {

    private final ByteBuffer wrapped;


    public ByteBufferSliceable(ByteBuffer wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ByteBufferSliceable slice(int offset, int length) {
        wrapped.position(offset);
        ByteBuffer result = wrapped.slice();
        result.limit(length);
        return new ByteBufferSliceable(result);
    }

    @Override
    public int size() {
        return wrapped.limit();
    }

    @Override
    public String toString() {
        return new String(toArray());
    }

    private byte[] toArray() {
        byte[] result = new byte[wrapped.limit()];
        wrapped.rewind();
        wrapped.get(result);
        return result;
    }

}
