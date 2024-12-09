package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day9::new, "2024/D9_small.txt");
    assertEquals("1928", results.a);
    assertEquals("2858", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day9::new, "2024/D9_full.txt");
    assertEquals("6334655979668", results.a);
    assertEquals("6349492251099", results.b);
  }
}
