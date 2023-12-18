package es.ing.aoc.common;

import java.util.List;
import java.util.function.BiFunction;

public class PolygonUtils {

  private PolygonUtils() {
    throw new RuntimeException("Constructor not meant to be called");
  }

  // Shoelace formula, found it on the internet:
  // https://rosettacode.org/wiki/Shoelace_formula_for_polygonal_area#Java#
  public static long shoelaceArea(List<Point> v) {
    int n = v.size();
    double a = 0.0;
    BiFunction<Point, Point, Long> fn = (p1, p2) -> (long)p1.x * (long)p2.y - (long)p2.x * (long)p1.y;

    for (int i = 0; i < n - 1; i++) {
      a += fn.apply(v.get(i), v.get(i+1));
    }
    return (long) (Math.abs(a + fn.apply(v.get(n-1), v.get(0))) / 2.0);
  }
}
