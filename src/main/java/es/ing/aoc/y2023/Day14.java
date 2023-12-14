package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Day14 extends Day {

  private static final PointFunction N = new PointFunction(x -> x - 1, y -> y);
  private static final PointFunction S = new PointFunction(x -> x + 1, y -> y);
  private static final PointFunction E = new PointFunction(x -> x, y -> y + 1);
  private static final PointFunction W = new PointFunction(x -> x, y -> y - 1);

  private static final int ITERATIONS = 1000000000;
  private static final int MAX_SEQS_CACHE = 100;
  private static final int LENGTH_FOR_PATTERN_SEARCH = 10;

  record PointFunction(Function<Integer, Integer> xFn, Function<Integer, Integer> yFn) {
  }

  record Map(List<Point> map, List<Point> rocks, List<Point> walls) {
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);
    Map map = buildRocksAndWalls(matrix);
    tiltRocks(matrix, map, N);
    return String.valueOf(getNorthOverload(matrix, map.rocks));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);
    Map map = buildRocksAndWalls(matrix);

    List<Integer> loadSeqs = new ArrayList<>();

    for (int i = 0; i < ITERATIONS; i++) {
      tiltRocks(matrix, map, N);
      tiltRocks(matrix, map, W);
      tiltRocks(matrix, map, S);
      tiltRocks(matrix, map, E);

      int overload = getNorthOverload(matrix, map.rocks);

      loadSeqs.add(overload);
      if (loadSeqs.size() > MAX_SEQS_CACHE) {
        loadSeqs.remove(0);
      }

      int pattern = checkSequenceRepeatPattern(loadSeqs);

      if (pattern > 0) {
        System.out.printf("Pattern detected: %d  %d\n", i, pattern);
        while ((i + pattern) < ITERATIONS) {
          i += pattern;
        }
      }
    }
    return String.valueOf(getNorthOverload(matrix, map.rocks));
  }

  private int checkSequenceRepeatPattern(List<Integer> loadSeqs) {
    if (loadSeqs.size() > LENGTH_FOR_PATTERN_SEARCH) {
      List<Integer> lastN = loadSeqs.subList(loadSeqs.size() - LENGTH_FOR_PATTERN_SEARCH, loadSeqs.size());

      int firstOcurrence = Collections.indexOfSubList(loadSeqs, lastN);
      int lastOcurrence = Collections.lastIndexOfSubList(loadSeqs, lastN);

      if (firstOcurrence > 0 && lastOcurrence > 0) {
        return lastOcurrence - firstOcurrence;
      }
    }
    return 0;
  }

  private Map buildRocksAndWalls(String[][] matrix) {
    List<Point> rocks = new ArrayList<>(), walls = new ArrayList<>(), map = new ArrayList<>();
    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {
        switch (matrix[x][y]) {
          case "O" -> rocks.add(Point.of(x, y));
          case "#" -> walls.add(Point.of(x, y));
        }
      }
    }
    map.addAll(rocks);
    map.addAll(walls);
    return new Map(map, rocks, walls);
  }

  private int getNorthOverload(String[][] matrix, List<Point> rocks) {
    return rocks.stream().mapToInt(p -> matrix.length - p.x).sum();
  }

  private void tiltRocks(String[][] matrix, Map map, PointFunction fn) {
    boolean movs;
    do {
      movs = false;
      for (Point rock : map.rocks) {
        boolean currentRockRoll;
        do {
          currentRockRoll = false;
          Point newRockPoint = Point.of(fn.xFn().apply(rock.x), fn.yFn().apply(rock.y));

          if (pointIsWithinLimits(matrix, newRockPoint) && !map.map.contains(newRockPoint)) {
            movs = true;
            currentRockRoll = true;
            rock.x = newRockPoint.x;
            rock.y = newRockPoint.y;
          }
        } while (currentRockRoll);
      }
    } while (movs);
  }

  private boolean pointIsWithinLimits(String[][] matrix, Point p) {
    return p.x >= 0 && p.x < matrix.length && p.y >= 0 && p.y < matrix[0].length;
  }

  public static void main(String[] args) {
    Day.run(Day14::new, "2023/D14_small.txt", "2023/D14_full.txt");
  }
}
