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

public class Day16 extends Day {

  private static final String START = "S";
  private static final String END = "E";
  private static final String EMPTY = ".";

  public enum Direction {
    N,
    S,
    E,
    W;

    public Point move(Point p) {
      return switch (this) {
        case N -> Point.of(p.x - 1, p.y);
        case S -> Point.of(p.x + 1, p.y);
        case W -> Point.of(p.x, p.y - 1);
        case E -> Point.of(p.x, p.y + 1);
      };
    }

    Direction turnClockwise() {
      return switch (this) {
        case N -> E;
        case S -> W;
        case W -> N;
        case E -> S;
      };
    }

    Direction turnAntiClockwise() {
      return switch (this) {
        case N -> W;
        case S -> E;
        case W -> S;
        case E -> N;
      };
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] maze = MatrixUtils.readMatrixFromFile(fileContents);

    Pair<Point, Direction> start = Pair.of(find(maze, START), Direction.E);
    Point exit = find(maze, END);

    Map<Pair<Point, Direction>, List<GenericNode<Pair<Point, Direction>>>> edges = new HashMap<>();

    for (int x=0; x < maze.length; x++){
      for (int y=0; y < maze[x].length; y++){
        Point p1 = Point.of(x, y);
        for (Direction d : Direction.values()){
          var key = Pair.of(p1, d);
          if (!edges.containsKey(key)) {
            edges.put(key, new ArrayList<>());
          }
          Point p2 = d.move(p1);
          if (EMPTY.equals(maze[p1.x][p1.y]) && EMPTY.equals(maze[p2.x][p2.y])){
            addNewEdge(edges, Pair.of(p1, d), Pair.of(p2, d), 1);
            addNewEdge(edges, Pair.of(p1, d.turnClockwise()), Pair.of(p2, d), 1001);
            addNewEdge(edges, Pair.of(p1, d.turnAntiClockwise()), Pair.of(p2, d), 1001);
          }
        }
      }
    }
    GenericGraph<Pair<Point, Direction>> graph = new GenericGraph<>(edges);
    graph.algorithm(start);

    return String.valueOf(Arrays.stream(Direction.values())
        .map(d -> Pair.of(exit, d))
        .map(p -> graph.getDistances().get(p))
        .mapToInt(Integer::intValue)
        .min()
        .orElse(Integer.MAX_VALUE));
  }

  private <T> void addNewEdge(Map<T, List<GenericNode<T>>> edges, T a, T b, int cost){
    if (edges.containsKey(a)){
      edges.get(a).add(new GenericNode<>(b, cost));
    } else {
      List<GenericNode<T>> nodes = new ArrayList<>();
      nodes.add(new GenericNode<>(b, cost));
      edges.put(a, nodes);
    }
  }

  private Point find(String[][] maze, String character) {
    for (int x = 0; x < maze.length; x++) {
      for (int y = 0; y < maze[x].length; y++) {
        if (character.equals(maze[x][y])) {
          maze[x][y] = EMPTY;
          return Point.of(x, y);
        }
      }
    }
    return null;
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day16::new, "2024/D16_small.txt");//, "2024/D16_full.txt");
  }
}
