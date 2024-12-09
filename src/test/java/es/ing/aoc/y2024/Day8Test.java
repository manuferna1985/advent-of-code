package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day8Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day8::new, "2024/D8_small.txt");
    assertEquals("14", results.a);
    assertEquals("34", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day8::new, "2024/D8_full.txt");
    assertEquals("265", results.a);
    assertEquals("962", results.b);
  }
}
