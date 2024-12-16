package es.ing.aoc.common.dijkstra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class GenericGraph<T> {
  private final Map<T, Integer> distances;
  private final PriorityQueue<GenericNode<T>> priorityQueue;
  private final Map<T, List<GenericNode<T>>> edges;

  //class constructor
  public GenericGraph(Map<T, List<GenericNode<T>>> edges) {
    this.distances = new HashMap<>();
    this.priorityQueue = new PriorityQueue<>(edges.size(), new GenericNode<>(null, 0));
    this.edges = edges;
  }

  public Map<T, Integer> getDistances() {
    return distances;
  }

  public GenericGraph<T> algorithm(T source) {
    return algorithm(List.of(source));
  }

  // Dijkstra's Algorithm implementation
  public GenericGraph<T> algorithm(List<T> sources) {

    for (T e : edges.keySet()) {
      if (sources.contains(e)) {
        distances.put(e, 0);
      } else {
        distances.put(e, Integer.MAX_VALUE);
      }
      priorityQueue.add(new GenericNode<>(e, distances.get(e)));
    }

    while (!priorityQueue.isEmpty()) {
      // u is removed from PriorityQueue and has min distance
      T u = priorityQueue.remove().id();
      processNeighbours(u);
    }

    return this;
  }

  // This methods processes all neighbours of the just visited node
  private void processNeighbours(T origin) {
    int alt = -1;

    List<GenericNode<T>> neighbours = edges.get(origin);
    // process all neighbouring nodes of origin
    for (GenericNode<T> destination : neighbours) {
      alt = distances.get(origin) + destination.cost();

      if (alt < 0) {
        alt = Integer.MAX_VALUE;
      }

      // compare distances
      if (alt < distances.get(destination.id())) {
        distances.put(destination.id(), alt);

        // Add the current vertex to the PriorityQueue
        GenericNode<T> newNode = new GenericNode<>(destination.id(), distances.get(destination.id()));

        priorityQueue.remove(newNode);
        priorityQueue.add(newNode);
      }
    }
  }
}