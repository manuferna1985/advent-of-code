package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.List;
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
    findLongestPath(start, end, matrix, 0L, maxDistance, new HashSet<>(), null, true);
    return String.valueOf(maxDistance.get());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);
    Point start = Point.of(0, 1);
    Point end = Point.of(matrix.length - 1, matrix[0].length - 2);
    AtomicLong maxDistance = new AtomicLong(0L);
    findLongestPath(start, end, matrix, 0L, maxDistance, new HashSet<>(), null, false);
    return String.valueOf(maxDistance.get());
  }

  private void findLongestPath(Point start, Point end, String[][] matrix, long currentLength, AtomicLong max, Set<Point> alreadyVisited, Direction forcedDir, boolean slipperySlopes) {

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
            findLongestPath(n.getKey(), end, matrix, currentLength + 1, max, alreadyVisited, dir, slipperySlopes);
            alreadyVisited.remove(n.getKey());
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


  public static void main(String[] args) {
    Day.run(Day23::new, "2023/D23_full.txt");//, "2023/D23_full.txt");
  }
}
