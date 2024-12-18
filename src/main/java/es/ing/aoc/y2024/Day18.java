package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.dijkstra.GenericGraph;
import es.ing.aoc.common.dijkstra.GenericNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day18 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    // Simple Dijkstra
    Map<Point, Integer> flakes = buildFlakes(fileContents);
    int mapSize = getMapSize(flakes);
    int simulationLimit = getSimLimit(flakes);

    return String.valueOf(
        getMinimumPathToEnd(
            Point.of(0, 0),
            Point.of(mapSize, mapSize),
            flakes,
            simulationLimit,
            mapSize));
  }

  private int getSimLimit(Map<Point, Integer> flakes) {
    return flakes.size() < 100 ? 12:1024;
  }

  private int getMapSize(Map<Point, Integer> flakes) {
    return flakes.size() < 100 ? 6:70;
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    Map<Point, Integer> flakes = buildFlakes(fileContents);
    int mapSize = getMapSize(flakes);
    int simulationLimit = getSimLimit(flakes);

    Point start = Point.of(0, 0);
    Point end = Point.of(mapSize, mapSize);

    int min = simulationLimit;
    int max = flakes.size();

    // Dijkstra with Binary Search from simulation to max flakes number
    while (true) {
      int limit = (min + max) / 2;
      if (getMinimumPathToEnd(start, end, flakes, limit, mapSize) < Integer.MAX_VALUE) {
        // Add more flakes
        min = limit;
      } else {
        // Dead-end, remove flakes
        max = limit;
      }

      if (min + 1==max) {
        Point firstBlockingFlake = inverseMap(flakes).get(min);
        return "%d,%d".formatted(firstBlockingFlake.y, firstBlockingFlake.x);
      }
    }
  }

  private Map<Point, Integer> buildFlakes(String fileContents) {
    String[] lines = fileContents.split(System.lineSeparator());
    Map<Point, Integer> flakes = new HashMap<>();
    int index = 0;
    for (String line : lines) {
      List<Integer> flake = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
      flakes.put(Point.of(flake.get(1), flake.get(0)), index++);
    }
    return flakes;
  }

  private Map<Point, List<GenericNode<Point>>> buildEdges(Map<Point, Integer> flakes, int limit, int mapSize) {
    Map<Point, List<GenericNode<Point>>> edges = new HashMap<>();
    Integer[][] matrix = new Integer[mapSize + 1][mapSize + 1];
    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {
        Point a = Point.of(x, y);
        if (isEmptyPath(flakes, a, limit)) {
          List<Pair<Point, Integer>> neighbours = MatrixUtils.getNeighbours(matrix, x, y, false);
          for (Pair<Point, Integer> n : neighbours) {
            if (isEmptyPath(flakes, n.getKey(), limit)) {
              addEdge(edges, a, n.getKey());
            }
          }
        }
      }
    }
    return edges;
  }

  private boolean isEmptyPath(Map<Point, Integer> flakes, Point flake, int maxIndex) {
    if (!flakes.containsKey(flake)) {
      return true;
    } else {
      return flakes.get(flake) >= maxIndex;
    }
  }

  private void addEdge(Map<Point, List<GenericNode<Point>>> edges, Point a, Point b) {
    edges.computeIfAbsent(a, k -> new ArrayList<>());
    edges.get(a).add(new GenericNode<>(b, 1));
  }

  private Integer getMinimumPathToEnd(Point start, Point end, Map<Point, Integer> flakes, int limitSimulation, int mapSize) {
    Map<Point, List<GenericNode<Point>>> edges = buildEdges(flakes, limitSimulation, mapSize);
    GenericGraph<Point> graph = new GenericGraph<>(edges);
    graph.algorithm(start);
    return graph.getDistances().get(end);
  }

  public <K, V> Map<V, K> inverseMap(Map<K, V> sourceMap) {
    //if sourceMap has duplicate values, keep only first
    return sourceMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (a, b) -> a));
  }

  public static void main(String[] args) {
    Day.run(Day18::new, "2024/D18_small.txt", "2024/D18_full.txt");
  }
}
