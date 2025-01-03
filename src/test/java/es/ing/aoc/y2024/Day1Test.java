package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day1::new, "2024/D1_small.txt");
    assertEquals("11", results.a);
    assertEquals("31", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day1::new, "2024/D1_full.txt");
    assertEquals("1222801", results.a);
    assertEquals("22545250", results.b);
  }
}
