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
}
