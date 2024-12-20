package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
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
  private static final String WALL = "#";

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] maze = MatrixUtils.readMatrixFromFile(fileContents);
    List<Point> path = findPath(maze);

    System.out.println(path.size());

    Map<Integer, List<Point>> savedPositions = new HashMap<>();

    for (int i = 0; i < path.size(); i++) {
      Point p = path.get(i);
      // Let's count how many cheats (and how much will they save) from the p position

      if (p.x > 2) {
        // NORTH
        saveCheat(maze, p, Point.of(p.x - 2, p.y), Point.of(p.x - 1, p.y), path, i, savedPositions);
      }

      if (p.x < maze.length - 3) {
        // SOUTH
        saveCheat(maze, p, Point.of(p.x + 2, p.y), Point.of(p.x + 1, p.y), path, i, savedPositions);
      }

      if (p.y > 2) {
        // WEST
        saveCheat(maze, p, Point.of(p.x, p.y - 2), Point.of(p.x, p.y - 1), path, i, savedPositions);
      }

      if (p.y < maze[0].length - 3) {
        // EAST
        saveCheat(maze, p, Point.of(p.x, p.y + 2), Point.of(p.x, p.y + 1), path, i, savedPositions);
      }
    }

    return String.valueOf(
        savedPositions.entrySet().stream()
            .filter(entry -> entry.getKey() >= 100)
            .map(entry -> entry.getValue().size())
            .mapToInt(Integer::intValue)
            .sum());
  }

  private static void saveCheat(String[][] maze, Point current, Point cheat, Point jump, List<Point> path, int i, Map<Integer, List<Point>> savedPositions) {
    if (maze[cheat.x][cheat.y].equals(EMPTY) && maze[jump.x][jump.y].equals(WALL)) {
      int j = path.indexOf(cheat);
      if (j > i && j!=-1) {
        int savedPath = j - i - 2;
        //System.out.printf("Cheat from %s to %s will save %d positions%n", current, cheat, savedPath);
        savedPositions.computeIfAbsent(savedPath, key -> new ArrayList<>()).add(cheat);
      }
    }
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
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
