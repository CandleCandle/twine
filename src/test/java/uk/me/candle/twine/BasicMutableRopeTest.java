package uk.me.candle.twine;

import static org.junit.Assert.assertThat;
import java.util.Iterator;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Ignore;
import org.junit.Test;

public class BasicMutableRopeTest {

    @Test
    public void itCanIterateOverASimpleRope() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"));
        Iterator<Sliceable> result = undertest.iterator();
        assertThat(result.hasNext(), is(true));
        assertThat(result.next().toString(), is("abcd"));
    }

    @Test
    public void itCanConcaternateTwoRopes() throws Exception {
        Rope first = new BasicMutableRope(new CharSeqSliceable("abcd"));
        Rope second = new BasicMutableRope(new CharSeqSliceable("efgh"));
        Rope result = first.append(second);

        assertThat(ropeToString(result), is("abcdefgh"));
    }

    @Test
    public void itCanAppendNewElements() throws Exception {
        Rope first = new BasicMutableRope(new CharSeqSliceable("abcd"));
        Sliceable second = new CharSeqSliceable("efgh");
        Rope result = first.append(second);

        assertThat(ropeToString(result), is("abcdefgh"));
    }

    @Test
    public void itCanCreateFromMultipleElements() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"));
        ((BasicMutableRope)undertest).printTree(System.out);
        assertThat(ropeToString(undertest), is("abcdefgh"));
    }

    @Test
    public void itCanSplitOnSliceableBoundaries() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"));
        Rope[] results = undertest.split(4);
        assertThat(ropeToString(results[0]), is("abcd"));
        assertThat(ropeToString(results[1]), is("efgh"));
    }

    @Test
    public void itCanSplitInTheMiddleOfASliceable() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        ((BasicMutableRope)undertest).printTree(System.out);
        Rope[] results = undertest.split(6);
        assertThat(ropeToString(results[0]), is("abcdef"));
        assertThat(ropeToString(results[1]), is("ghijkl"));
    }

    @Test
    public void itCanSplitToAZeroLengthRope() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope[] results = undertest.split(0);
        assertThat(ropeToString(results[0]), is(""));
        assertThat(ropeToString(results[1]), is("abcdefghijkl"));
    }

    @Test
    public void itCanInsertSliceableOnSliceableBoundaries() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("ijkl"));
        Sliceable toInsert = new CharSeqSliceable("efgh");
        Rope result = undertest.insert(4, toInsert);
        assertThat(ropeToString(result), is("abcdefghijkl"));
    }

    @Test
    public void itCanInsertRopeOnSliceableBoundaries() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("ijkl"));
        Rope toInsert = new BasicMutableRope(new CharSeqSliceable("efgh"));
        Rope result = undertest.insert(4, toInsert);
        assertThat(ropeToString(result), is("abcdefghijkl"));
    }

    @Test
    public void itCanInsertSliceableOnAnyBoundary() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcdijkl"));
        Sliceable toInsert = new CharSeqSliceable("efgh");
        Rope result = undertest.insert(4, toInsert);
        assertThat(ropeToString(result), is("abcdefghijkl"));
    }

    @Test
    public void itCanInsertRopeOnAnyBoundary() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcdijkl"));
        Rope toInsert = new BasicMutableRope(new CharSeqSliceable("efgh"));
        Rope result = undertest.insert(4, toInsert);
        assertThat(ropeToString(result), is("abcdefghijkl"));
    }

    @Test
    public void itCanDeleteAWholeElement() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope result = undertest.delete(4, 4); // should remove "efgh"
        assertThat(ropeToString(result), is("abcdijkl"));
    }

    @Test
    public void itCanDeleteFromTheStartOfAnElement() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope result = undertest.delete(4, 2); // should remove "ef"
        assertThat(ropeToString(result), is("abcdghijkl"));
    }

    @Test
    public void itCanDeleteFromTheEndOfAnElement() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope result = undertest.delete(6, 2); // should remove "gh"
        assertThat(ropeToString(result), is("abcdefijkl"));
    }

    @Test
    public void itCanDeleteFromTheMiddleOfAnElement() throws Exception {
        Rope undertest = new BasicMutableRope(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope result = undertest.delete(5, 2); // should remove "fg"
        assertThat(ropeToString(result), is("abcdehijkl"));
    }

    // delete whole sliceable -> find sibling, replace parent with sibling.
    // delete from start of sliceable -> replace tree node with smaller.
    // delete from end of sliceable -> replace tree node with smaller.
    // delete from middle of sliceable -> replace tree node with intermediate, each side is the L and R of the original sliceable.

    private String ropeToString(Rope undertest) {
        StringBuilder builder = new StringBuilder();
        for (Sliceable element : undertest) {
            builder.append(element.toString());
        }
        return builder.toString();
    }

}
