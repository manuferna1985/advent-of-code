package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day10::new, "2024/D10_small.txt");
    assertEquals("36", results.a);
    assertEquals("81", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day10::new, "2024/D10_full.txt");
    assertEquals("535", results.a);
    assertEquals("1186", results.b);
  }
}
