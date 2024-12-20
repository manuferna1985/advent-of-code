package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.RangeUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Day20 extends Day {

  private static final String START = "S";
  private static final String END = "E";
  private static final String EMPTY = ".";

  @Override
  protected String part1(String fileContents) throws Exception {
    return algorithm(fileContents, 2);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return algorithm(fileContents, 20);
  }

  private String algorithm(String fileContents, int maxCheatDistance) {
    String[][] maze = MatrixUtils.readMatrixFromFile(fileContents);
    List<Point> path = findPath(maze);

    return String.valueOf(
        calculateCheats(path, maxCheatDistance).entrySet().stream()
            .filter(entry -> entry.getKey() >= 100)
            .map(entry -> entry.getValue().size())
            .mapToInt(Integer::intValue)
            .sum());
  }

  private Map<Integer, List<Point>> calculateCheats(List<Point> path, int maxCheatDistance) {
    Map<Integer, List<Point>> savedPositions = new HashMap<>();
    for (int i = 0; i < path.size(); i++) {
      Point pi = path.get(i);
      for (int j = i + 1; j < path.size(); j++) {
        Point pj = path.get(j);
        int manhattanDistance = RangeUtils.getManhattanDistance(pi, pj);
        if (manhattanDistance <= maxCheatDistance) {
          int savedPath = j - i - manhattanDistance;
          if (savedPath > 0) {
            savedPositions.computeIfAbsent(savedPath, key -> new ArrayList<>()).add(pj);
          }
        }
      }
    }
    return savedPositions;
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

  private List<Point> findPath(String[][] maze) {
    Point start = find(maze, START);
    Point end = find(maze, END);

    final AtomicReference<Point> current = new AtomicReference<>(start);
    final AtomicReference<Point> previous = new AtomicReference<>(null);
    List<Point> path = new ArrayList<>();
    path.add(current.get());
    boolean exit = false;
    while (!exit) {
      Point next = MatrixUtils.getNeighbours(maze, current.get().x, current.get().y, false)
          .stream()
          .filter(p -> p.getValue().equals(EMPTY))
          .filter(p -> previous.get()==null || !p.getKey().equals(previous.get()))
          .findFirst()
          .map(Pair::getKey)
          .orElse(current.get());

      path.add(next);
      if (next.equals(end)) {
        exit = true;
      }
      previous.set(current.get());
      current.set(next);
    }
    return path;
  }

  public static void main(String[] args) {
    Day.run(Day20::new, "2024/D20_small.txt", "2024/D20_full.txt");
  }
}
