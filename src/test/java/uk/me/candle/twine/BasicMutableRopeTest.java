package uk.me.candle.twine;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/*
 * This set of tests that could be made more generic and run against
 * any/all Rope implementations.
 */
public class BasicMutableRopeTest {

    @Test
    public void itCanIterateOverASimpleRope() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"));
        Iterator<CharSeqSliceable> result = undertest.iterator();
        assertThat(result.hasNext(), is(true));
        assertThat(result.next().toString(), is("abcd"));
    }

    @Test
    public void itCanConcaternateTwoRopes() throws Exception {
        Rope<Character, CharSeqSliceable> first = new BasicMutableRope<>(new CharSeqSliceable("abcd"));
        Rope<Character, CharSeqSliceable> second = new BasicMutableRope<>(new CharSeqSliceable("efgh"));
        Rope<Character, CharSeqSliceable> result = first.append(second);

        assertThat(ropeToString(result), is("abcdefgh"));
    }

    @Test
    public void itCanAppendNewElements() throws Exception {
        Rope<Character, CharSeqSliceable> first = new BasicMutableRope<>(new CharSeqSliceable("abcd"));
        CharSeqSliceable second = new CharSeqSliceable("efgh");
        Rope<Character, CharSeqSliceable> result = first.append(second);

        assertThat(ropeToString(result), is("abcdefgh"));
    }

    @Test
    public void itCanCreateFromMultipleElements() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"));
        ((BasicMutableRope)undertest).printTree(System.out);
        assertThat(ropeToString(undertest), is("abcdefgh"));
    }

    @Test
    public void itCanSplitOnSliceableBoundaries() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"));
        List<Rope<Character, CharSeqSliceable>> results = undertest.split(4);
        assertThat(ropeToString(results.get(0)), is("abcd"));
        assertThat(ropeToString(results.get(1)), is("efgh"));
    }

    @Test
    public void itCanSplitInTheMiddleOfASliceable() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        ((BasicMutableRope)undertest).printTree(System.out);
        List<Rope<Character, CharSeqSliceable>> results = undertest.split(6);
        assertThat(ropeToString(results.get(0)), is("abcdef"));
        assertThat(ropeToString(results.get(1)), is("ghijkl"));
    }

    @Test
    public void itCanSplitToAZeroLengthRope() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        List<Rope<Character, CharSeqSliceable>> results = undertest.split(0);
        assertThat(ropeToString(results.get(0)), is(""));
        assertThat(ropeToString(results.get(1)), is("abcdefghijkl"));
    }

    @Test
    public void itCanInsertSliceableOnSliceableBoundaries() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("ijkl"));
        CharSeqSliceable toInsert = new CharSeqSliceable("efgh");
        Rope<Character, CharSeqSliceable> result = undertest.insert(4, toInsert);
        assertThat(ropeToString(result), is("abcdefghijkl"));
    }

    @Test
    public void itCanInsertRopeOnSliceableBoundaries() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("ijkl"));
        Rope<Character, CharSeqSliceable> toInsert = new BasicMutableRope<>(new CharSeqSliceable("efgh"));
        Rope<Character, CharSeqSliceable> result = undertest.insert(4, toInsert);
        assertThat(ropeToString(result), is("abcdefghijkl"));
    }

    @Test
    public void itCanInsertSliceableOnAnyBoundary() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcdijkl"));
        CharSeqSliceable toInsert = new CharSeqSliceable("efgh");
        Rope<Character, CharSeqSliceable> result = undertest.insert(4, toInsert);
        assertThat(ropeToString(result), is("abcdefghijkl"));
    }

    @Test
    public void itCanInsertRopeOnAnyBoundary() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcdijkl"));
        Rope<Character, CharSeqSliceable> toInsert = new BasicMutableRope<>(new CharSeqSliceable("efgh"));
        Rope<Character, CharSeqSliceable> result = undertest.insert(4, toInsert);
        assertThat(ropeToString(result), is("abcdefghijkl"));
    }

    @Test
    public void itCanDeleteAWholeElement() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope<Character, CharSeqSliceable> result = undertest.delete(4, 4); // should remove "efgh"
        assertThat(ropeToString(result), is("abcdijkl"));
    }

    @Test
    public void itCanDeleteFromTheStartOfAnElement() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope<Character, CharSeqSliceable> result = undertest.delete(4, 2); // should remove "ef"
        assertThat(ropeToString(result), is("abcdghijkl"));
    }

    @Test
    public void itCanDeleteFromTheEndOfAnElement() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope<Character, CharSeqSliceable> result = undertest.delete(6, 2); // should remove "gh"
        assertThat(ropeToString(result), is("abcdefijkl"));
    }

    @Test
    public void itCanDeleteFromTheMiddleOfAnElement() throws Exception {
        Rope<Character, CharSeqSliceable> undertest = new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"));
        Rope<Character, CharSeqSliceable> result = undertest.delete(5, 2); // should remove "fg"
        assertThat(ropeToString(result), is("abcdehijkl"));
    }

    @ParameterizedTest
    @MethodSource("getParams")
    public void itCanAccessIndividualElements(int offset, char expected, Rope<Character, CharSeqSliceable> input) throws Exception {
        assertThat(input.get(offset), is(expected));
    }

    static Stream<Arguments> getParams() {
        return Stream.of(
            Arguments.of(0, 'a', new BasicMutableRope<>(new CharSeqSliceable("abcd"))),
            Arguments.of(0, 'e', new BasicMutableRope<>(new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"))),
            Arguments.of(4, 'i', new BasicMutableRope<>(new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"))),
            Arguments.of(5, 'f', new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl"))),
            Arguments.of(10, 'k', new BasicMutableRope<>(new CharSeqSliceable("abcd"), new CharSeqSliceable("efgh"), new CharSeqSliceable("ijkl")))
        );
    }

    private String ropeToString(Rope<Character, CharSeqSliceable> undertest) {
        StringBuilder builder = new StringBuilder();
        for (CharSeqSliceable element : undertest) {
            builder.append(element.toString());
        }
        return builder.toString();
    }

}
