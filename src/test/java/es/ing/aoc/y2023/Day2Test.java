package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day2Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day2::new, "2023/D2_small.txt");
    assertEquals("8", results.a);
    assertEquals("2286", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day2::new, "2023/D2_full.txt");
    assertEquals("2449", results.a);
    assertEquals("63981", results.b);
  }
}
