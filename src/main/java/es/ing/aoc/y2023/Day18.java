package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.PolygonUtils;
import org.apache.commons.lang3.StringUtils;
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

  private long getDiggedPoints(String fileContents, BiFunction<String, Point, Pair<Point, Integer>> fn) {
    String[] lines = fileContents.split(System.lineSeparator());

    Point s1 = Point.of(0, 0);
    List<Point> polygon = new ArrayList<>();

    long perimeter = 0L;
    for (String line : lines) {
      Pair<Point, Integer> nextData = fn.apply(line, s1);
      perimeter += nextData.getRight();
      Point s2 = nextData.getLeft();
      polygon.add(s2);
      s1 = s2;
    }
    return (long) (PolygonUtils.shoelaceArea(polygon) + (perimeter / 2.0) + 1);
  }

  private Pair<Point, Integer> getNormalPoint(String line, Point prev) {
    String[] parts = line.split(StringUtils.SPACE);
    int len = Integer.parseInt(parts[1]);
    return Pair.of(getNextPoint(prev, parts[0].charAt(0), len), len);
  }

  private Pair<Point, Integer> getHexPoint(String line, Point prev) {
    String[] parts = line.split(StringUtils.SPACE);
    int len = HexFormat.fromHexDigits(parts[2].substring(2, parts[2].length() - 2));
    return Pair.of(getNextPoint(prev, parts[2].charAt(parts[2].length() - 2), len), len);
  }

  private static Point getNextPoint(Point prev, char dir, int len) {
    return switch (dir) {
      case '0', 'R' -> Point.of(prev.x, prev.y + len);
      case '1', 'D' -> Point.of(prev.x + len, prev.y);
      case '2', 'L' -> Point.of(prev.x, prev.y - len);
      case '3', 'U' -> Point.of(prev.x - len, prev.y);
      default -> throw new IllegalStateException("Unexpected value: " + dir);
    };
  }

  public static void main(String[] args) {
    Day.run(Day18::new, "2023/D18_small.txt", "2023/D18_full.txt");
  }
}
