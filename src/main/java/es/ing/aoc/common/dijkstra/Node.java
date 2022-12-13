package es.ing.aoc.common.dijkstra;

import java.util.Comparator;

// Node class
public class Node implements Comparator<Node> {
    private final int id;
    private final int cost;

    public Node(int id, int cost) {
        this.id = id;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public int compare(Node node1, Node node2) {
        return Integer.compare(node1.cost, node2.cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node1 = (Node) o;

        return id == node1.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
