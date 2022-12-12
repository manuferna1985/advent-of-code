package es.ing.aoc.common.dijkstra;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {
    private int[] dist;
    private PriorityQueue<Node> pqueue;
    private Map<Integer, List<Node>> edges;

    //class constructor
    public Graph(int numberOfNodes) {
        dist = new int[numberOfNodes];
        pqueue = new PriorityQueue<>(numberOfNodes, new Node());
    }

    public int[] getDistances() {
        return dist;
    }

    // Dijkstra's Algorithm implementation
    public void algorithm(Map<Integer, List<Node>> edges, int source) {
        this.edges = edges;

        for (int i = 0; i < dist.length; i++) {
            if (i == source) {
                dist[i] = 0;
            } else {
                dist[i] = Integer.MAX_VALUE;
            }
            pqueue.add(new Node(i, dist[i]));
        }

        while (!pqueue.isEmpty()) {
            // u is removed from PriorityQueue and has min distance
            int u = pqueue.remove().id;
            processNeighbours(u);
        }
    }

    // This methods processes all neighbours of the just visited node
    private void processNeighbours(int origin) {
        int alt = -1;

        List<Node> neighbours = edges.get(origin);
        // process all neighbouring nodes of origin
        for (Node destination : neighbours) {
            alt = dist[origin] + destination.cost;

            if (alt < 0) {
                alt = Integer.MAX_VALUE;
            }

            // compare distances
            if (alt < dist[destination.id]) {
                dist[destination.id] = alt;

                // Add the current vertex to the PriorityQueue
                Node newNode = new Node(destination.id, dist[destination.id]);
                pqueue.remove(newNode);
                pqueue.add(newNode);
            }
        }
    }
}