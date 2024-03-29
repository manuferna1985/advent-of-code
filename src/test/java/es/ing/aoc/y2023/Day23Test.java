package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day23Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day23::new, "2023/D23_small.txt");
    assertEquals("94", results.a);
    assertEquals("154", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day23::new, "2023/D23_full.txt");
    assertEquals("2218", results.a);
    assertEquals("6674", results.b);
  }
}
