package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day20Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day20::new, "2024/D20_small.txt");
    assertEquals("", results.a);
    assertEquals("", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day20::new, "2024/D20_full.txt");
    assertEquals("", results.a);
    assertEquals("", results.b);
  }
}
