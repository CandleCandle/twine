package uk.me.candle.twine;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class BasicMutableRope<B, T extends Sliceable<B, T>> implements Rope<B, T> {

    private RopeNode<T> root;

    public BasicMutableRope(T... initialElements) {
        for (T element : initialElements) {
            if (root == null) {
                root = new LeafRopeNode<>(element);
            } else {
                this.root = new IntermediateRopeNode<>(this.root, new LeafRopeNode<>(element));
            }
        }
    }

    @Override
    public B get(int offset) {
        int position = offset;
        RopeNode<T> current = root;
        while (true) {
            if (current instanceof IntermediateRopeNode) {
                IntermediateRopeNode<T> cur = (IntermediateRopeNode<T>)current;
                if (cur.left().size() > position) {
                    current = cur.left();
                } else {
                    current = cur.right();
                    position -= cur.left().size();
                }
            } else {
                return ((LeafRopeNode<T>)current).item().get(position);
            }
        }
    }

    @Override
    public Rope<B, T> insert(int offset, T slice) {
        List<Rope<B, T>> ends = split(offset);
        return ends.get(0).append(slice).append(ends.get(1));
    }

    @Override
    public Rope<B, T> insert(int offset, Rope<B, T> slice) {
        List<Rope<B, T>> ends = split(offset);
        return ends.get(0).append(slice).append(ends.get(1));
    }

    @Override
    public Rope<B, T> delete(int offset, int length) {
        List<Rope<B, T>> first = split(offset);
        List<Rope<B, T>> second = split(offset + length);
        return first.get(0).append(second.get(1));
    }

    @Override
    public Rope<B, T> append(Rope other) {
        if (this.root == null) {
            this.root = ((BasicMutableRope)other).root;
        } else {
            this.root = new IntermediateRopeNode(this.root, ((BasicMutableRope)other).root);
        }
        return this;
    }

    @Override
    public Rope<B, T> append(Sliceable other) {
        if (this.root == null) {
            this.root = new LeafRopeNode(other);
        } else {
            this.root = new IntermediateRopeNode(this.root, new LeafRopeNode(other));
        }
        return this;
    }

    @Override
    public List<Rope<B, T>> split(int position) {
        // first case: split point is on a boundary.
        // second case: split point is not on a boundary.

        int sizeFirst = 0;
        BasicMutableRope<B, T> first = new BasicMutableRope<>();
        BasicMutableRope<B, T> second = new BasicMutableRope<>();
        for (T item : this) {
            if (sizeFirst < position) {
                int potentialNewLength = sizeFirst + item.size();
                if (potentialNewLength < position) {
                    // the whole item should be included on the first side of the split
                    first.append(item);
                    sizeFirst = potentialNewLength;
                } else {
                    int slicePosition = position - sizeFirst;
                    // If we would generate a zero-length item, then append the
                    // whole item to either the first or second as appropriate.
                    if (slicePosition == 0) {
                        second.append(item);
                    } else if (slicePosition == item.size()) {
                        first.append(item);
                    } else {
                        IntermediateRopeNode<T> split = new LeafRopeNode(item).split(slicePosition);
                        first.append(((LeafRopeNode)split.left()).item());
                        second.append(((LeafRopeNode)split.right()).item());
                    }
                    sizeFirst = first.size();
                }
            } else {
                // everything else goes into the second half.
                second.append(item);
            }
        }
        return List.of(first, second);
    }

    @Override
    public Rope<B, T> slice(int offset, int length) {
        return split(offset + length).get(0).split(offset).get(1);
    }

    public void printTree(PrintStream out) {
        printTree(out, root, 0);
    }
    void printTree(PrintStream out, RopeNode node, int depth) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < depth; ++i) {
            line.append("  ");
        }
        if (node instanceof LeafRopeNode) {
            line.append("|* (").append( ((LeafRopeNode<T>)node).item().toString() ).append(')');
            out.println(line.toString());
        }
        if (node instanceof IntermediateRopeNode) {
            IntermediateRopeNode<T> intermediate = (IntermediateRopeNode<T>)node;
            line.append("|- (").append(intermediate.size() ).append(')');
            out.println(line.toString());
            printTree(out, intermediate.left(), depth+1);
            printTree(out, intermediate.right(), depth+1);
        }
    }

    @Override
    public int size() {
        if (root == null) return 0;
        return root.size();
    }

    @Override
    public Iterator<T> iterator() {
        if (root == null) return Collections.emptyIterator();
        // lazy-ish iterator, convert to a list first.
        List<T> result = new ArrayList<>();
        Deque<RopeNode<T>> items = new ArrayDeque<>();
        items.push(root);
        while (items.size() > 0) {
            RopeNode current = items.pop();
            if (current instanceof IntermediateRopeNode) {
                items.push(((IntermediateRopeNode<T>)current).right);
                items.push(((IntermediateRopeNode<T>)current).left);
            } else if (current instanceof LeafRopeNode) {
                result.add(((LeafRopeNode<T>)current).item());
            }
        }
        return result.iterator();
    }

    interface RopeNode<T extends Sliceable> {
        int size();
    }

    static class IntermediateRopeNode<T extends Sliceable> implements RopeNode<T> {
        final int size;
        final RopeNode<T> left;
        final RopeNode<T> right;

        public IntermediateRopeNode(RopeNode<T> left, RopeNode<T> right) {
            this.left = left;
            this.right = right;
            this.size = left.size() + right.size();
        }

        @Override public int size() { return size; }
        public RopeNode<T> left() { return left; }
        public RopeNode<T> right() { return right; }
    }

    static class LeafRopeNode<T extends Sliceable> implements RopeNode<T> {
        final T item;

        public LeafRopeNode(T item) {
            this.item = item;
        }

        IntermediateRopeNode<T> split(int offset) {
            T l = (T)item.slice(0, offset);
            T r = (T)item.slice(offset, item.size()-offset);
            return new IntermediateRopeNode<>(
                    new LeafRopeNode<>(l),
                    new LeafRopeNode<>(r)
            );
        }

        @Override public int size() { return item.size(); }
        public T item() { return item; }
    }
}

