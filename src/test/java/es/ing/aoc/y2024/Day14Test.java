package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day14::new, "2024/D14_small.txt");
    assertEquals("12", results.a);
    assertEquals("1", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day14::new, "2024/D14_full.txt");
    assertEquals("225552000", results.a);
    assertEquals("7371", results.b);
  }
}
