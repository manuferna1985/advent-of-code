package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day5Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day5::new, "2015/D5_small.txt");
    assertEquals("2", results.a);
    assertEquals("0", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day5::new, "2015/D5_full.txt");
    assertEquals("255", results.a);
    assertEquals("55", results.b);
  }
}
