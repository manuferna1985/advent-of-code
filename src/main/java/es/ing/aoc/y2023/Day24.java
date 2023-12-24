package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.LongPoint;

import java.util.ArrayList;
import java.util.List;

public class Day24 extends Day {

  record Hail(LongPoint p, LongPoint v) {}

  @Override
  protected String part1(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());

    List<Hail> hails = new ArrayList<>();
    for (String line : lines) {
      String[] parts = line.split("@");
      hails.add(new Hail(
          LongPoint.of(parts[0].trim().split(",")),
          LongPoint.of(parts[1].trim().split(","))));
    }

    if (lines.length < 10) {
      return String.valueOf(searchIntersections(hails, 7L, 27L));
    } else {
      return String.valueOf(searchIntersections(hails, 200000000000000L, 400000000000000L));
    }
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());


    return String.valueOf("");
  }

  private int searchIntersections(List<Hail> hails, long minArea, long maxArea) {
    int counter = 0;
    for (int i = 0; i < hails.size(); i++) {
      for (int j = i + 1; j < hails.size(); j++) {
        if (haveIntersection(hails.get(i), hails.get(j), minArea, maxArea)) {
          counter++;
        }
      }
    }
    return counter;
  }

  private boolean haveIntersection(Hail one, Hail two, long minArea, long maxArea) {

    /*
        Hailstone A: 20, 25, 34 @ -2, -2, -4
        Hailstone B: 12, 31, 28 @ -1, -2, -1
        Hailstones' paths will cross outside the test area (at x=-2, y=3).
     */

    double diff1 = two.p.x - one.p.x;
    double diff2 = two.p.y - one.p.y;

    double right = one.v.x * diff2 - one.v.y * diff1;
    double left = two.v.x * one.v.y - two.v.y * one.v.x;

    if (left!=0) {
      double b = right / left;
      double a = (two.p.x + b * two.v.x - one.p.x) / one.v.x;

      double x = one.p.x + one.v.x * a;
      double y = one.p.y + one.v.y * a;

      System.out.printf("%-35s VS     %-35s --> [%f,%f]\n", one, two, x, y);
      return isFuturePath(one, x, y) && isFuturePath(two, x, y) && between(minArea, maxArea, x) && between(minArea, maxArea, y);
    }

    return false;
  }

  private boolean isFuturePath(Hail hail, double x, double y) {
    return isFuturePath(hail.p.x, hail.v.x, x) && isFuturePath(hail.p.y, hail.v.y, y);
  }

  private boolean isFuturePath(Long pos, Long vel, double test) {
    if (vel > 0) {
      return test >= pos;
    } else {
      return test <= pos;
    }
  }

  private boolean between(long min, long max, double test) {
    return min <= test && max >= test;
  }

  public static void main(String[] args) {
    Day.run(Day24::new, "2023/D24_small.txt", "2023/D24_full.txt");
  }
}
