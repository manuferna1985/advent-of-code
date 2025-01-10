package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day11::new, "2015/D11_small.txt");
    assertEquals("abcdffaa", results.a);
    assertEquals("abcdffbb", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day11::new, "2015/D11_full.txt");
    assertEquals("vzbxxyzz", results.a);
    assertEquals("vzcaabcc", results.b);
  }
}
