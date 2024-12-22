package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day22::new, "2024/D22_small.txt");
    assertEquals("37327623", results.a);
    assertEquals("24", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day22::new, "2024/D22_full.txt");
    assertEquals("19458130434", results.a);
    assertEquals("2130", results.b);
  }
}
