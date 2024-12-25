package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day25Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day25::new, "2024/D25_small.txt");
    assertEquals("3", results.a);
    assertEquals("", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day25::new, "2024/D25_full.txt");
    assertEquals("3495", results.a);
    assertEquals("", results.b);
  }
}
