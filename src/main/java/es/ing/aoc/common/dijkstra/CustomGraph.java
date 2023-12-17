package es.ing.aoc.common.dijkstra;

import es.ing.aoc.common.Point;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static es.ing.aoc.common.dijkstra.CustomNode.Direction.S;

public class CustomGraph {
  private final int[][][][] distances;
  private final PriorityQueue<CustomNode> priorityQueue;
  private final Map<CustomNode, List<CustomNode>> edges;

  //class constructor
  public CustomGraph(Map<CustomNode, List<CustomNode>> edges) {
    int maxX = edges.keySet().stream().map(node -> node.getPoint().x).mapToInt(Integer::valueOf).max().orElse(0);
    int maxY = edges.keySet().stream().map(node -> node.getPoint().y).mapToInt(Integer::valueOf).max().orElse(0);
    int maxSteps = edges.keySet().stream().map(CustomNode::getDirStraightMoves).mapToInt(Integer::valueOf).max().orElse(0);
    this.distances = new int[maxX + 1][maxY + 1][CustomNode.Direction.values().length][maxSteps];
    this.priorityQueue = new PriorityQueue<>(edges.size(), new CustomNode(Point.of(0, 0), S, 0, 0));
    this.edges = edges;
  }

  public int[][][][] getDistances() {
    return distances;
  }

  public CustomGraph algorithm(CustomNode source) {
    return algorithm(List.of(source));
  }

  // Dijkstra's Algorithm implementation
  public CustomGraph algorithm(List<CustomNode> sources) {

    for (CustomNode edge : edges.keySet()){
      if (sources.contains(edge)) {
        setCurrentDistance(edge, 0);
      } else {
        setCurrentDistance(edge, Integer.MAX_VALUE);
      }
      priorityQueue.add(edge.cloneWithDistance(getCurrentDistance(edge)));
    }

    while (!priorityQueue.isEmpty()) {
      // u is removed from PriorityQueue and has min distance
      System.out.println(priorityQueue.size());
      CustomNode u = priorityQueue.remove();
      processNeighbours(u);
    }

    return this;
  }

  // This methods processes all neighbours of the just visited node
  private void processNeighbours(CustomNode origin) {

    List<CustomNode> neighbours = edges.get(origin);
    // process all neighbouring nodes of origin

    neighbours.parallelStream().forEach(destination -> {
      int alt = getCurrentDistance(origin) + destination.getCost();

      if (alt < 0) {
        alt = Integer.MAX_VALUE;
      }

      // compare distances
      if (alt < getCurrentDistance(destination)) {
        setCurrentDistance(destination, alt);

        // Add the current vertex to the PriorityQueue
        CustomNode newNode = destination.cloneWithDistance(alt);

        synchronized (this) {
          priorityQueue.remove(newNode);
          priorityQueue.add(newNode);
        }
      }
    });
  }

  private int getCurrentDistance(CustomNode c){
    return distances[c.getPoint().x][c.getPoint().y][c.getDir().getValue()][c.getDirStraightMoves()-1];
  }

  private void setCurrentDistance(CustomNode c, int newDistance){
    distances[c.getPoint().x][c.getPoint().y][c.getDir().getValue()][c.getDirStraightMoves()-1] = newDistance;
  }
}