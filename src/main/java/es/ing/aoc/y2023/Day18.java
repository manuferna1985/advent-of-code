package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.function.BiFunction;

public class Day18 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(getDiggedPoints(fileContents, this::getNormalPoint));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(getDiggedPoints(fileContents, this::getHexPoint));
  }

  private double getDiggedPoints(String fileContents, BiFunction<String, Point, Pair<Point, Integer>> fn) {
    String[] lines = fileContents.split(System.lineSeparator());

    Point min = Point.of(Integer.MAX_VALUE, Integer.MAX_VALUE);
    Point max = Point.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
    Point s1 = Point.of(0, 0);

    List<Point> polygon = new ArrayList<>();

    for (String line : lines) {
      Pair<Point, Integer> nextData = fn.apply(line, s1);
      Point s2 = nextData.getLeft();
      polygon.add(s2);
      max = Point.max(max, s2);
      min = Point.min(min, s2);
      s1 = s2;
    }
    // (int(area-0.5*edge+1)+edge)
    //return shoelaceArea(polygon) + (perimeter / 2.0) + 1;

    polygon.add(polygon.get(0));

    return area(polygon) / 2 + 1;
  }

  private Pair<Point, Integer> getNormalPoint(String line, Point prev) {
    String[] parts = line.split(" ");
    int len = Integer.parseInt(parts[1]);
    return Pair.of(getNextPoint(prev, parts[0].charAt(0), len), len);
  }

  private Pair<Point, Integer> getHexPoint(String line, Point prev) {
    String[] parts = line.split(" ");
    int len = HexFormat.fromHexDigits(parts[2].substring(2, parts[2].length() - 2));
    return Pair.of(getNextPoint(prev, parts[2].charAt(parts[2].length() - 2), len), len);
  }

  private static Point getNextPoint(Point prev, char dir, int len) {
    //System.out.printf("%s - %d\n", dir, len);

    return switch (dir) {
      case '0', 'R' -> Point.of(prev.x, prev.y + len);
      case '1', 'D' -> Point.of(prev.x + len, prev.y);
      case '2', 'L' -> Point.of(prev.x, prev.y - len);
      case '3', 'U' -> Point.of(prev.x - len, prev.y);
      default -> throw new IllegalStateException("Unexpected value: " + dir);
    };
  }

  // Shoelace formula, found it on the internet:
  // https://rosettacode.org/wiki/Shoelace_formula_for_polygonal_area#Java#
  private double shoelaceArea(Point[] v) {
    int n = v.length;
    double a = 0.0;
    for (int i = 0; i < n - 1; i++) {
      a += v[i].x * v[i + 1].y - v[i + 1].x * v[i].y;
    }
    return Math.abs(a + v[n - 1].x * v[0].y - v[0].x * v[n - 1].y) / 2.0;
  }

  private long area(List<Point> v) {
    long result = 0L;

    int i = v.size() - 1;
    for (; i > 0; i--) {
      Point p1 = v.get(i - 1);
      Point p2 = v.get(i);

      System.out.printf("%d %d %d\n", i, p2.x, p2.y);
      System.out.printf("%d %d %d\n", i - 1, p1.x, p1.y);

      long a = (long) p2.y * (long) p1.x;
      long b = (long) p2.x * (long) p1.y;
      long c = Math.abs(p2.x - p1.x);
      long d = Math.abs(p2.y - p1.y);

      long e = c + d;
      e += (b - a);

      System.out.printf("----------------------- %d\n", e);

      result += e;
    }
    return result;

  }

  public static void main(String[] args) {
    Day.run(Day18::new, "2023/D18_small.txt");//, "2023/D18_full.txt");
  }
}
