package es.ing.aoc.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TreeNode implements Comparable<TreeNode> {

    public Integer value;
    public TreeNode parent;
    public List<TreeNode> elements = new ArrayList<>();
    public boolean isDivider = false;

    public boolean isLeafNode() {
        return value != null;
    }

    public String toString() {
        return value != null ? String.valueOf(value) : elements.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode node = (TreeNode) o;

        if (!Objects.equals(value, node.value)) return false;
        return Objects.equals(elements, node.elements);
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (elements != null ? elements.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(TreeNode other) {
        int comp = 0;

        if (this.isLeafNode() && other.isLeafNode()) {
            comp = Integer.compare(this.value, other.value);

        } else if (!this.isLeafNode() && !other.isLeafNode()) {
            int i = 0;
            while (comp == 0 && i < Math.max(this.elements.size(), other.elements.size())) {
                if (i >= this.elements.size()) {
                    comp = -1;
                } else if (i >= other.elements.size()) {
                    comp = 1;
                } else {
                    comp = this.elements.get(i).compareTo(other.elements.get(i));
                }
                i++;
            }

        } else {
            if (this.isLeafNode()) {
                return wrapNode(this).compareTo(other);
            } else {
                return this.compareTo(wrapNode(other));
            }
        }

        return comp;
    }

    private static TreeNode wrapNode(TreeNode n) {
        TreeNode wrapNode = new TreeNode();
        wrapNode.elements.add(n);
        return wrapNode;
    }
}
