package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Day6 extends Day {

  private static final String ROCK = "#";
  private static final String EMPTY = ".";
  private static final String START = "^";

  enum Direction {
    UP, RIGHT, DOWN, LEFT;

    Direction turn() {
      return switch (this) {
        case UP -> RIGHT;
        case RIGHT -> DOWN;
        case DOWN -> LEFT;
        case LEFT -> UP;
      };
    }

    Point move(Point p) {
      return switch (this) {
        case UP -> Point.of(p.x - 1, p.y);
        case RIGHT -> Point.of(p.x, p.y + 1);
        case DOWN -> Point.of(p.x + 1, p.y);
        case LEFT -> Point.of(p.x, p.y - 1);
      };
    }
  }

  record Position(Point coords, Direction dir) {
    Position move() {
      return new Position(dir.move(coords), dir);
    }

    Position turn() {
      return new Position(coords, dir.turn());
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] map = MatrixUtils.readMatrixFromFile(fileContents);

    Position me = new Position(getStart(map), Direction.UP);
    map[me.coords.x][me.coords.y] = EMPTY;
    return String.valueOf(walk(me, map, new HashSet<>(), new ArrayList<>()));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[][] map = MatrixUtils.readMatrixFromFile(fileContents);

    Position me = new Position(getStart(map), Direction.UP);
    map[me.coords.x][me.coords.y] = EMPTY;

    Set<Point> visited = new HashSet<>();
    // Normal walk through the map path
    walk(me, map, visited, new ArrayList<>());

    final AtomicInteger loopPaths = new AtomicInteger(0);

    // Then we try with all the visited positions, setting a rock in each one to test if there's a loop
    visited.parallelStream().forEach(p -> {
      if (walk(me, map, new HashSet<>(), List.of(p)) == -1) {
        loopPaths.incrementAndGet();
      }
    });

    return String.valueOf(loopPaths.get());
  }

  private Point getStart(String[][] map) {
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        if (START.equals(map[i][j])) {
          return Point.of(i, j);
        }
      }
    }
    throw new RuntimeException("No start mark detected!");
  }

  private int walk(Position me, String[][] map, Set<Point> visited, List<Point> virtualRocks) {
    boolean inside = true;

    List<Position> path = new ArrayList<>();
    while (inside) {
      Position next = me.move();
      visited.add(me.coords);
      if (path.contains(me)) {
        //LOOP!!!
        return -1;
      }
      path.add(me);
      if (isInside(next, map)) {
        if (ROCK.equals(map[next.coords.x][next.coords.y]) || virtualRocks.contains(next.coords)) {
          me = me.turn();
        } else {
          me = next;
        }
      } else {
        inside = false;
      }
    }
    return visited.size();
  }

  private boolean isInside(Position pos, String[][] map) {
    return pos.coords.x >= 0 && pos.coords.x < map.length && pos.coords.y >= 0 && pos.coords.y < map[0].length;
  }

  public static void main(String[] args) {
    Day.run(Day6::new, "2024/D6_small.txt", "2024/D6_full.txt");
  }
}
