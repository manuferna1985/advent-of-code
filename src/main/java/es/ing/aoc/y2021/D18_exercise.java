package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D18_exercise {

    public static class Node {
        Integer value;
        Node parent;
        Node left;
        Node right;

        public boolean isLeafNode() {
            return left == null && right == null;
        }

        public boolean isFinalPair() {
            return left != null && right != null && left.isLeafNode() && right.isLeafNode();
        }

        public String toString() {
            return value != null ? String.valueOf(value) : String.format("[%s,%s]", left, right);
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D18_exercise.class.getResource("2021/D18_full.txt").toURI()), Charset.defaultCharset());

        // Part 1
        List<Node> lines = new ArrayList<>();
        for (String line : allLines) {
            lines.add(createTree(line));
        }

        Node result = lines.remove(0);
        do {
            result = sumAndReduce(result, lines.remove(0));
        } while (!lines.isEmpty());

        System.out.println(result);
        System.out.println(getTreeMagnitude(result));

        // Part 2

        Long maxSumValue = Long.MIN_VALUE;

        for (int i = 0; i < allLines.size(); i++) {
            for (int j = 0; j < allLines.size(); j++) {
                if (i != j) {
                    Node combinationResult = sumAndReduce(createTree(allLines.get(i)), createTree(allLines.get(j)));
                    maxSumValue = Math.max(maxSumValue, getTreeMagnitude(combinationResult));
                }
            }
        }

        System.out.println(maxSumValue);
    }

    private static Node sumAndReduce(Node n1, Node n2) {

        Node root = new Node();
        root.left = n1;
        root.right = n2;
        n1.parent = root;
        n2.parent = root;

        return reduce(root);
    }

    private static Node reduce(Node node) {

        boolean reductionsDone;

        do {
            reductionsDone = false;

            Node explodingCandidate = findLeftMostCandidateForExplode(node, 0);
            if (explodingCandidate != null) {
                reductionsDone = true;

                useLeftExplodedPart(node, explodingCandidate);
                useRightExplodedPart(node, explodingCandidate);

                explodingCandidate.left = null;
                explodingCandidate.right = null;
                explodingCandidate.value = 0;

            } else {

                Node splittingCandidate = findLeftMostGeThan10RegularNumber(node);

                if (splittingCandidate != null) {
                    reductionsDone = true;
                    splitNode(splittingCandidate);
                }
            }

        } while (reductionsDone);

        return node;
    }

    private static void useLeftExplodedPart(Node node, Node exploding) {

        List<Node> orderedTree = new ArrayList<>();
        orderTree(node, orderedTree);

        int current = orderedTree.indexOf(exploding.left);
        if (current > 0) {
            orderedTree.get(current - 1).value += exploding.left.value;
        }
    }

    private static void useRightExplodedPart(Node node, Node exploding) {

        List<Node> orderedTree = new ArrayList<>();
        orderTree(node, orderedTree);

        int current = orderedTree.indexOf(exploding.right);
        if (current < orderedTree.size() - 1) {
            orderedTree.get(current + 1).value += exploding.right.value;
        }
    }

    private static void splitNode(Node nodeToSplit) {

        Node left = new Node();
        left.parent = nodeToSplit;
        left.value = (int) Math.floor(nodeToSplit.value / 2.0);

        Node right = new Node();
        right.parent = nodeToSplit;
        right.value = (int) Math.ceil(nodeToSplit.value / 2.0);

        nodeToSplit.left = left;
        nodeToSplit.right = right;
        nodeToSplit.value = null;
    }

    private static Node createTree(String line) {

        Node n = new Node();

        if (line.charAt(0) == '[') {
            int commaPos = findSameDepthComma(line);
            int closingBracketPos = findClosingBracket(line);
            String left = line.substring(1, commaPos);
            String right = line.substring(commaPos + 1, closingBracketPos);

            n.left = createTree(left);
            n.left.parent = n;

            n.right = createTree(right);
            n.right.parent = n;
        } else {
            n.value = Integer.parseInt(line);
        }

        return n;
    }

    private static int findClosingBracket(String line) {

        boolean founded = false;
        int depth = 0;
        int i = 0;
        do {
            if (line.charAt(i) == '[') {
                depth++;
            } else if (line.charAt(i) == ']') {
                depth--;
                if (depth == 0) {
                    founded = true;
                    break;
                }
            }
            i++;
        } while (!founded);

        return i;
    }

    private static int findSameDepthComma(String line) {

        boolean founded = false;
        int depth = 0;
        int i = 1;
        do {
            if (line.charAt(i) == '[') {
                depth++;
            } else if (line.charAt(i) == ']') {
                depth--;
            } else if (line.charAt(i) == ',') {
                if (depth == 0) {
                    founded = true;
                    break;
                }
            }
            i++;
        } while (!founded);

        return i;
    }

    public static Node findLeftMostCandidateForExplode(Node node, int depth) {
        if (node != null) {
            if (depth >= 4 && node.isFinalPair()) {
                return node;
            } else {
                Node l = findLeftMostCandidateForExplode(node.left, depth + 1);
                if (l != null) {
                    return l;
                }
                Node r = findLeftMostCandidateForExplode(node.right, depth + 1);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    public static Node findLeftMostGeThan10RegularNumber(Node node) {
        if (node != null) {
            if (node.isLeafNode() && node.value >= 10) {
                return node;
            } else {
                Node l = findLeftMostGeThan10RegularNumber(node.left);
                if (l != null) {
                    return l;
                }
                Node r = findLeftMostGeThan10RegularNumber(node.right);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    public static void orderTree(Node node, List<Node> result) {
        if (node != null) {
            if (node.isLeafNode()) {
                result.add(node);
            } else {
                orderTree(node.left, result);
                orderTree(node.right, result);
            }
        }
    }

    public static Long getTreeMagnitude(Node node) {
        if (node.isLeafNode()) {
            return node.value.longValue();
        } else {
            return getTreeMagnitude(node.left) * 3 + getTreeMagnitude(node.right) * 2;
        }
    }
}
