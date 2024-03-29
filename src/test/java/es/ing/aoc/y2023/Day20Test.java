package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day20Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day20::new, "2023/D20_small.txt");
    assertEquals("32000000", results.a);
    assertEquals("N/A", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day20::new, "2023/D20_full.txt");
    assertEquals("791120136", results.a);
    assertEquals("215252378794009", results.b);
  }
}
