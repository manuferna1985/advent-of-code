package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
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
      return turnClockwise().turnClockwise().turnClockwise();
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] maze = MatrixUtils.readMatrixFromFile(fileContents);

    Pair<Point, Direction> p1 = Pair.of(find(maze, START), Direction.E);
    Point exit = find(maze, END);

    return String.valueOf(numberOfStepsForExitMaze(maze, p1, exit));
  }

  private int numberOfStepsForExitMaze(String[][] maze, Pair<Point, Direction> player, Point exit) {
    Map<Point, Integer> path = new HashMap<>();
    path.put(player.getKey(), 0);
    return numberOfStepsForExitMaze(maze, player, exit, path, Integer.MAX_VALUE, 0);
  }

  private int numberOfStepsForExitMaze(String[][] maze,
                                       Pair<Point, Direction> player,
                                       Point exit,
                                       Map<Point, Integer> path,
                                       int best,
                                       int steps) {

    if (exit.equals(player.getKey())) {
      System.out.println(steps);
      return steps;
    }

    if (steps >= best){
      return Integer.MAX_VALUE;
    }

    Map<Pair<Point, Direction>, Integer> allowedMovements = new HashMap<>();

    // Straight movement
    Point nextInARow = player.getValue().move(player.getKey());
    if (isEmpty(maze, nextInARow)) {
      allowedMovements.put(Pair.of(nextInARow, player.getValue()), 1);
    }
    // Allowed turns
    Direction dirCw = player.getValue().turnClockwise();
    Point nextCw = dirCw.move(player.getKey());
    if (isEmpty(maze, nextCw)) {
      allowedMovements.put(Pair.of(nextCw, dirCw), 1001);
    }

    Direction dirAcw = player.getValue().turnAntiClockwise();
    Point nextACw = dirAcw.move(player.getKey());
    if (isEmpty(maze, nextACw)) {
      allowedMovements.put(Pair.of(nextACw, dirAcw), 1001);
    }

    if (allowedMovements.isEmpty()) {
      return Integer.MAX_VALUE;
    }

    int minLen = Integer.MAX_VALUE;
    for (var mov : allowedMovements.entrySet()) {
      if (!path.containsKey(mov.getKey().getKey())) {
        path.put(mov.getKey().getKey(), mov.getValue());
        int len = numberOfStepsForExitMaze(maze, mov.getKey(), exit, path, best, steps + mov.getValue());
        best = len == Integer.MAX_VALUE ? best : len;
        path.remove(mov.getKey().getKey());
        minLen = Math.min(minLen, len);
      }
    }

    return minLen;
  }

  private boolean isEmpty(String[][] maze, Point p) {
    return EMPTY.equals(maze[p.x][p.y]);
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
    Day.run(Day16::new, "2024/D16_small.txt", "2024/D16_full.txt");
  }
}
