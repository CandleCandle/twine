package uk.me.candle.twine;

import java.nio.ByteBuffer;
import java.util.stream.Stream;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ByteBufferSliceableTest {

    @ParameterizedTest
    @MethodSource("slicesParams")
    public void slicesDirect(int from, int length, String result) throws Exception {
        ByteBuffer direct = ByteBuffer.allocateDirect(10);
        direct.put(new byte[]{0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A});
        ByteBufferSliceable undertest = new ByteBufferSliceable(direct);
        assertThat(undertest.slice(from, length).toString(), is(result));
    }

    @ParameterizedTest
    @MethodSource("slicesParams")
    public void slicesHeap(int from, int length, String expected) throws Exception {
        ByteBufferSliceable undertest = new ByteBufferSliceable(
                ByteBuffer.wrap(new byte[]{0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A})
        );

        Sliceable result = undertest.slice(from, length);
        assertThat(result.size(), is(length));
        assertThat(result.toString(), is(expected));
    }

    static Stream<Arguments> slicesParams() {
        return Stream.of(
            Arguments.of(0, 2, "ab"),
            Arguments.of(0, 5, "abcde"),
            Arguments.of(4, 4, "efgh"),
            Arguments.of(0, 2, "ab")
        );
    }

    @Test
    public void itSizesDirect() {
        ByteBuffer direct = ByteBuffer.allocateDirect(10);
        direct.put(new byte[]{0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A});
        ByteBufferSliceable undertest = new ByteBufferSliceable(direct);
        assertThat(undertest.size(), is(10));
    }

    @Test
    public void itSizesHeap() {
        ByteBufferSliceable undertest = new ByteBufferSliceable(
                ByteBuffer.wrap(new byte[]{0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A})
        );
        assertThat(undertest.size(), is(10));
    }

}