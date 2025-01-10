package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day9::new, "2015/D9_small.txt");
    assertEquals("", results.a);
    assertEquals("", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day9::new, "2015/D9_full.txt");
    assertEquals("", results.a);
    assertEquals("", results.b);
  }
}
