package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

  @Test
  void testSmallProblem() {
    Pair<String, String> results = Day.run(Day13::new, "2015/D13_small.txt");
    assertEquals("330", results.a);
    assertEquals("286", results.b);
  }

  @Test
  void testFullProblem() {
    Pair<String, String> results = Day.run(Day13::new, "2015/D13_full.txt");
    assertEquals("709", results.a);
    assertEquals("668", results.b);
  }
}
