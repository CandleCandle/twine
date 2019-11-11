package uk.me.candle.twine;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;


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
}