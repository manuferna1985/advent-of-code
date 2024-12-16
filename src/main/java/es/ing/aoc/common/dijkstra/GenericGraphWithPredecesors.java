package es.ing.aoc.common.dijkstra;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class GenericGraphWithPredecesors<T> {
  private final Map<T, Pair<Integer, Set<T>>> distances;
  private final PriorityQueue<GenericNode<T>> priorityQueue;
  private final Map<T, List<GenericNode<T>>> edges;

  //class constructor
  public GenericGraphWithPredecesors(Map<T, List<GenericNode<T>>> edges) {
    this.distances = new HashMap<>();
    this.priorityQueue = new PriorityQueue<>(edges.size(), new GenericNode<>(null, 0));
    this.edges = edges;
  }

  public Map<T, Pair<Integer, Set<T>>> getDistances() {
    return distances;
  }

  public GenericGraphWithPredecesors<T> algorithm(T source) {
    return algorithm(List.of(source));
  }

  // Dijkstra's Algorithm implementation
  public GenericGraphWithPredecesors<T> algorithm(List<T> sources) {

    for (T e : edges.keySet()) {
      if (sources.contains(e)) {
        distances.put(e, Pair.of(0, new HashSet<>()));
      } else {
        distances.put(e, Pair.of(Integer.MAX_VALUE, new HashSet<>()));
      }
      priorityQueue.add(new GenericNode<>(e, distances.get(e).getKey()));
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
      Pair<Integer, Set<T>> currentDist = distances.get(origin);
      alt = currentDist.getKey() + destination.cost();

      if (alt < 0) {
        alt = Integer.MAX_VALUE;
      }

      // compare distances
      if (alt <= distances.get(destination.id()).getKey()) {
        boolean strictlyLower = alt < distances.get(destination.id()).getKey();
        Set<T> newPath = new HashSet<>(currentDist.getValue());
        newPath.add(origin);
        if (alt == distances.get(destination.id()).getKey()) {
          newPath.addAll(distances.get(destination.id()).getValue());
        }
        distances.put(destination.id(), Pair.of(alt, newPath));

        if (strictlyLower) {
          // Add the current vertex to the PriorityQueue
          GenericNode<T> newNode = new GenericNode<>(destination.id(), distances.get(destination.id()).getKey());

          priorityQueue.remove(newNode);
          priorityQueue.add(newNode);
        }
      }
    }
  }
}