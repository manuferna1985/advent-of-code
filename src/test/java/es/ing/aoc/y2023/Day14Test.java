package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day14::new, "2023/D14_small.txt");
    assertEquals("136", results.a);
    assertEquals("", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day14::new, "2023/D14_full.txt");
    assertEquals("107430", results.a);
    assertEquals("", results.b);
  }
}
