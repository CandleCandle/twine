package uk.me.candle.twine;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class BasicMutableRope implements Rope {

    private RopeNode root;

    public BasicMutableRope(Sliceable... initialElements) {
        for (Sliceable element : initialElements) {
            if (root == null) {
                root = new LeafRopeNode(element);
            } else {
                this.root = new IntermediateRopeNode(this.root, new LeafRopeNode(element));
            }
        }
    }

    @Override
    public Rope insert(int offset, Sliceable slice) {
        Rope[] ends = split(offset);
        return ends[0].append(slice).append(ends[1]);
    }

    @Override
    public Rope insert(int offset, Rope slice) {
        Rope[] ends = split(offset);
        return ends[0].append(slice).append(ends[1]);
    }

    @Override
    public Rope delete(int offset, int length) {
        Rope[] first = split(offset);
        Rope[] second = split(offset + length);
        return first[0].append(second[1]);
    }

    @Override
    public Rope append(Rope other) {
        if (this.root == null) {
            this.root = ((BasicMutableRope)other).root;
        } else {
            this.root = new IntermediateRopeNode(this.root, ((BasicMutableRope)other).root);
        }
        return this;
    }

    @Override
    public Rope append(Sliceable other) {
        if (this.root == null) {
            this.root = new LeafRopeNode(other);
        } else {
            this.root = new IntermediateRopeNode(this.root, new LeafRopeNode(other));
        }
        return this;
    }

    @Override
    public Rope[] split(int position) {
        // first case: split point is on a boundary.
        // second case: split point is not on a boundary.

        int sizeFirst = 0;
        BasicMutableRope first = new BasicMutableRope();
        BasicMutableRope second = new BasicMutableRope();
        for (Sliceable item : this) {
            if (sizeFirst < position) {
                int potentialNewLength = sizeFirst + item.size();
                if (potentialNewLength < position) {
                    // the whole item should be included on the first side of the split
                    first.append(item);
                    sizeFirst = potentialNewLength;
                } else {
                    IntermediateRopeNode split = new LeafRopeNode(item).split(position - sizeFirst);
                    first.append(((LeafRopeNode)split.left).item);
                    second.append(((LeafRopeNode)split.right).item);
                    sizeFirst = first.size();
                }
            } else {
                // everything else goes into the second half.
                second.append(item);
            }
        }
        return new Rope[]{first, second};
    }

    void printTree(PrintStream out) {
        printTree(out, root, 0);
    }
    void printTree(PrintStream out, RopeNode node, int depth) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < depth; ++i) {
            line.append("  ");
        }
        if (node instanceof LeafRopeNode) {
            line.append("|* (").append( ((LeafRopeNode)node).item.toString() ).append(')');
            out.println(line.toString());
        }
        if (node instanceof IntermediateRopeNode) {
            IntermediateRopeNode intermediate = (IntermediateRopeNode)node;
            line.append("|- (").append(intermediate.size ).append(')');
            out.println(line.toString());
            printTree(out, intermediate.left, depth+1);
            printTree(out, intermediate.right, depth+1);
        }
    }

    @Override
    public int size() {
        if (root == null) return 0;
        return root.size();
    }

    @Override
    public Iterator<Sliceable> iterator() {
        if (root == null) return Collections.emptyIterator();
        // lazy-ish iterator, convert to a list first.
        List<Sliceable> result = new ArrayList<>();
        Deque<RopeNode> items = new ArrayDeque<>();
        items.push(root);
        while (items.size() > 0) {
            RopeNode current = items.pop();
            if (current instanceof IntermediateRopeNode) {
                items.push(((IntermediateRopeNode)current).right);
                items.push(((IntermediateRopeNode)current).left);
            } else if (current instanceof LeafRopeNode) {
                result.add(((LeafRopeNode)current).item);
            }
        }
        return result.iterator();
    }

    interface RopeNode {
        int size();
    }

    static class IntermediateRopeNode implements RopeNode {
        final int size;
        final RopeNode left;
        final RopeNode right;

        public IntermediateRopeNode(RopeNode left, RopeNode right) {
            this.left = left;
            this.right = right;
            this.size = left.size() + right.size();
        }

        @Override
        public int size() {
            return size;
        }
    }

    static class LeafRopeNode implements RopeNode {
        final Sliceable item;

        public LeafRopeNode(Sliceable item) {
            this.item = item;
        }

        IntermediateRopeNode split(int offset) {
            Sliceable l = item.slice(0, offset);
            Sliceable r = item.slice(offset, item.size()-offset);
            return new IntermediateRopeNode(
                    new LeafRopeNode(l),
                    new LeafRopeNode(r)
            );
        }

        @Override
        public int size() {
            return item.size();
        }
    }
}

