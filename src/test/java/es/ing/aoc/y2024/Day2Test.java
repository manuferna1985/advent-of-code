package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day2Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day2::new, "2024/D2_small.txt");
    assertEquals("2", results.a);
    assertEquals("4", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day2::new, "2024/D2_full.txt");
    assertEquals("411", results.a);
    assertEquals("465", results.b);
  }
}
