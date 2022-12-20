package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.ArrayList;
import java.util.List;

public class Day20 extends Day {

    static class Node {
        long value;
        Node nextNode;
        Node previousNode;

        public Node(long value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(value);
        }
    }

    static class CircularDoubleLinkedList {
        private Node head = null;
        private Node tail = null;
        private int numberOfNodes;

        CircularDoubleLinkedList() {
            numberOfNodes = 0;
        }

        public void addNode(long value) {
            Node newNode = new Node(value);

            if (head == null) {
                head = newNode;
            } else {
                tail.nextNode = newNode;
                newNode.previousNode = tail;
            }

            tail = newNode;
            tail.nextNode = head;
            head.previousNode = tail;

            numberOfNodes++;
        }

        public void moveNodeInRing(Node nodeToMove) {

            long positionsToMove = nodeToMove.value;

            if (positionsToMove > 0) {
                // Right movement
                for (int i = 0; i < getReducedPositionsToMove(positionsToMove); i++) {
                    Node before = nodeToMove.previousNode;
                    Node after = nodeToMove.nextNode;

                    before.nextNode = after;
                    after.previousNode = before;

                    nodeToMove.previousNode = after;
                    nodeToMove.nextNode = after.nextNode;
                    after.nextNode.previousNode = nodeToMove;
                    after.nextNode = nodeToMove;
                }
            } else {
                // Left movement
                for (int i = 0; i < getReducedPositionsToMove(positionsToMove); i++) {
                    Node before = nodeToMove.previousNode;
                    Node after = nodeToMove.nextNode;

                    before.nextNode = after;
                    after.previousNode = before;

                    nodeToMove.previousNode = before.previousNode;
                    nodeToMove.nextNode = before;

                    before.previousNode.nextNode = nodeToMove;
                    before.previousNode = nodeToMove;
                }
            }
        }

        private int getReducedPositionsToMove(long positionsToMove) {
            return (int) (Math.abs(positionsToMove) % (numberOfNodes - 1));
        }

        public List<Node> loopCurrentRing() {
            List<Node> sequence = new ArrayList<>();
            Node currentNode = head;

            if (head != null) {
                do {
                    sequence.add(currentNode);
                    currentNode = currentNode.nextNode;
                } while (currentNode != head);
            }
            return sequence;
        }

        public Node find(int value) {
            Node currentNode = head;
            if (head != null) {
                while (true) {
                    if (currentNode.value == value) {
                        return currentNode;
                    }
                    currentNode = currentNode.nextNode;
                }
            }
            return null;
        }
    }

    private long ringLoopNumbers(String fileContents, long multiplyFactor, int mixTimes) {
        String[] numbers = fileContents.split(System.lineSeparator()); // when input file is multiline
        CircularDoubleLinkedList ring = new CircularDoubleLinkedList();

        for (String n : numbers) {
            ring.addNode(Long.parseLong(n) * multiplyFactor);
        }

        List<Node> sequence = ring.loopCurrentRing();
        for (int i = 0; i < mixTimes; i++) {
            for (Node n : sequence) {
                ring.moveNodeInRing(n);
            }
        }

        Node current = ring.find(0);
        long total = 0;

        for (int i = 1; i <= 3000; i++) {
            current = current.nextNode;
            if (i % 1000 == 0) {
                total += current.value;
            }
        }

        return total;
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        return String.valueOf(ringLoopNumbers(fileContents, 1L, 1));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return String.valueOf(ringLoopNumbers(fileContents, 811589153L, 10));
    }

    public static void main(String[] args) {
        Day.run(Day20::new, "2022/D20_small.txt", "2022/D20_full.txt");
    }
}
