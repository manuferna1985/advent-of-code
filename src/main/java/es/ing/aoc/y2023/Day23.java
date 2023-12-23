package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.dijkstra.Node;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Day23 extends Day {

  enum Direction {
    UP, DOWN, LEFT, RIGHT;
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);
    Point start = Point.of(0, 1);
    Point end = Point.of(matrix.length - 1, matrix[0].length - 2);
    AtomicLong maxDistance = new AtomicLong(0L);
    findLongestPathRecursive(start, end, matrix, 0L, maxDistance, new HashSet<>(), null, true);
    return String.valueOf(maxDistance.get());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);

    AtomicLong maxDistance = new AtomicLong(0L);
    Map<Integer, List<Node>> nodes = pruneLongPaths(createGraph(matrix));

    Integer start = getIdFrom(0, 1, matrix);
    Integer end = getIdFrom(matrix.length - 1, matrix[0].length - 2, matrix);
    findLongestPathIterative(start, end, nodes, maxDistance);

    return String.valueOf(maxDistance.get());
  }

  private void findLongestPathRecursive(Point start, Point end, String[][] matrix, long currentLength, AtomicLong max, Set<Point> alreadyVisited, Direction forcedDir, boolean slipperySlopes) {

    if (end.equals(start)) {
      max.set(Math.max(max.get(), currentLength));
    } else {
      List<Pair<Point, String>> neighbours = MatrixUtils.getNeighbours(matrix, start.x, start.y, false);

      for (Pair<Point, String> n : neighbours) {
        if (!alreadyVisited.contains(n.getKey()) && !n.getValue().equals("#")) {
          if (forcedDir==null || (!slipperySlopes || correctDir(start, n.getKey(), forcedDir))) {

            Direction dir =
                switch (n.getValue()) {
                  case ">" -> Direction.RIGHT;
                  case "<" -> Direction.LEFT;
                  case "^" -> Direction.UP;
                  case "v" -> Direction.DOWN;
                  default -> null;
                };

            alreadyVisited.add(n.getKey());
            findLongestPathRecursive(n.getKey(), end, matrix, currentLength + 1, max, alreadyVisited, dir, slipperySlopes);
            alreadyVisited.remove(n.getKey());
          }
        }
      }
    }
  }

  private void findLongestPathIterative(Integer start, Integer end, Map<Integer, List<Node>> edges, AtomicLong max) {

    List<Triple<Integer, Long, Set<Integer>>> pendingPoints = new ArrayList<>();
    pendingPoints.add(Triple.of(start, 0L, new HashSet<>()));

    Map<Triple<Integer, Integer, Integer>, Long> maxRelatives = new HashMap<>();

    while (!pendingPoints.isEmpty()) {

      Triple<Integer, Long, Set<Integer>> currentData = pendingPoints.remove(0);
      Integer currentPoint = currentData.getLeft();

      if (end.equals(currentPoint)) {
        if (currentData.getMiddle() > max.get()) {
          max.set(currentData.getMiddle());
          System.out.printf("%d - %d\n", pendingPoints.size(), max.get());
        }
      } else {
        List<Node> neighbours = edges.get(currentPoint);

        for (Node n : neighbours) {
          if (!currentData.getRight().contains(n.getId())) {

            Triple<Integer, Integer, Integer> relativeKey = Triple.of(start, currentPoint, n.getId());
            if (!maxRelatives.containsKey(relativeKey) || maxRelatives.get(relativeKey) <= (currentData.getMiddle() + n.getCost() + 250)) {
              maxRelatives.put(relativeKey, currentData.getMiddle() + n.getCost());
              Set<Integer> newAlreadyVisited = new HashSet<>(currentData.getRight());
              newAlreadyVisited.add(n.getId());

              pendingPoints.add(Triple.of(n.getId(), currentData.getMiddle() + n.getCost(), newAlreadyVisited));
            }
          }
        }
      }
    }
  }


  private boolean correctDir(Point start, Point end, Direction forcedDir) {
    return switch (forcedDir) {
      case UP -> end.x==start.x - 1 && end.y==start.y;
      case DOWN -> end.x==start.x + 1 && end.y==start.y;
      case LEFT -> end.x==start.x && end.y==start.y - 1;
      case RIGHT -> end.x==start.x && end.y==start.y + 1;
    };
  }

  private Map<Integer, List<Node>> createGraph(String[][] matrix) {

    Map<Integer, List<Node>> edges = new HashMap<>();

    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {

        if (!matrix[x][y].equals("#")) {
          List<Pair<Point, String>> neighbours = MatrixUtils.getNeighbours(matrix, x, y, false);
          List<Node> nns = new ArrayList<>();

          for (Pair<Point, String> n : neighbours) {
            if (!n.getValue().equals("#")) {
              nns.add(new Node(getIdFrom(n.getKey().x, n.getKey().y, matrix), 1));
            }
          }

          edges.put(getIdFrom(x, y, matrix), nns);
        }
      }
    }


    return edges;
  }

  private Map<Integer, List<Node>> pruneLongPaths(Map<Integer, List<Node>> edges) {

    Map<Integer, List<Node>> newEdges = new HashMap<>();

    for (Map.Entry<Integer, List<Node>> entry : edges.entrySet()) {
      Node nB = new Node(entry.getKey());

      if (entry.getValue().size()==2) {
        Node nA = entry.getValue().get(0);
        Node nC = entry.getValue().get(1);
        int newCost = nA.getCost() + nC.getCost();

        edges.get(nA.getId()).add(new Node(nC.getId(), newCost));
        edges.get(nA.getId()).remove(nB);

        edges.get(nC.getId()).add(new Node(nA.getId(), newCost));
        edges.get(nC.getId()).remove(nB);

      } else {
        newEdges.put(entry.getKey(), entry.getValue());
      }
    }

    return newEdges;
  }

  private Integer getIdFrom(int x, int y, String[][] matrix) {
    return x * matrix.length + y;
  }

  public static void main(String[] args) {
    Day.run(Day23::new, "2023/D23_small.txt", "2023/D23_full.txt");
  }
}
