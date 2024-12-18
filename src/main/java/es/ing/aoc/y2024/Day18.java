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

public class Day18 extends Day {

  private static final Integer MAP_SIZE = 70;
  private static final Integer SIMULATION = 1024;

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    List<Point> flakes = new ArrayList<>();

    for (String line : lines){
      List<Integer> flake = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
      flakes.add(Point.of(flake.get(1), flake.get(0)));
      if (flakes.size() == SIMULATION){
        break;
      }
    }

    Map<Point, List<GenericNode<Point>>> edges = new HashMap<>();

    Point start = Point.of(0,0);
    Point end = Point.of(MAP_SIZE,MAP_SIZE);

    Integer[][] matrix = new Integer[MAP_SIZE+1][MAP_SIZE+1];
    for (int x=0; x<matrix.length; x++){
      for (int y=0; y<matrix[x].length; y++){
        Point a = Point.of(x, y);
        if (!flakes.contains(a)) {
          List<Pair<Point, Integer>> neighbours = MatrixUtils.getNeighbours(matrix, x, y, false);
          for (Pair<Point, Integer> n : neighbours) {
            if (!flakes.contains(n.getKey())) {
              addEdge(edges, a, n.getKey());
            }
          }
        }
      }
    }

    GenericGraph<Point> graph = new GenericGraph<>(edges);
    graph.algorithm(start);

    Integer minLength = graph.getDistances().get(end);

    return String.valueOf(minLength);
  }

  private void addEdge(Map<Point, List<GenericNode<Point>>> edges, Point a, Point b){
    edges.computeIfAbsent(a, k -> new ArrayList<>());
    edges.get(a).add(new GenericNode<>(b, 1));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    //Day.run(Day18::new, "2024/D18_small.txt", "2024/D18_full.txt");
    Day.run(Day18::new, "2024/D18_full.txt");

    // 152 too low
  }
}
