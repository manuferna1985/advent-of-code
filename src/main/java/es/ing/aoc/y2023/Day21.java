package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Day21 extends Day {

  public static void main(String[] args) {
    Day.run(Day21::new, "2023/D21_small.txt", "2023/D21_full.txt");
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] map = MatrixUtils.readMatrixFromFile(fileContents);
    Point s = find(map, "S"::equals).get(0);
    if (map.length > 20) {
      return String.valueOf(move(map, s, 64));
    } else {
      return String.valueOf(move(map, s, 6));
    }
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[][] map = MatrixUtils.readMatrixFromFile(fileContents);
    Point s = find(map, "S"::equals).get(0);

    int steps = 26501365;
    int size = map.length;
    int gridWidth = steps / size - 1;
    System.out.printf("gridWidth = %d\n", gridWidth);

    long oddGardens = (long) Math.pow(gridWidth / 2 * 2 + 1, 2);
    long evenGardens = (long) Math.pow((gridWidth + 1) / 2 * 2, 2);
    long oddPoints = move(map, s, size * 2 + 1);
    long evenPoints = move(map, s, size * 2);
    long corners = moveN(map, getMiddlePoints(size, s), size - 1);
    long triangles = moveN(map, getCornerPoints(size), size / 2 - 1);
    long pentagons = moveN(map, getCornerPoints(size), size * 3 / 2 - 1);

    long result = oddGardens * oddPoints +
        evenGardens * evenPoints +
        corners + (gridWidth + 1) * triangles + gridWidth * pentagons;

    return String.valueOf(result);
  }

  private List<Point> getMiddlePoints(int size, Point s) {
    return List.of(
        Point.of(size - 1, s.y),        // N
        Point.of(s.x, 0),               // E
        Point.of(0, s.y),               // S
        Point.of(s.x, size - 1));       // W
  }

  private List<Point> getCornerPoints(int size) {
    return List.of(
        Point.of(size - 1, 0),        // NE
        Point.of(size - 1, size - 1), // NW
        Point.of(0, 0),               // SE
        Point.of(0, size - 1));       // SW
  }

  private long moveN(String[][] map, List<Point> sList, int maxSteps) {
    return sList.stream().map(s -> move(map, s, maxSteps)).mapToLong(Long::valueOf).sum();
  }

  private long move(String[][] map, Point s, int maxSteps) {

    Set<Point> results = new HashSet<>();
    Set<Point> visited = new HashSet<>();
    visited.add(s);
    Deque<Pair<Point, Integer>> queue = new ArrayDeque<>();
    queue.add(Pair.of(s, maxSteps));

    while (!queue.isEmpty()) {
      Pair<Point, Integer> c = queue.pop();

      if (c.getRight() % 2==0) {
        results.add(c.getLeft());
      }

      if (c.getRight()==0) {
        continue;
      }

      for (Pair<Point, String> n : MatrixUtils.getNeighbours(map, c.getKey().x, c.getKey().y, false)) {
        if (!"#".equals(n.getRight()) && !visited.contains(n.getKey())) {
          visited.add(n.getKey());
          queue.add(Pair.of(n.getKey(), c.getValue() - 1));
        }
      }
    }

    return results.size();
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
}
