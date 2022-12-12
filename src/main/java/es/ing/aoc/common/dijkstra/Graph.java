package es.ing.aoc.common.dijkstra;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {
    private int[] distances;
    private PriorityQueue<Node> priorityQueue;
    private Map<Integer, List<Node>> edges;

    //class constructor
    public Graph(int numberOfNodes) {
        distances = new int[numberOfNodes];
        priorityQueue = new PriorityQueue<>(numberOfNodes, new Node(0, 0));
    }

    public int[] getDistances() {
        return distances;
    }

    // Dijkstra's Algorithm implementation
    public void algorithm(Map<Integer, List<Node>> edges, int source) {
        this.edges = edges;

        for (int i = 0; i < distances.length; i++) {
            if (i == source) {
                distances[i] = 0;
            } else {
                distances[i] = Integer.MAX_VALUE;
            }
            priorityQueue.add(new Node(i, distances[i]));
        }

        while (!priorityQueue.isEmpty()) {
            // u is removed from PriorityQueue and has min distance
            int u = priorityQueue.remove().getId();
            processNeighbours(u);
        }
    }

    // This methods processes all neighbours of the just visited node
    private void processNeighbours(int origin) {
        int alt = -1;

        List<Node> neighbours = edges.get(origin);
        // process all neighbouring nodes of origin
        for (Node destination : neighbours) {
            alt = distances[origin] + destination.getCost();

            if (alt < 0) {
                alt = Integer.MAX_VALUE;
            }

            // compare distances
            if (alt < distances[destination.getId()]) {
                distances[destination.getId()] = alt;

                // Add the current vertex to the PriorityQueue
                Node newNode = new Node(destination.getId(), distances[destination.getId()]);
                priorityQueue.remove(newNode);
                priorityQueue.add(newNode);
            }
        }
    }
}