package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.dijkstra.Graph;
import es.ing.aoc.common.dijkstra.Node;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Day21 extends Day {

  private static final Integer MAX_STEPS = 64;

  @Override
  protected String part1(String fileContents) throws Exception {

    String[][] map = MatrixUtils.readMatrixFromFile(fileContents);
    Point start = find(map, "S"::equals).get(0);

    int[] distances = getDistances(start, map);

    long count = 0L;
    boolean evenLength = even(MAX_STEPS);
    for (int dist : distances) {
      if (dist <= MAX_STEPS) {
        if (evenLength && even(dist)) {
          count++;
        } else if (!evenLength && !even(dist)) {
          count++;
        }
      }
    }

    return String.valueOf(count);
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[][] map = MatrixUtils.readMatrixFromFile(fileContents);
    Point start = find(map, "S"::equals).get(0);

    int[] distances = getDistances(start, map);

    long count = 0L;
    for (int max = 1; max <= MAX_STEPS; max++) {

      count = 0L;
      boolean evenLength = even(max);
      for (int dist : distances) {
        if (dist <= max) {
          if (evenLength && even(dist)) {
            count++;
          } else if (!evenLength && !even(dist)) {
            count++;
          }
        }
      }

      System.out.printf("[%-2d] - %d\n", max, count);
    }

    return String.valueOf(count);
  }

  private boolean even(int number) {
    return number % 2==0;
  }

  private int[] getDistances(Point start, String[][] map) {

    Map<Integer, List<Node>> edges = new HashMap<>();

    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        Integer id = getId(x, y, map);
        List<Pair<Point, String>> neighbours = MatrixUtils.getNeighbours(map, x, y, false);

        List<Node> garden = new ArrayList<>();
        for (Pair<Point, String> n : neighbours) {
          if (!n.getValue().equals("#")) {
            garden.add(new Node(getId(n.getKey(), map), 1));
          }
        }

        edges.put(id, garden);
      }
    }

    Graph graph = new Graph(edges);
    graph.algorithm(getId(start, map));

    return graph.getDistances();
  }

  private int getId(Point p, String[][] map) {
    return getId(p.x, p.y, map);
  }

  private int getId(int x, int y, String[][] map) {
    return x * map.length + y;
  }

  private List<Point> find(String[][] map, Predicate<String> predicate) {
    List<Point> results = new ArrayList<>();
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        if (predicate.test(map[x][y])) {
          results.add(Point.of(x, y));
        }
      }
    }
    return results;
  }

  public static void main(String[] args) {
    Day.run(Day21::new, "2023/D21_full.txt");//, "2023/D21_full.txt");
  }
}
