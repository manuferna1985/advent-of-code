package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day1::new, "2023/D1_small.txt");
    assertEquals("142", results.a);
    assertEquals("142", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day1::new, "2023/D1_full.txt");
    assertEquals("55029", results.a);
    assertEquals("55686", results.b);
  }
}
