package uk.me.candle.twine;

import java.util.stream.Stream;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class CharSeqSliceableTest {

    @Test
    public void itSlices() {
        CharSeqSliceable undertest = new CharSeqSliceable("some content");
        assertThat(undertest.slice(0, 5).toString(), is("some "));
        assertThat(undertest.slice(5, 7).toString(), is("content"));
    }

    @Test
    public void itSizesHeap() {
        CharSeqSliceable undertest = new CharSeqSliceable("some content");
        assertThat(undertest.size(), is(12));
    }

    @ParameterizedTest
    @MethodSource("getParams")
    public void itCanAccessIndividualElements(String input, int offset, char expected) throws Exception {
        CharSeqSliceable undertest = new CharSeqSliceable(input);
        assertThat(undertest.get(offset), is(expected));
    }

    static Stream<Arguments> getParams() {
        return Stream.of(
            Arguments.of("some content", 0, 's'),
            Arguments.of("some content", 4, ' '),
            Arguments.of("some content", 7, 'n')
        );
    }

}