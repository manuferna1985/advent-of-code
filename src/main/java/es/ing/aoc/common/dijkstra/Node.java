package es.ing.aoc.common.dijkstra;

import java.util.Comparator;

// Node class
public class Node implements Comparator<Node> {
    public int id;
    public int cost;

    public Node() {
    } //empty constructor

    public Node(int id, int cost) {
        this.id = id;
        this.cost = cost;
    }

    @Override
    public int compare(Node node1, Node node2) {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
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
