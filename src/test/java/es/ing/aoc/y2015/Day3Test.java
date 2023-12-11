package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day3::new, "2015/D3_small.txt");
    assertEquals("4", results.a);
    assertEquals("3", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day3::new, "2015/D3_full.txt");
    assertEquals("2572", results.a);
    assertEquals("2631", results.b);
  }
}
