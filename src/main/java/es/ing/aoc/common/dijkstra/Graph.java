package es.ing.aoc.common.dijkstra;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {
    private final int[] distances;
    private final PriorityQueue<Node> priorityQueue;
    private final Map<Integer, List<Node>> edges;

    //class constructor
    public Graph(Map<Integer, List<Node>> edges) {
        this.distances = new int[edges.size()];
        this.priorityQueue = new PriorityQueue<>(edges.size(), new Node(0, 0));
        this.edges = edges;
    }

    public int[] getDistances() {
        return distances;
    }

    public Graph algorithm(int source) {
        return algorithm(List.of(source));
    }

    // Dijkstra's Algorithm implementation
    public Graph algorithm(List<Integer> sources) {

        for (int i = 0; i < distances.length; i++) {
            if (sources.contains(i)) {
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

        return this;
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