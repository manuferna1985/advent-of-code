package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day18Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day18::new, "2024/D18_small.txt");
    assertEquals("22", results.a);
    assertEquals("6,1", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day18::new, "2024/D18_full.txt");
    assertEquals("336", results.a);
    assertEquals("24,30", results.b);
  }
}
