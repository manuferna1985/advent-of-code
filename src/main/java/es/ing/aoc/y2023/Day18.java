package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.Range;

import java.util.HexFormat;
import java.util.function.BiFunction;

public class Day18 extends Day {

  record Segment(Point p1, Point p2) {
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(getDiggedPoints(fileContents, this::getNormalPoint));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(getDiggedPoints(fileContents, this::getNormalPoint));
  }

  private int getDiggedPoints(String fileContents, BiFunction<String, Point, Point> fn) {
    String[] lines = fileContents.split(System.lineSeparator());

    Point min = Point.of(Integer.MAX_VALUE, Integer.MAX_VALUE);
    Point max = Point.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
    Point s1 = Point.of(0, 0);

    Point[] edges = new Point[lines.length];
    int i=0;
    for (String line : lines) {
      Point s2 = fn.apply(line, s1);
      edges[i++] = s2;
      max = Point.max(max, s2);
      min = Point.min(min, s2);
      s1 = s2;
    }

    int digged = 0;
    for (int x = min.x; x <= max.x; x++) {
      System.out.println(x);
      for (int y = min.y; y <= max.y; y++) {
        if (pointInPoligon(x, y, edges) || pointInsidePoligon(x, y, edges)) {
          digged++;
        }
      }
    }

    return digged;
  }

  private Point getNormalPoint(String line, Point prev) {
    String[] parts = line.split(" ");
    int len = Integer.parseInt(parts[1]);

    Point s2 = switch (parts[0].charAt(0)) {
      case 'R' -> Point.of(prev.x, prev.y + len);
      case 'D' -> Point.of(prev.x + len, prev.y);
      case 'L' -> Point.of(prev.x, prev.y - len);
      case 'U' -> Point.of(prev.x - len, prev.y);
      default -> throw new IllegalStateException("Unexpected value: " + parts[0].charAt(0));
    };
    return s2;
  }

  private Point getHexPoint(String line, Point prev) {
    String[] parts = line.split(" ");
    int len = HexFormat.fromHexDigits(parts[2].substring(2, parts[2].length() - 2));

    Point s2 = switch (parts[2].charAt(parts[2].length() - 2)) {
      case '0' -> Point.of(prev.x, prev.y + len);
      case '1' -> Point.of(prev.x + len, prev.y);
      case '2' -> Point.of(prev.x, prev.y - len);
      case '3' -> Point.of(prev.x - len, prev.y);
      default -> throw new IllegalStateException("Unexpected value: " + parts[0].charAt(0));
    };
    return s2;
  }

  private boolean pointInPoligon(int x, int y, Point[] edges) {

    for (int i = 0, j=1; i < edges.length; i++) {
      if (edges[i].x == x && edges[j].x == x && Range.between(edges[i].y, edges[j].y).contains(y)){
        return true;
      }
      if (edges[i].y == y && edges[j].y == y && Range.between(edges[i].x, edges[j].x).contains(x)){
        return true;
      }

      j = (j + 1) % edges.length;
    }
    return false;
  }

  private boolean pointInsidePoligon(int x, int y, Point[] edges) {
    int i = 0;
    int j = edges.length - 1;
    boolean ok = false;

    for (i = 0; i < edges.length; i++) {
      if ((edges[i].y < y && edges[j].y >= y) || (edges[j].y < y && edges[i].y >= y)) {
        if (edges[i].x + (y - edges[i].y) / (edges[j].y - edges[i].y) * (edges[j].x - edges[i].x) < x) {
          ok = !ok;
        }
      }
      j = i;
    }

    return ok;
  }

  public static void main(String[] args) {
    Day.run(Day18::new, "2023/D18_small.txt", "2023/D18_full.txt");
  }
}
