package es.ing.aoc.common;

import java.util.ArrayList;
import java.util.List;

public final class TreeNode implements Comparable<TreeNode> {

    private Integer value;
    private TreeNode parent;
    private List<TreeNode> elements = new ArrayList<>();

    public boolean isLeafNode() {
        return value != null;
    }

    public Integer getValue() {
        return value;
    }

    public TreeNode withValue(Integer value) {
        this.value = value;
        return this;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode withParent(TreeNode parent) {
        this.parent = parent;
        return this;
    }

    public List<TreeNode> getElements() {
        return elements;
    }

    public TreeNode withElements(List<TreeNode> elements) {
        this.elements = elements;
        return this;
    }

    public String toString() {
        return value != null ? String.valueOf(value) : elements.toString();
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
